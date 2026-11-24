package io.github.tomthebuddhacat.record;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayerRecord(String playerName, String rankName, UUID playerUUID, Timestamp firstLoginTimeStamp, Timestamp lastLoginTimeStamp) { }
