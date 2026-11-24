package io.github.tomthebuddhacat.repository;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.record.ReportRecord;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.LogService;

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

    public void createTable() {

        String databaseName = plugin.getConfig().getString("mysqlDatabase.databaseName");

        String statement = """
        CREATE TABLE IF NOT EXISTS `%s`.playerReports(
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            player_name VARCHAR(36) NOT NULL,
            reported_player_name VARCHAR(36) NOT NULL,
            reported_reason VARCHAR(15) NOT NULL,
            report_time TIMESTAMP,
            report_status VARCHAR(10),
            reviewed_by_player_name VARCHAR(36),
            reviewed_time TIMESTAMP
        )
        """.replace("%s", databaseName);

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create reports table.", sqlException);
        }
    }

    public boolean createDummyReportData(
            String playerName,
            String reportedPlayerName,
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

            preparedStatement.setString(1, playerName);
            preparedStatement.setString(2, reportedPlayerName);
            preparedStatement.setString(3, reportedReason);
            preparedStatement.setTimestamp(4, Timestamp.from(Instant.now()));
            preparedStatement.setString(5, "PENDING");

            preparedStatement.executeUpdate();
            return true;

        } catch (SQLException sqlException) {
            LogService.error("Failed to create dummy report data.", sqlException);
            return false;
        }
    }

    public boolean deleteDummyReport(int id) {

        String statement = """
        DELETE FROM playerReports WHERE id = ?
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
        SELECT player_name, reported_player_name, report_time 
        FROM playerReports
        ORDER BY id DESC
        """;

        List<ReportRecord> playerReports = new ArrayList<>();

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    playerReports.add(new ReportRecord(
                            resultSet.getString("player_name"),
                            resultSet.getString("reported_player_name"),
                            resultSet.getTimestamp("report_time")
                    ));
                }
            }
        } catch (SQLException sqlException) {
            LogService.error("Failed to retrieve reported player name.", sqlException);
        }
        return playerReports;
    }
}
