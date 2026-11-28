package io.github.tomthebuddhacat.paper.commands;

import io.github.tomthebuddhacat.common.command.*;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

@CommandParameters(getCommandDescription = "Update your player's game-mode.", getCommandAliases = "gm",  getCommandUsage = "/gamemode", getCommandPowerLevel = 900)
public class GameModeCommand extends BaseCommand {

    @Override
    public void run(CommandSource commandSource, String[] args) {

        Player player = commandSource.getPlayer();

        if (args.length != 1) {
            throw new CommandArgumentException();
        }

        if (commandSource instanceof ConsoleCommandSender) {
            throw new CommandFailException("Only players can execute this command.");
        }

        GameMode gameModeName = GameMode.valueOf(args[0].toUpperCase(Locale.ROOT));

        player.setGameMode(gameModeName);
    }
}
