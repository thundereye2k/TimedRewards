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

package me.bradleysteele.timedrewards.command.timedrewards;

import me.bradleysteele.commons.register.command.BCommand;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.timedrewards.command.CmdRewards;
import org.bukkit.command.CommandSender;

/**
 * @author Bradley Steele
 */
public class CmdTimedRewards extends BCommand {

    public CmdTimedRewards() {
        this.setAliases("timedrewards", "timedreward", "tr", "trs");
        this.setDescription("The main TimedRewards command.");
        this.setUsage("/timedrewards");

        this.setAllowConsole(true);

        this.setChildren(new CmdTimedRewardsReload());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Players.isOnline(sender)) {
            CmdRewards.get().execute(sender, args);
        }
    }
}