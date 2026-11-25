package io.github.tomthebuddhacat.record;

import io.github.tomthebuddhacat.player.NetworkPlayer;

import java.sql.Timestamp;

public record ReportRecord(int reportId, NetworkPlayer playerName, NetworkPlayer reportedPlayerName, Timestamp reportedTimeStamp, String reportReason) {}
