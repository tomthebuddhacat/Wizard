package io.github.tomthebuddhacat.repository;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.enums.ReportStatus;
import io.github.tomthebuddhacat.player.NetworkPlayer;
import io.github.tomthebuddhacat.player.OfflineNetworkPlayer;
import io.github.tomthebuddhacat.record.ReportRecord;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.LogService;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {

    private final DatabaseService databaseService;

    private final Wizard plugin;

    public ReportRepository(DatabaseService databaseService, Wizard plugin) {
        this.databaseService = databaseService;
        this.plugin = plugin;
    }

    public void createReportsTable() {

        String databaseName = plugin.getConfig().getString("mysqlDatabase.databaseName");

        String statement = """
        CREATE TABLE IF NOT EXISTS `%s`.playerReports(
            report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
            player_name VARCHAR(36) NOT NULL,
            reported_player_name VARCHAR(36) NOT NULL,
            reported_reason VARCHAR(36) NOT NULL,
            report_time TIMESTAMP,
            report_status ENUM(
            'PENDING', 'ACCEPTED', 'REJECTED' 
            ) NOT NULL DEFAULT 'PENDING'
        )
        """.replace("%s", databaseName);

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create reports table.", sqlException);
        }
    }

    public boolean createDummyReportData(
            NetworkPlayer networkPlayer,
            NetworkPlayer reportedPlayerName,
            String reportedReason) {

        String statement = """
        INSERT INTO playerReports(
        player_name,
        reported_player_name,
        reported_reason,
        report_time,
        report_status
        ) VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.setString(1, networkPlayer.getPlayerName());
            preparedStatement.setString(2, reportedPlayerName.getPlayerName());
            preparedStatement.setString(3, reportedReason);
            preparedStatement.setTimestamp(4, Timestamp.from(Instant.now()));
            preparedStatement.setString(5, ReportStatus.PENDING.name());

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException sqlException) {
            LogService.error("Failed to create dummy report data.", sqlException);
            return false;
        }
    }

    public void updatePlayerReportStatusAsReject(int reportId) {

        String statement = """
        UPDATE playerReports
        SET report_status = ?
        WHERE report_id = ?
        """;

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.setString(1, ReportStatus.REJECTED.name());
            preparedStatement.setInt(2, reportId);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to accept player report.", sqlException);
        }
    }

    public void updatePlayerReportStatusAsAccepted(int reportId) {

        String statement = """
        UPDATE playerReports
        SET report_status = ?
        WHERE report_id = ?
        """;

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.setString(1, ReportStatus.ACCEPTED.name());
            preparedStatement.setInt(2, reportId);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to accept player report.", sqlException);
        }
    }

    public boolean deleteDummyReport(int id) {

        String statement = """
        DELETE FROM playerReports 
        WHERE report_id = ?
        """;

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException sqlException) {
            LogService.error("Failed to delete dummy report.", sqlException);
            return false;
        }
    }

    public List<ReportRecord> getAllDummyReports() {

        String statement = """
        SELECT report_id, player_name, reported_player_name, report_time, reported_reason 
        FROM playerReports
        WHERE report_status = 'PENDING'
        """;

        List<ReportRecord> playerReports = new ArrayList<>();

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    playerReports.add(new ReportRecord(
                            resultSet.getInt("report_id"),
                            new OfflineNetworkPlayer(Bukkit.getOfflinePlayer(resultSet.getString("player_name"))),
                            new OfflineNetworkPlayer(Bukkit.getOfflinePlayer(resultSet.getString("reported_player_name"))),
                            resultSet.getTimestamp("report_time"),
                            resultSet.getString("reported_reason")
                    ));
                }
            }
        } catch (SQLException sqlException) {
            LogService.error("Failed to retrieve reported player name.", sqlException);
        }
        return playerReports;
    }
}
