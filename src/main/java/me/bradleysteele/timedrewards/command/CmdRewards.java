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

package me.bradleysteele.timedrewards.command;

import me.bradleysteele.commons.register.command.BCommand;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.timedrewards.inventory.InvRewards;
import me.bradleysteele.timedrewards.menu.RewardMenu;
import me.bradleysteele.timedrewards.menu.WorkerRewardMenu;
import me.bradleysteele.timedrewards.resource.yml.Config;
import me.bradleysteele.timedrewards.resource.yml.Locale;
import org.bukkit.command.CommandSender;

/**
 * @author Bradley Steele
 */
public class CmdRewards extends BCommand {

    private static final CmdRewards instance = new CmdRewards();

    public static CmdRewards get() {
        return instance;
    }

    private CmdRewards() {
        this.setAliases("rewards");
        Config.REWARDS_ALIASES.getAsStringList().forEach(this::addAlias);

        this.setDescription("Opens a rewards inventory.");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Config.REWARDS_ENABLED.getAsBoolean()) {
            RewardMenu menu = WorkerRewardMenu.get().getRewardMenu();

            if (menu != null) {
                Players.getPlayer(sender).openInventory(new InvRewards(menu).getInventory());
            }
        } else {
            Players.sendMessage(sender, Locale.CMD_REWARDS_DISABLED.getMessage());
        }
    }
}
