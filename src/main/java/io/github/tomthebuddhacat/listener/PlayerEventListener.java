package io.github.tomthebuddhacat.listener;

import io.github.tomthebuddhacat.repository.PlayerRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.time.Instant;

public class PlayerEventListener implements Listener {

    private final PlayerRepository playerRepository;

    public PlayerEventListener(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {

        Player player = playerJoinEvent.getPlayer();

        playerRepository.createPlayer(player, Timestamp.from(Instant.now()));
    }
}
