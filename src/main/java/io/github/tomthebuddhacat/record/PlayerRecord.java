package io.github.tomthebuddhacat.record;

import io.github.tomthebuddhacat.player.NetworkPlayer;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayerRecord(NetworkPlayer networkPlayer, String rankName, UUID playerUUID, int playerPunishmentPoints, Timestamp firstLoginTimeStamp, Timestamp lastLoginTimeStamp) { }
