package io.github.tomthebuddhacat.command.commands;

import io.github.tomthebuddhacat.command.BaseCommand;
import io.github.tomthebuddhacat.command.CommandParameters;
import io.github.tomthebuddhacat.command.CommandSource;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

@CommandParameters(getCommandDescription = "A test command.", getCommandAliases = "t",  getCommandUsage = "/test", getCommandPowerLevel = 0)
public class TestCommand extends BaseCommand {

    @Override
    public void run(CommandSource commandSource, String[] args) {

        Player player = commandSource.getPlayer();

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>The plugin seems to be working just fine.</green>"));
    }
}
