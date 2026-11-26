package io.github.tomthebuddhacat.listener;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.record.RankRecord;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.service.RankService;
import io.github.tomthebuddhacat.service.TabListService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.time.Instant;

public class PlayerEventListener implements Listener {

    private final PlayerRepository playerRepository;

    private final RankService rankService;

    private final TabListService tabListService;

    private final Wizard plugin;

    public PlayerEventListener(PlayerRepository playerRepository, RankService rankService, TabListService tabListService, Wizard plugin) {
        this.playerRepository = playerRepository;
        this.rankService = rankService;
        this.tabListService = tabListService;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

        Player player = playerJoinEvent.getPlayer();

        playerRepository.createPlayer(player, Timestamp.from(Instant.now()));

        String rankName = playerRepository.getPlayerRank(player);

        rankService.setPlayerRank(rankName, player.getUniqueId());

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {

            tabListService.createFooterAndHeader(onlinePlayers);
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            tabListService.updatePlayerDisplayTab(player);
        });
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncChatEvent chatEvent) {

        Player player = chatEvent.getPlayer();

        RankRecord rankRecord = rankService.getPlayerRank(player.getUniqueId());

        Component rankPrefix = rankRecord != null ? rankRecord.rankPrefix() : Component.empty();

        Component rankChatColor = rankRecord != null ? rankRecord.chatColor() : Component.empty();

        chatEvent.renderer((source, sourceDisplayName, message, viewer) -> {

            Component displayMessage = rankChatColor.color() != null ? message.color(rankChatColor.color()) : message;

            return Component.empty()
                    .append(rankPrefix)
                    .append(Component.space())
                    .append(sourceDisplayName.color(rankRecord.tabNameColor().color()))
                    .append(Component.text(": "))
                    .append(displayMessage);
        });
    }
}


