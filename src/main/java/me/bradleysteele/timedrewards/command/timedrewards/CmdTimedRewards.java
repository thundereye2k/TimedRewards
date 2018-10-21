package me.bradleysteele.timedrewards.command.timedrewards;

import me.bradleysteele.commons.register.command.BCommand;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.timedrewards.resource.yml.Locale;
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
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Players.sendMessage(sender, Locale.CMD_UNKNOWN.getMessage());
    }
}