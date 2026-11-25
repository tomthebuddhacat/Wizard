package io.github.tomthebuddhacat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.tomthebuddhacat.player.NetworkPlayer;
import io.github.tomthebuddhacat.player.OfflineNetworkPlayer;
import io.github.tomthebuddhacat.record.PlayerRecord;
import io.github.tomthebuddhacat.record.RankRecord;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.repository.RankRepository;
import io.github.tomthebuddhacat.service.LogService;
import io.github.tomthebuddhacat.service.RankService;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@CommandAlias("dummy")
public class DummyCommand extends BaseCommand {

    private final PlayerRepository playerRepository;

    private final RankRepository rankRepository;

    private final RankService rankService;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Default
    public void onDefault(Player player) {

        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
        player.sendMessage(miniMessage.deserialize("<red>/dummy playerInfo -</red> <white>Retrieve information about a player.</white>"));
        player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));

    }

    @Subcommand("playerInfo")
    @CommandPermission("wizard.command.dummy")
    @Syntax("<targetPlayer>")
    public void onPlayerInformation(
            Player player,
            @Single Player targetPlayer) {

        List<PlayerRecord> playerRecord = playerRepository.getAllInformation();

        for (PlayerRecord listOfPlayerRecords : playerRecord) {
            player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
            player.sendMessage(miniMessage.deserialize("<red>Player Name: </red> <white>" + targetPlayer.getName() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Player UUID: </red> <white>" + listOfPlayerRecords.playerUUID() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Player Rank: </red> <white>" + listOfPlayerRecords.rankName() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Punishment Points: </red> <white>" + listOfPlayerRecords.playerPunishmentPoints() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>First Login: </red> <white>" + listOfPlayerRecords.firstLoginTimeStamp() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red>Last Login: </red> <white>" + listOfPlayerRecords.lastLoginTimeStamp() + "</white>"));
            player.sendMessage(miniMessage.deserialize("<red><bold>---------------------------------</bold></red>"));
        }
    }

    @Subcommand("create rank")
    @CommandPermission("wizard.command.dummy")
    @Syntax("<rankName>")
    public void onRankCreate(
            Player player,
            String rankName,
            String rankPrefix,
            String tabNameColor,
            String rankColor,
            int rankWeight) {

        rankRepository.createRank(rankName, rankPrefix, tabNameColor, rankColor, rankWeight);

        player.sendMessage(miniMessage.deserialize("<green>You have successfully created a rank!</green>"));
    }

    @Subcommand("grant rank")
    @CommandPermission("wizard.command.dummy")
    @Syntax("<targetPlayer> <rankName>")
    public void onRankGrant(
            Player player,
            NetworkPlayer targetPlayer,
            String rankName) {

        RankRecord rankRecord = rankService.getRankName(rankName);

        playerRepository.setPlayerRank(rankRecord.rankName(), targetPlayer);

        player.sendMessage(miniMessage.deserialize("<green>You have successfully updated</green> <yellow>" + targetPlayer.getPlayerName() + "'s<yellow> <green>rank to</green> <yellow>" + rankRecord.rankName() + ".</yellow>"));
    }
}
