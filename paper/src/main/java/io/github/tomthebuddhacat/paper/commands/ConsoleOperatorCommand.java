package io.github.tomthebuddhacat.paper.commands;

import io.github.tomthebuddhacat.common.command.*;
import io.github.tomthebuddhacat.common.service.RankService;
import io.github.tomthebuddhacat.paper.Wizard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandParameters(getCommandDescription = "Grant a specific player operator permission.", getCommandAliases = "op",  getCommandUsage = "/consoleoperator", getCommandPowerLevel = 1500)
public class ConsoleOperatorCommand extends BaseCommand {

    private final RankService rankService;

    private final Wizard plugin;

    public ConsoleOperatorCommand(RankService rankService, Wizard plugin) {
        this.rankService = rankService;
        this.plugin = plugin;
    }

    @Override
    public void run(CommandSource commandSource, String[] args) {

        if (args.length != 1) {
            throw new CommandArgumentException();
        }

        if (commandSource instanceof Player) {
            throw new CommandFailException("Only console can execute this command.");
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);

        if (targetPlayer.isOp()) {

            targetPlayer.setOp(false);
            commandSource.sendMessage("You have successfully removed operation permission from " + targetPlayer.getName() + ".");

        } else {

            targetPlayer.setOp(true);
            commandSource.sendMessage("You have successfully granted operation permission to " + targetPlayer.getName() + ".");

        }
    }
}


