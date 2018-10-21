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
import me.bradleysteele.timedrewards.menu.WorkerRewardMenu;
import me.bradleysteele.timedrewards.resource.Resources;
import me.bradleysteele.timedrewards.resource.yml.Locale;
import me.bradleysteele.timedrewards.util.Permissions;
import org.bukkit.command.CommandSender;

/**
 * @author Bradley Steele
 */
public class CmdTimedRewardsReload extends BCommand {

    CmdTimedRewardsReload() {
        this.setAliases("reload", "rl");

        this.setAllowConsole(true);

        this.setPermission(Permissions.TR_RELOAD);
        this.setPermissionDenyMessage(Locale.CMD_NO_PERM.getMessage());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        long start = System.currentTimeMillis();

        // Reload files
        Resources.get().register();

        // Unregister
        WorkerRewardMenu.get().onUnregister();

        // Re-register
        WorkerRewardMenu.get().onRegister();

        Players.sendMessage(sender, Locale.CMD_TR_RELOAD_COMPLETE.getMessage("{time}", (System.currentTimeMillis() - start)));

    }
}