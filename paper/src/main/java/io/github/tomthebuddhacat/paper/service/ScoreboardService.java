package io.github.tomthebuddhacat.paper.service;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import io.github.tomthebuddhacat.common.service.ConfigService;
import io.github.tomthebuddhacat.common.service.LogService;
import io.github.tomthebuddhacat.common.service.RankService;
import io.github.tomthebuddhacat.paper.Wizard;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ScoreboardService {

    private final Wizard plugin;

    private ConfigService configService;

    private Supplier<RankService> rankService;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ScoreboardService(Wizard plugin, ConfigService configService, Supplier<RankService> rankService) {
        this.plugin = plugin;
        this.configService = configService;
        this.rankService = rankService;
    }

    public void createConfigurationFile() {

        try {
            configService = new ConfigService("scoreboard.yml", plugin);
        } catch (IOException ioException) {
            LogService.error("failed to create scoreboard configuration file.", ioException);
        }
    }

    public void loadConfiguration() {

        CommentedConfigurationNode scoreboardNode = configService.getCommentedConfigurationNode().node("scoreboard");

        Map<String, Object> defaultValues = new HashMap<>();

        defaultValues.put("scoreboardName", "HYPIXEL");
        defaultValues.put("displayName", "<yellow><bold>HYPIXEL</bold></yellow>");

        defaultValues.put("scoreboardLines", List.of(
                "<gray>Rank:</gray> %playerRankColor%%playerRank%",
                "<yellow><bold>LOCALHOST</bold></yellow>"
        ));

        for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {

            CommentedConfigurationNode node = scoreboardNode.node(entry.getKey());

            try {
                if (node.virtual()) {
                    node.set(entry.getValue());
                }
            } catch (SerializationException serializationException) {
                LogService.error("Failed to set default values for scoreboard configuration.", serializationException);
            }

            configService.save();
        }
    }

    public void createScoreboard(Player player) {

        CommentedConfigurationNode scoreboardNode = configService.getCommentedConfigurationNode().node("scoreboard");

        WrapperPlayServerScoreboardObjective scoreboardObjective
                = new WrapperPlayServerScoreboardObjective(scoreboardNode.node("scoreboardName").getString(), WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE,
                miniMessage.deserialize(scoreboardNode.node("displayName").getString()), WrapperPlayServerScoreboardObjective.RenderType.INTEGER);

        PacketEvents.getAPI().getPlayerManager().getUser(player).sendPacket(scoreboardObjective);

    }

    public void displayScoreboard(Player player) {

        CommentedConfigurationNode scoreboardNode = configService.getCommentedConfigurationNode().node("scoreboard");

        WrapperPlayServerDisplayScoreboard displayScoreboard =
                new WrapperPlayServerDisplayScoreboard(1, scoreboardNode.node("scoreboardName").getString());

        PacketEvents.getAPI().getPlayerManager().getUser(player).sendPacket(displayScoreboard);
    }

    public void updateScoreboard(Player player) {

        CommentedConfigurationNode scoreboardNode = configService.getCommentedConfigurationNode().node("scoreboard");

        try {
            List<String> scoreboardLines = scoreboardNode.node("scoreboardLines").getList(String.class);

            for (int i = 0; i < scoreboardLines.size(); i++) {

                String scoreboardLine = scoreboardLines.get(i);

                scoreboardLine = scoreboardLine.replace("%playerRank%", rankService.get().getPlayerRank(player.getUniqueId()).rankName());

                scoreboardLine = scoreboardLine.replace("%playerRankColor%", miniMessage.serialize(rankService.get().getPlayerRank(player.getUniqueId()).tabNameColor()));

                WrapperPlayServerUpdateScore updateScore =
                        new WrapperPlayServerUpdateScore(
                                "scoreboardLine_" + i,
                                WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM,
                                scoreboardNode.node("scoreboardName").getString(),
                                scoreboardLines.size() - 1,
                                miniMessage.deserialize(scoreboardLine),
                                ScoreFormat.blankScore());


                PacketEvents.getAPI().getPlayerManager().getUser(player).sendPacket(updateScore);
            }
        } catch (SerializationException serializationException) {

        }
    }
}

