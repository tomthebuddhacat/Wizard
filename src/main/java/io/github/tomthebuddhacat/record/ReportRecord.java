package io.github.tomthebuddhacat.record;

import java.sql.Timestamp;

public record ReportRecord(String playerName, String reportedPlayerName, Timestamp reportedTimeStamp) {}
