package io.github.tomthebuddhacat.record;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public record ReportRecord(int reportId, Player player, OfflinePlayer offlinePlayer, Timestamp reportedTimeStamp, String reportReason) {}
