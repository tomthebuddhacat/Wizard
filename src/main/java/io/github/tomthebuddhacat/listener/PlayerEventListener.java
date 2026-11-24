package io.github.tomthebuddhacat.listener;

import io.github.tomthebuddhacat.record.RankRecord;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.service.RankService;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.time.Instant;

public class PlayerEventListener implements Listener {

    private final PlayerRepository playerRepository;

    private final RankService rankService;

    public PlayerEventListener(PlayerRepository playerRepository, RankService rankService) {
        this.playerRepository = playerRepository;
        this.rankService = rankService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

        Player player = playerJoinEvent.getPlayer();

        playerRepository.createPlayer(player, Timestamp.from(Instant.now()));
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncChatEvent chatEvent) {

        Player player = chatEvent.getPlayer();

        RankRecord rankRecord = rankService.getRank(playerRepository.getPlayerRank(player.getUniqueId()));

            chatEvent.renderer((displayPlayer, componentDisplayName, componentMessage, viewerPlayer) ->
                    Component.empty()
                            .append(rankRecord.rankPrefix())
                            .append(Component.space())
                            .append(componentDisplayName)
                            .append(Component.text(": "))
                            .append(componentMessage)
            );
    }
}


