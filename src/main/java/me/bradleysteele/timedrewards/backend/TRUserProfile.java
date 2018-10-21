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

package me.bradleysteele.timedrewards.backend;

import com.google.common.collect.Maps;
import me.bradleysteele.commons.util.OfflinePlayers;
import me.bradleysteele.timedrewards.menu.RewardItem;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author Bradley Steele
 */
public class TRUserProfile {

    private final UUID uuid;
    private Map<String, Long> claimTimes = Maps.newHashMap();

    public TRUserProfile(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return user's unique id.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return user's name.
     */
    public String getName() {
        return OfflinePlayers.getName(uuid);
    }

    /**
     * @param item the reward item to check.
     *
     * @return {@code true} if the reward is claimable.
     */
    public boolean isClaimable(RewardItem item) {
        return getCooldownRemaining(item) <= 0;
    }

    /**
     * @param key reward key.
     * @return time, in milliseconds, that the reward was claimed.
     */
    public long getClaimTime(String key) {
        if (claimTimes.containsKey(key)) {
            return claimTimes.get(key);
        }

        // Haven't claimed.
        claimTimes.put(key, 0L);
        return 0L;
    }

    public long getClaimTime(RewardItem item) {
        return getClaimTime(item.getKey());
    }

    /**
     * @param item reward item.
     * @return the remaining time, in milliseconds, before the reward
     *         can be claimed again.
     */
    public long getCooldownRemaining(RewardItem item) {
        return (getClaimTime(item) + item.getCooldown()) - System.currentTimeMillis();
    }

    /**
     * @return unmodifiable map of claim times.
     */
    public Map<String, Long> getClaimTimes() {
        return Collections.unmodifiableMap(claimTimes);
    }

    /**
     * Sets the time a player claimed a reward, as well as queuing the
     * profile in the store.
     *
     * @param key       reward key.
     * @param claimTime time it was claimed (milliseconds).
     */
    public void setClaimTime(String key, long claimTime) {
        claimTimes.put(key, claimTime);
    }

    public void setClaimTimes(RewardItem item, long claimTime) {
        setClaimTime(item.getKey(), claimTime);
    }
}