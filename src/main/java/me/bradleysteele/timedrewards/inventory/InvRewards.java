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

package me.bradleysteele.timedrewards.inventory;

import me.bradleysteele.commons.inventory.BInventory;
import me.bradleysteele.commons.itemstack.ItemStacks;
import me.bradleysteele.commons.itemstack.nbt.NBTItemStack;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.timedrewards.TimedRewards;
import me.bradleysteele.timedrewards.backend.StoreTRUserProfile;
import me.bradleysteele.timedrewards.backend.TRUserProfile;
import me.bradleysteele.timedrewards.menu.RewardItem;
import me.bradleysteele.timedrewards.menu.RewardMenu;
import me.bradleysteele.timedrewards.menu.WorkerRewardMenu;
import me.bradleysteele.timedrewards.resource.yml.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bradley Steele
 */
public class InvRewards implements BInventory {

    private final RewardMenu menu;
    private final Inventory inventory;

    public InvRewards(RewardMenu menu) {
        this.menu = menu;
        this.inventory = Bukkit.createInventory(this, menu.getSize(), Messages.colour(menu.getTitle()));
    }

    @Override
    public void onClick(InventoryClickEvent event, Player clicker, ItemStack stack) {
        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        NBTItemStack nbtStack = ItemStacks.toNBTItemStack(stack);

        if (nbtStack.hasKey(RewardItem.NBT_KEY)) {
            RewardItem item = menu.getRewardItem(nbtStack.getString(RewardItem.NBT_KEY));
            TRUserProfile profile = StoreTRUserProfile.get().retrieve(clicker.getUniqueId());

            if (item.getPermission() != null && !clicker.hasPermission(item.getPermission())) {
                Players.sendMessage(clicker, Locale.REWARD_NO_PERM.getMessage());
                return;
            }

            if (!profile.isClaimable(item)) {
                Players.sendMessage(clicker, Locale.REWARD_ALREADY_CLAIMED.getMessage());
                return;
            }

            WorkerRewardMenu.get().claimRewardItem(profile, item);
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event, Player clicker, ItemStack stack) {
        event.setCancelled(true);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public RewardMenu getRewardMenu() {
        return menu;
    }
}
