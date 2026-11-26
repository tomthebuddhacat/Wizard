package io.github.tomthebuddhacat.service;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.record.RankRecord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TabListService {

    private final Wizard plugin;

    private final RankService rankService;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public TabListService(Wizard plugin, RankService rankService) {
        this.plugin = plugin;
        this.rankService = rankService;
    }

    public void createFooterAndHeader(Player player) {

        Component tabHeader = miniMessage.deserialize("<aqua>You are playing on </aqua><bold><yellow>LOCALHOST</yellow></bold>");

        Component tabFooter = miniMessage.deserialize("<green>Rank, Boosters & More at </green><bold><red>LOCALHOST</red></bold>");

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter
                = new WrapperPlayServerPlayerListHeaderAndFooter(tabHeader, tabFooter);

        PacketEvents.getAPI().getPlayerManager().getUser(player).sendPacket(headerAndFooter);
    }

    public void updatePlayerDisplayTab(Player player) {

        UserProfile userProfile = PacketEvents.getAPI().getPlayerManager().getUser(player).getProfile();

        RankRecord rankRecord = rankService.getPlayerRank(player.getUniqueId());

        Component rankPrefix = rankRecord != null ? rankRecord.rankPrefix() : Component.empty();

        Component tabFormat = rankRecord != null ? rankPrefix
                .append(Component.space())
                .append(Component.text(player.getName()))
                .color(rankRecord.tabNameColor().color())
                : Component.text(player.getName());

        WrapperPlayServerPlayerInfoUpdate.PlayerInfo playerInfoUpdate
                = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(userProfile, true, player.getPing(), GameMode.valueOf(player.getGameMode().name()), tabFormat, null);

        WrapperPlayServerPlayerInfoUpdate wrapper
                = new WrapperPlayServerPlayerInfoUpdate(
                        WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME,
                List.of(playerInfoUpdate)
        );

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            PacketEvents.getAPI().getPlayerManager().getUser(onlinePlayers).sendPacket(wrapper);
        }
    }
}
