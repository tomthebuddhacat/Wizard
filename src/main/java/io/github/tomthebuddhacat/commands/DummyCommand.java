package io.github.tomthebuddhacat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.gui.GUIManager;
import io.github.tomthebuddhacat.gui.impl.TestReportGUI;
import io.github.tomthebuddhacat.record.PlayerRecord;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.ItemService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@CommandAlias("dummy")
public class DummyCommand extends BaseCommand {

    private final ReportRepository reportRepository;

    private final PlayerRepository playerRepository;

    private final ItemService itemService;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final GUIManager guiManager;

    private final Wizard plugin;

    @Default
    public void onDefault(Player player) {

        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy create report -</red> <white>Create a report against a player.</white>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy delete report -</red> <white>Delete an existing report.</white>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy gui report -</red> <white>Display's all active player reports.</white>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy player information -</red> <white>Retrieve information about a player.</white>"));
        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));

    }

    @Subcommand("create report")
    @CommandPermission("wizard.command.dummy")
    public void onReportCreate(Player player, String reportedPlayerName, String reportReason) {

        boolean success = reportRepository.createDummyReportData(player.getName(), reportedPlayerName, reportReason);

        if (success) {
            player.sendMessage(miniMessage.deserialize("<green>You have successfully reported</green> <white>" + reportedPlayerName + "</white> <green>for</green> <white>" + reportReason + ".</white>"));
        } else {
            player.sendMessage(miniMessage.deserialize("<red>Failed to create a report against</red> <white>" + reportedPlayerName + ".</white>"));
        }
    }

    @Subcommand("delete report")
    @CommandPermission("wizard.command.dummy")
    public void onReportDelete(Player player, int reportId) {

        boolean success = reportRepository.deleteDummyReport(reportId);

        if (success) {
            player.sendMessage(miniMessage.deserialize("<green>Player report with the ID </green> <white>" + reportId + "</white> <green>has been deleted successfully.</green>"));
        } else {
            player.sendMessage(miniMessage.deserialize("<red>Failed to delete a report with the ID</red> <white>" + reportId + ".</white>"));
        }
    }

    @Subcommand("gui report")
    @CommandPermission("wizard.command.dummy")
    public void onReportGUI(Player player){

        guiManager.openGUI(new TestReportGUI(reportRepository, itemService, plugin), player);
    }

    @Subcommand("player information")
    @CommandPermission("wizard.command.dummy")
    public void onPlayerInformation(Player player, String playerName) {

        List<PlayerRecord> playerRecord = playerRepository.getAllInformation();

        for (PlayerRecord listOfPlayerRecords : playerRecord) {
            player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
            player.sendMessage(miniMessage.deserialize("<red>Player Name: </red> <white>" + playerName + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Player UUID: </red> <white>" + listOfPlayerRecords.playerUUID() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Player Rank: </red> <white>" + listOfPlayerRecords.rankName() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>First Login: </red> <white>" + listOfPlayerRecords.firstLoginTimeStamp() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Last Login: </red> <white>" + listOfPlayerRecords.lastLoginTimeStamp() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
        }
    }
}
