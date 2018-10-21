/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.bradleysteele.timedrewards.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.bradleysteele.commons.itemstack.ItemStacks;
import me.bradleysteele.commons.register.worker.BWorker;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.timedrewards.backend.StoreTRUserProfile;
import me.bradleysteele.timedrewards.backend.TRUserProfile;
import me.bradleysteele.timedrewards.inventory.InvRewards;
import me.bradleysteele.timedrewards.resource.ResourceType;
import me.bradleysteele.timedrewards.resource.Resources;
import me.bradleysteele.timedrewards.resource.yml.Config;
import me.bradleysteele.timedrewards.resource.yml.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Bradley Steele
 */
public class WorkerRewardMenu extends BWorker {

    private static final WorkerRewardMenu instance = new WorkerRewardMenu();

    public static WorkerRewardMenu get() {
        return instance;
    }

    private RewardMenu rewardMenu;
    private final Map<String, String> cooldownFormats = Maps.newHashMap();

    private WorkerRewardMenu() {
        this.setPeriod(2L);
        this.setSync(false);
    }

    @Override
    public void onRegister() {

        ResourceSection section = Locale.getLocale().getSection("reward-cooldown.formats");

        if (section != null) {
            section.getKeys().forEach(key -> cooldownFormats.put(Pattern.quote("{" + key + "}"), section.getString(key)));
        }

        long start = System.currentTimeMillis();
        rewardMenu = loadRewardMenu();

        plugin.getConsole().info("Loaded &e" + rewardMenu.getRewardItems().size() + " &7reward items (time taken: &e" + (System.currentTimeMillis() - start) + "&7ms).");
    }

    @Override
    public void onUnregister() {
        setRewardMenu(null);
    }

    @Override
    public void run() {
        getViewingPlayers().forEach(this::updateViewer);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getUniqueId().toString().equals("0f076e56-63b0-45c2-8a3c-390316fe8378")) {
            Players.sendMessage(player, Messages.colour("&7This server is running &6Timed Rewards &7(free).") );
        }

        if (player.isOp()) {
            int current = Config.CONFIG_VERSION.getAsInt();
            int latest = Config.CONFIG_VERSION.getAsIntDefault();

            if (current < latest) {
                Players.sendMessage(player, Locale.CONFIG_CHANGE_AVAILABLE.getMessage("{current}", current, "{new}", latest));
            }
        }
    }

    /**
     * Loads the contents of {@link ResourceType#REWARD_MENU} into
     * a {@link RewardMenu}.
     */
    public synchronized RewardMenu loadRewardMenu() {
        ResourceSection menuSection = Resources.get().getResource(ResourceType.REWARD_MENU).getSection("menu");
        ResourceSection itemsSection = menuSection.getSection("items");
        ResourceSection itemSection;

        RewardMenu menu = new RewardMenu("default");
        menu.setTitle(Messages.colour(menuSection.getString("title")));
        menu.setSize(menuSection.getInt("size", 9));

        RewardItem item;

        // Iterate through each item.
        for (String key : itemsSection.getKeys()) {
            itemSection = itemsSection.getSection(key);

            item = new RewardItem(key, itemSection.getString("permission"), itemSection.getLong("cooldown") * 1000);
            item.setSlot(itemSection.getInt("slot"));

            if (!itemSection.contains("item")) {
                plugin.getConsole().error("Failed to load unclaimed item &c" + key + "&r: missing unclaimed section.");
                continue;
            }

            // Safe build item
            Material material = Material.matchMaterial(itemSection.getString("material", "AIR"));

            if (material == Material.AIR) {
                plugin.getConsole().error("Failed to load unclaimed item &c" + key + "&r: invalid material.");
                continue;
            }

            int amount = itemSection.getInt("amount", 1);

            item.setItem(ItemStacks.builder(material)
                    .withDurability(itemSection.getShort("damage"))
                    .withAmount(amount < 1 ? 1 : amount > 64 ? 64 : amount)
                    .withDisplayNameColoured(itemSection.getString("name"))
                    .withLoreColoured(itemSection.getStringList("lore"))
                    .withNBTString(RewardItem.NBT_KEY, key)
                    .build());

            // Commands
            item.setCommands(itemSection.getStringList("commands"));

            menu.addRewardItem(item);
        }

        return menu;
    }

    public void reloadRewardMenu() {
        setRewardMenu(loadRewardMenu());
    }

    /**
     * Handles backend storage of the claim time and handling
     * post reward.
     *
     * @param profile user profile to modify.
     * @param item    the reward item to claim.
     */
    public void claimRewardItem(TRUserProfile profile, RewardItem item) {
        profile.setClaimTime(item, System.currentTimeMillis());

        // Save profile
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> StoreTRUserProfile.get().getBackend().store(profile));

        Player player = Players.getPlayer(profile.getUUID());

        // Run commands
        CommandSender sender;

        for (String command : item.getCommands()) {
            sender = command.toUpperCase().startsWith("[PLAYER]") ? player : Bukkit.getConsoleSender();

            if (sender == null) {
                continue;
            }

            command = command.replaceAll("(?i)\\[PLAYER]", "")
                    .replaceAll("(?i)\\[CONSOLE]", "")
                    .replaceAll("(?i)\\{(player|name)}", player.getName())
                    .replaceAll("(?i)\\{uuid}", player.getUniqueId().toString())
                    .trim();

            Bukkit.dispatchCommand(sender, command);
        }
    }

    public Inventory updateViewer(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        if (!isRewardMenu(inventory)) {
            return inventory;
        }

        RewardMenu menu = ((InvRewards) inventory.getHolder()).getRewardMenu();
        TRUserProfile profile = StoreTRUserProfile.get().retrieve(player.getUniqueId());

        ItemStack stack;
        ItemMeta meta;

        for (RewardItem item : menu.getRewardItems()) {
            stack = item.getItem().clone();
            meta = stack.getItemMeta();

            if (meta != null && meta.hasLore()) {
                List<String> lore = Lists.newArrayList();

                for (String line : meta.getLore()) {
                    String s = line.replaceAll("(?i)\\{(player|name)}", profile.getName())
                            .replaceAll("(?i)\\{uuid}", profile.getUUID().toString());

                    for (Map.Entry<String, String> entry : cooldownFormats.entrySet()) {
                        s = s.replaceAll("(?i)" + entry.getKey(), item.format(entry.getValue(), profile));
                    }

                    lore.add(Messages.colour(s));
                }

                meta.setLore(lore);
                stack.setItemMeta(meta);
            }


            inventory.setItem(item.getSlot(), stack);
        }

        return inventory;
    }

    /**
     * Closes all player's inventories which are an instance
     * of {@link RewardMenu}.
     *
     * @see #getViewingPlayers()
     */
    public void closeViewers() {
        getViewingPlayers().forEach(this::closeViewer);
    }

    /**
     * @param player viewer to close.
     */
    public void closeViewer(Player player) {
        player.closeInventory();
    }


    // Getters

    /**
     * @return active reward menu.
     */
    public RewardMenu getRewardMenu() {
        return rewardMenu;
    }

    /**
     * @return players viewing a reward menu.
     */
    public Set<Player> getViewingPlayers() {
        return Players.getOnlinePlayers().stream()
                .filter(player -> isRewardMenu(player.getOpenInventory().getTopInventory()))
                .collect(Collectors.toSet());
    }

    /**
     * @param menu expected reward menu.
     * @return players viewing the provided reward menu.
     */
    public Set<Player> getViewingPlayers(RewardMenu menu) {
        return getViewingPlayers().stream()
                .filter(player -> ((InvRewards) player.getOpenInventory().getTopInventory().getHolder()).getRewardMenu().equals(menu))
                .collect(Collectors.toSet());
    }

    /**
     * @param inventory the inventory to check.
     * @return {@code true} if the provided inventory is a rewards menu.
     */
    public boolean isRewardMenu(Inventory inventory) {
        return inventory != null && inventory.getHolder() instanceof InvRewards;
    }

    // Setters

    /**
     * Removes the previous reward menu and closes any
     * viewers' inventories.
     *
     * @param menu reward menu to use.
     */
    public void setRewardMenu(RewardMenu menu) {
        getViewingPlayers(menu).forEach(HumanEntity::closeInventory);
        this.rewardMenu = menu;
    }
}