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

import me.bradleysteele.commons.itemstack.ItemStacks;
import me.bradleysteele.commons.register.worker.BWorker;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.timedrewards.resource.ResourceType;
import me.bradleysteele.timedrewards.resource.Resources;
import org.bukkit.Material;

/**
 * @author Bradley Steele
 */
public class WorkerRewardMenu extends BWorker {

    private static final WorkerRewardMenu instance = new WorkerRewardMenu();

    public static WorkerRewardMenu get() {
        return instance;
    }

    private RewardMenu rewardMenu;

    private WorkerRewardMenu() {
        this.setPeriod(2L);
        this.setSync(false);
    }

    @Override
    public void onRegister() {
        rewardMenu = loadRewardMenu();
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

    // Getters

    /**
     * @return active reward menu.
     */
    public RewardMenu getRewardMenu() {
        return rewardMenu;
    }
}