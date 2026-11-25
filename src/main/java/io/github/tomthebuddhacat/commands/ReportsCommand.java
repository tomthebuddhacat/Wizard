package io.github.tomthebuddhacat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.gui.GUIManager;
import io.github.tomthebuddhacat.gui.impl.ReportsGUI;
import io.github.tomthebuddhacat.player.NetworkPlayer;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.ItemService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("reports")
public class ReportsCommand extends BaseCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final GUIManager guiManager;

    private final ReportRepository reportRepository;

    private final ItemService itemService;

    private final Wizard plugin;

    @Default
    public void onDefault(Player player) {

        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
        player.sendMessage(miniMessage.deserialize("<red>/report player <targetPlayer> -</red> <white>Report a player for violating the network rules..</white>"));
        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));

    }

    @Subcommand("player")
    @CommandPermission("wizard.command.reports")
    @Syntax("<targetPlayer>")
    public void onReportGUI(
            Player player,
            NetworkPlayer targetPlayer) {

        guiManager.openGUI(new ReportsGUI(reportRepository, itemService, plugin), player);
    }
}