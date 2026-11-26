package io.github.tomthebuddhacat.record;

import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayerRecord(Player player, String rankName, UUID playerUUID, Timestamp firstLoginTimeStamp, Timestamp lastLoginTimeStamp) { }
