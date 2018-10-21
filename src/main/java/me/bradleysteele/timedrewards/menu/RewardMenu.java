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

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

/**
 * @author Bradley Steele
 */
public class RewardMenu {

    private final String key;

    private String title;
    private int size;
    private final Set<RewardItem> rewardItems = Sets.newHashSet();

    public RewardMenu(String key) {
        this.key = key;
    }

    /**
     * @return reward menu key.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return title of the menu's inventory.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return size of the menu's inventory.
     */
    public int getSize() {
        return size;
    }

    /**
     * @param key item key.
     * @return the first reward item matching the provided key.
     */
    public RewardItem getRewardItem(String key) {
        return getRewardItems().stream()
                .filter(item -> item.getKey().equalsIgnoreCase(key))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return unmodifiable set of the menu's {@link RewardItem}s.
     */
    public Set<RewardItem> getRewardItems() {
        return Collections.unmodifiableSet(rewardItems);
    }

    /**
     * @param title the title of the menu's inventory.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param size the size of the menu's inventory.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @param rewardItem the reward item to add.
     */
    public void addRewardItem(RewardItem rewardItem) {
        rewardItems.add(rewardItem);
    }

    /**
     * @param rewardItem the reward item to remove.
     */
    public void removeRewardItem(RewardItem rewardItem) {
        rewardItems.remove(rewardItem);
    }
}