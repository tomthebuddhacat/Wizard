package io.github.tomthebuddhacat.player;

import java.util.UUID;

public abstract class NetworkPlayer {

    private final String playerName;

    private final UUID playerUUID;

    public NetworkPlayer(String playerName, UUID playerUUID) {
        this.playerName = playerName;
        this.playerUUID = playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
