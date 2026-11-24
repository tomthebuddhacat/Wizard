package io.github.tomthebuddhacat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import io.github.tomthebuddhacat.gui.TestGUI;
import io.github.tomthebuddhacat.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("dummy")
public class DummyCommand extends BaseCommand {

    private final ReportRepository reportRepository;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final TestGUI testGUI;

    @Default
    public void onDefault(Player player) {

        player.sendMessage(miniMessage.deserialize("<red><bold>-----------------------------------------------------</bold></red>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy create report -</red> <white>Create a report against a player.</red>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy delete report -</red> <white>Delete an existing report.</red>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy gui report -</red> <white>Display's all active player reports.</red>"));
        player.sendMessage(miniMessage.deserialize("<red><bold>-----------------------------------------------------</bold></red>"));

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

        testGUI.openInventory(player);
    }
}
