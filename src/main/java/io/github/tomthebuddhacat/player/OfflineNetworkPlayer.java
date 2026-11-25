package io.github.tomthebuddhacat.player;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class OfflineNetworkPlayer extends NetworkPlayer {

    private final OfflinePlayer offlinePlayer;

    public OfflineNetworkPlayer(OfflinePlayer offlinePlayer) {
        super(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        this.offlinePlayer = offlinePlayer;
    }

    @Override
    public UUID getPlayerUUID() {
        return super.getPlayerUUID();
    }

    @Override
    public String getPlayerName() {
        return super.getPlayerName();
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }
}
