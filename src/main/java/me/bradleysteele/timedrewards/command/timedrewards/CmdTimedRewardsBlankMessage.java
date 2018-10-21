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

import com.google.common.collect.Lists;
import me.bradleysteele.commons.register.command.BCommand;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.timedrewards.resource.yml.Locale;
import me.bradleysteele.timedrewards.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Bradley Steele
 */
public class CmdTimedRewardsBlankMessage extends BCommand {

    public CmdTimedRewardsBlankMessage() {
        this.setAliases("blankmessage", "blankmsg");
        this.setDescription("Sends a message to a player with no tr prefix.");

        this.setAllowConsole(true);
        this.setSync(false);

        this.setPermission(Permissions.TR_BLANKMESSAGE);
        this.setPermissionDenyMessage(Locale.CMD_NO_PERM.getMessage());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            Player player = Players.getPlayer(args[0]);

            if (player == null) {
                Players.sendMessage(sender, Locale.CMD_TR_BLANKMSG_INVALID.getMessage("{player}", args[0]));
                return;
            }

            List<String> words = Lists.newArrayList(args);
            words.remove(0);

            Players.sendMessage(player, Messages.colour(String.join(" ", words)));
        } else {
            Players.sendMessage(sender, Locale.CMD_INVALID.getMessage("{args}", "trs blankmsg <player> <message>"));
        }
    }
}
