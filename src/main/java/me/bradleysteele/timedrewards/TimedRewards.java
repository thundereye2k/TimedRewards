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

package me.bradleysteele.timedrewards;

import me.bradleysteele.commons.BPlugin;
import me.bradleysteele.timedrewards.backend.StoreTRUserProfile;
import me.bradleysteele.timedrewards.menu.WorkerRewardMenu;
import me.bradleysteele.timedrewards.resource.Resources;

/**
 * @author Bradley Steele
 */
public class TimedRewards extends BPlugin {

    @Override
    public void enable() {
        console.info("TimedRewards (free) enable start. Discord support server: &ahttps://redirect.bradleysteele.me/bps&7.");
        console.info("Purchase the premium version for multiple menus, NPCs, unclaimed an claimed items, vote rewards,"
                + " in-game management and more. https://www.spigotmc.org/resources/46501/");

        this.register(
                Resources.class,

                StoreTRUserProfile.class,

                WorkerRewardMenu.class
        );
    }
}