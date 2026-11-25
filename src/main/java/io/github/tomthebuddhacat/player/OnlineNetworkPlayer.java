package io.github.tomthebuddhacat.player;

import org.bukkit.entity.Player;

import java.util.UUID;

public class OnlineNetworkPlayer extends NetworkPlayer {

    private final Player player;

    public OnlineNetworkPlayer(Player player) {
        super(player.getName(), player.getUniqueId());
        this.player = player;
    }

    @Override
    public String getPlayerName() {
        return super.getPlayerName();
    }

    @Override
    public UUID getPlayerUUID() {
        return super.getPlayerUUID();
    }

    public Player getPlayer() {
        return player;
    }
}
