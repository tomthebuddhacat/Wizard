package io.github.tomthebuddhacat.paper.service;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import io.github.tomthebuddhacat.common.record.RankRecord;
import io.github.tomthebuddhacat.common.service.ConfigService;
import io.github.tomthebuddhacat.common.service.LogService;
import io.github.tomthebuddhacat.common.service.RankService;
import io.github.tomthebuddhacat.paper.Wizard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TabListService {

    private final Wizard plugin;

    private final Supplier<RankService> rankService;

    private ConfigService configService;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public TabListService(Wizard plugin, Supplier<RankService> rankService, ConfigService configService) {
        this.plugin = plugin;
        this.rankService = rankService;
        this.configService = configService;
    }

    public void createConfigurationFile() {

        try {
            configService = new ConfigService("tab.yml", plugin);
        } catch (IOException ioException) {
            LogService.error("failed to create tab configuration file.", ioException);
        }
    }

    public void loadConfiguration() {

        CommentedConfigurationNode tabNode = configService.getCommentedConfigurationNode().node("tab");

        Map<String, Object> defaultValues = new HashMap<>();

        defaultValues.put("tabHeader", "LocalHost");
        defaultValues.put("tabFooter", "<yellow><bold>LocalHost</bold></yellow>");

        for (Map.Entry<String, Object> entry : defaultValues.entrySet()) {

            CommentedConfigurationNode node = tabNode.node(entry.getKey());

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

    public void createFooterAndHeader(Player player) {

        CommentedConfigurationNode tabNode = configService.getCommentedConfigurationNode().node("tab");

        String tabHeader = tabNode.node("tabHeader").getString();

        String tabFooter = tabNode.node("tabFooter").getString();

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter
                = new WrapperPlayServerPlayerListHeaderAndFooter(Component.text(tabHeader), Component.text(tabFooter));

        PacketEvents.getAPI()
                .getPlayerManager()
                .getUser(player)
                .sendPacket(headerAndFooter);
    }

    public void updatePlayerDisplayTab(Player player) {

        UserProfile userProfile = PacketEvents.getAPI().getPlayerManager().getUser(player).getProfile();

        RankRecord rankRecord = rankService.get().getPlayerRank(player.getUniqueId());

        Component rankPrefix = rankRecord != null ? rankRecord.rankPrefix() : Component.empty();

        Component tabFormat = rankRecord != null ? rankPrefix
                .append(Component.space())
                .append(Component.text(player.getName()))
                .color(rankRecord.tabNameColor().color())
                : Component.text(player.getName());

        WrapperPlayServerPlayerInfoUpdate.PlayerInfo playerInfoUpdate
                = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                userProfile,
                true,
                player.getPing(),
                GameMode.valueOf(player.getGameMode().name()),
                tabFormat,
                null);

        WrapperPlayServerPlayerInfoUpdate wrapper
                = new WrapperPlayServerPlayerInfoUpdate(
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME,
                List.of(playerInfoUpdate)
        );

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            PacketEvents.getAPI()
                    .getPlayerManager()
                    .getUser(onlinePlayers)
                    .sendPacket(wrapper);
        }
    }
}


