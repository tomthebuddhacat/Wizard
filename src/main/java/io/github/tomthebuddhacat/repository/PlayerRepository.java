package io.github.tomthebuddhacat.repository;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.record.PlayerRecord;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.LogService;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {

    private final DatabaseService databaseService;

    private final Wizard plugin;

    public PlayerRepository(DatabaseService databaseService, Wizard plugin) {
        this.databaseService = databaseService;
        this.plugin = plugin;
    }

    public void createPlayerTable() {

        String databaseName = plugin.getConfig().getString("mysqlDatabase.databaseName");

        String statement = """
        CREATE TABLE IF NOT EXISTS `%s`.playerInformation(
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            player_uuid VARCHAR(36) NOT NULL UNIQUE,
            player_rank VARCHAR(36),
            player_first_join_timestamp TIMESTAMP,
            player_last_login_timestamp TIMESTAMP
        )
        """.replace("%s", databaseName);

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create player information table.", sqlException);
        }
    }

    public void createPlayer(Player player, Timestamp timestamp) {

        String statement = """
        INSERT INTO playerInformation(
        player_uuid, 
        player_first_join_timestamp,
        player_last_login_timestamp
        ) VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE
        player_last_login_timestamp = VALUES(
        player_last_login_timestamp
        )
        """;

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setTimestamp(3, timestamp);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create player information.", sqlException);
        }
    }

    public List<PlayerRecord> getAllInformation() {

        String statement = """
        SELECT player_uuid, player_rank, player_first_join_timestamp, player_last_login_timestamp 
        FROM playerInformation
        ORDER BY id DESC
        """;

        List<PlayerRecord> playerRecord = new ArrayList<>();

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    playerRecord.add(new PlayerRecord(
                            UUID.fromString(resultSet.getString("player_uuid")),
                            resultSet.getString("player_rank"),
                            resultSet.getTimestamp("player_first_join_timestamp"),
                            resultSet.getTimestamp("player_last_login_timestamp")
                    ));
                }
            }
        } catch (SQLException sqlException) {
            LogService.error("Failed to retrieve player information.", sqlException);
        }
        return playerRecord;
    }
}
