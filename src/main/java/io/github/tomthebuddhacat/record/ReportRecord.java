package io.github.tomthebuddhacat.record;

import java.sql.Timestamp;

public record ReportRecord(int reportId, String playerName, String reportedPlayerName, Timestamp reportedTimeStamp, String reportReason) {}
