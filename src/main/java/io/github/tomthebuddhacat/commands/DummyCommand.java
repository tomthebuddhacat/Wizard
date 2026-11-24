package io.github.tomthebuddhacat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.gui.GUIManager;
import io.github.tomthebuddhacat.gui.impl.test.TestPlayerReportGUI;
import io.github.tomthebuddhacat.gui.impl.test.TestReportGUI;
import io.github.tomthebuddhacat.record.PlayerRecord;
import io.github.tomthebuddhacat.record.ReportSession;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.ItemService;
import io.github.tomthebuddhacat.service.RankService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@CommandAlias("dummy")
public class DummyCommand extends BaseCommand {

    private final ReportRepository reportRepository;

    private final PlayerRepository playerRepository;

    private final ItemService itemService;

    private final RankService rankService;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final GUIManager guiManager;

    private final Wizard plugin;

    @Default
    public void onDefault(Player player) {

        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy gui report -</red> <white>Display's all active player reports.</white>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy player information -</red> <white>Retrieve information about a player.</white>"));
        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));

    }

    @Subcommand("gui report")
    @CommandPermission("wizard.command.dummy")
    public void onGUI(Player player){

        guiManager.openGUI(new TestReportGUI(reportRepository, itemService, plugin), player);
    }

    @Subcommand("gui player report")
    @CommandPermission("wizard.command.dummy")
    public void onReportGUI(
            Player player,
            @Name("targetPlayer") Player targetPlayer) {

        ReportSession reportSession = new ReportSession(targetPlayer);

        guiManager.openGUI(new TestPlayerReportGUI(reportRepository, itemService, reportSession, plugin), player);
    }


    @Subcommand("player information")
    @CommandPermission("wizard.command.dummy")
    public void onPlayerInformation(
            Player player,
            @Name("targetPlayer") String playerName) {

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

    @Subcommand("grant rank")
    @CommandPermission("wizard.command.dummy")
    public void onRankGrant(
            CommandIssuer commandIssuer,
            @Name("targetPlayer") Player targetPlayer,
            @Name("rankName") String rankName) {

        playerRepository.updatePlayerRank(rankName, targetPlayer.getUniqueId());

    }
}
