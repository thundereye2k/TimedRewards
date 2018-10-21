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
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Bradley Steele
 */
public class RewardItem {

    private final static Pattern PATTERN_DAYS = Pattern.compile("(?i)" + Pattern.quote("{days}"));
    private final static Pattern PATTERN_HOURS = Pattern.compile("(?i)" + Pattern.quote("{hours}"));
    private final static Pattern PATTERN_MINUTES = Pattern.compile("(?i)" + Pattern.quote("{minutes}"));
    private final static Pattern PATTERN_SECONDS = Pattern.compile("(?i)" + Pattern.quote("{seconds}"));

    private final String key;

    // Attributes
    private String permission;
    private long cooldown;
    private ItemStack item;
    private int slot;

    private final List<String> commands = Lists.newArrayList();

    public RewardItem(String key, String permission, long cooldown) {
        this.key = key;
        this.permission = permission;
        this.cooldown = cooldown;
    }

    public RewardItem(String key, long cooldown) {
        this(key, null, cooldown);
    }

    // Getters

    /**
     * @return reward item's identifier.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return permission node required to claim the reward.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @return time in milliseconds after claim until the
     *         reward can be claimed again.
     */
    public long getCooldown() {
        return cooldown;
    }

    /**
     * @return item to display.
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return slot in the inventory that the item is set at.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return list of commands to execute upon claiming.
     */
    public List<String> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    // Setters

    /**
     * @param permission node required to claim the reward.
     */
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    /**
     * @param cooldown wait time until the reward can be
     *                 reclaimed.
     */
    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * @param item the item to display.
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Does not the item in the {@link RewardMenu}'s inventory.
     *
     * @param slot slot in the inventory that the item is set at.
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * @param commands list of commands to run when the reward is claimed.
     */
    public void setCommands(List<String> commands) {
        this.commands.clear();
        this.commands.addAll(commands);
    }

    /**
     * @param command the command to add.
     */
    public void addCommand(String command) {
        commands.add(command);
    }

    /**
     * @param command the command to remove.
     */
    public void removeCommand(String command) {
        commands.remove(command);
    }
}