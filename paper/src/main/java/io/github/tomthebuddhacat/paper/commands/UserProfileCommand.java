package io.github.tomthebuddhacat.paper.commands;

import io.github.tomthebuddhacat.common.command.BaseCommand;
import io.github.tomthebuddhacat.common.command.CommandParameters;
import io.github.tomthebuddhacat.common.command.CommandSource;
import io.github.tomthebuddhacat.common.repository.PlayerRepository;
import io.github.tomthebuddhacat.common.service.ItemService;
import io.github.tomthebuddhacat.common.service.RankService;
import io.github.tomthebuddhacat.paper.Wizard;
import io.github.tomthebuddhacat.paper.gui.GUIManager;
import io.github.tomthebuddhacat.paper.gui.impl.UserProfileGUI;
import org.bukkit.entity.Player;

@CommandParameters(getCommandDescription = "Display information related to your account.", getCommandAliases = "info",  getCommandUsage = "/userprofile", getCommandPowerLevel = 0)
public class UserProfileCommand extends BaseCommand {

    private final GUIManager guiManager;

    private final RankService rankService;

    private final PlayerRepository playerRepository;

    private final ItemService itemService;

    private final Wizard plugin;

    public UserProfileCommand(GUIManager guiManager, RankService rankService, PlayerRepository playerRepository, ItemService itemService, Wizard plugin) {
        this.guiManager = guiManager;
        this.rankService = rankService;
        this.playerRepository = playerRepository;
        this.itemService = itemService;
        this.plugin = plugin;
    }

    @Override
    public void run(CommandSource commandSource, String[] args) {

        Player player = commandSource.getPlayer();

        guiManager.openGUI(new UserProfileGUI(playerRepository, itemService, rankService, plugin), player);
    }
}
