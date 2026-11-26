package io.github.tomthebuddhacat.command;

import io.github.tomthebuddhacat.service.RankService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    private final String COMMAND_SUFFIX = "Command";

    private CommandParameters commandParameters;

    private String commandName;

    private String commandDescription;

    private String commandUsage;

    private List<String> commandAliases;

    private String commandPermission;

    private int commandPermissionLevel;

    private CommandAdapter commandAdapter;

    private CommandSource commandSource;

    private CommandManager commandManager;

    public BaseCommand() {

        this.commandParameters = this.getClass().getAnnotation(CommandParameters.class);

        this.commandName = this.getClass().getSimpleName().replace(COMMAND_SUFFIX, "").toLowerCase();
        this.commandDescription = this.commandParameters.getCommandDescription();

        this.commandUsage = this.commandParameters.getCommandUsage();
        this.commandAliases = Arrays.asList(this.commandParameters.getCommandAliases().split(","));

        this.commandPermissionLevel = this.commandParameters.getCommandPowerLevel();

        this.commandManager = new CommandManager();
    }

    public abstract void run(CommandSource commandSource, String[] args);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        return null;
    }

    public void register(RankService rankService) {
        this.commandAdapter = new CommandAdapter(this, rankService);

        commandManager.getCommandMap().register("", this.commandAdapter);
    }

    public void sendMessage(String message, CommandSource commandSource) {
        commandSource.sendMessage(message);
    }

    public void sendMessage(String message) {
        sendMessage(message, commandSource);
    }

    public void sendMessage(String message, Player player) {
        player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>" + message + "</gray>"));
    }

    public void checkPermission(int commandPermissionLevel) {

        if (commandSource.getCommandPermissionLevel() < commandPermissionLevel) {
            throw new CommandPermissionException();
        }
    }

    public Player getNonNullPlayer(String playerName) {

        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            throw new PlayerNotFoundException();
        }

        return player;
    }

    private static class CommandAdapter extends Command {

        private final BaseCommand baseCommand;

        private final RankService rankService;

        public CommandAdapter(BaseCommand baseCommand, RankService rankService) {

            super(baseCommand.commandName, baseCommand.commandDescription, baseCommand.commandUsage, baseCommand.commandAliases);

            this.baseCommand = baseCommand;

            this.rankService = rankService;
        }

        @Override
        public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {

            CommandSource source = new CommandSource(commandSender, rankService);

            try {
                if (source.getCommandPermissionLevel() < baseCommand.commandPermissionLevel) {

                    throw new CommandPermissionException();
                }

                baseCommand.commandSource = source;
                baseCommand.run(source, args);
                return true;
            }

            catch (CommandFailException | CommandPermissionException | PlayerNotFoundException playerNotFoundException) {
                commandSender.sendMessage(playerNotFoundException.getMessage());
                return true;
            }

            catch (CommandArgumentException commandArgumentException) {
                return false;
            }

            catch (Exception exception) {
                commandSender.sendMessage("Failed to execute the command: " + exception.getMessage());
                return true;
            }
        }

        @Override
        public List<String> tabComplete(CommandSender commandSender, String alias, String[] args) {

            List<String> list = baseCommand.onTabComplete(commandSender, this, alias, args);

            if (list != null) {
                return list;
            }

            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .toList();
        }
    }
}
