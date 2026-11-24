package io.github.tomthebuddhacat.record;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayerRecord(UUID playerUUID, String rankName, Timestamp firstLoginTimeStamp, Timestamp lastLoginTimeStamp) { }
