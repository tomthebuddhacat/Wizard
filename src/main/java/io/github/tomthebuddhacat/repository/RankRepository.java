package io.github.tomthebuddhacat.repository;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.record.RankRecord;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.LogService;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RankRepository {

    private final DatabaseService databaseService;

    private final Wizard plugin;

    public RankRepository(DatabaseService databaseService, Wizard plugin) {
        this.databaseService = databaseService;
        this.plugin = plugin;
    }

    public void createRankTable() {

        String databaseName = plugin.getConfig().getString("mysqlDatabase.databaseName");

        String statement = """
        CREATE TABLE IF NOT EXISTS `%s`.ranks(
        rank_id BIGINT AUTO_INCREMENT PRIMARY KEY,
        rank_name VARCHAR(36) NOT NULL UNIQUE,
        rank_prefix VARCHAR(36),
        rank_chat_color VARCHAR(36),
        tab_player_name_color VARCHAR(36),
        rank_weight BIGINT NOT NULL DEFAULT 0,
        rank_given_timestamp TIMESTAMP,
        rank_expiry_timestamp TIMESTAMP
        )
        """.replace("%s", databaseName);

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create ranks table.", sqlException);
        }
    }

    public void createRank(String rankName, String rankPrefix, String tabNameColor, String chatColor, int rankWeight) {

        String statement = """
        INSERT INTO ranks(
        rank_name,
        rank_prefix,
        rank_chat_color,
        tab_player_name_color,
        rank_weight
        ) VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.setString(1, rankName);
            preparedStatement.setString(2, rankPrefix);
            preparedStatement.setString(3, tabNameColor);
            preparedStatement.setString(4, chatColor);
            preparedStatement.setInt(5, rankWeight);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create a rank.", sqlException);
        }
    }

    public Map<String, RankRecord> getAllRanks() {

        String statement = """
        SELECT rank_name, rank_prefix, tab_player_name_color, rank_chat_color, rank_weight
        FROM ranks
        """;

        Map<String, RankRecord> mapOfRanks = new HashMap<>();

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    mapOfRanks.put(
                            resultSet.getString("rank_name"),
                            new RankRecord(
                                    resultSet.getString("rank_name"),
                                    MiniMessage.miniMessage().deserialize(resultSet.getString("rank_prefix")),
                                    MiniMessage.miniMessage().deserialize(resultSet.getString("rank_chat_color")),
                                    MiniMessage.miniMessage().deserialize(resultSet.getString("tab_player_name_color")),
                                    resultSet.getInt("rank_weight")
                    ));
                }
            }
        } catch (SQLException sqlException) {
        LogService.error("Failed to retrieve all ranks.", sqlException);
        }

        return mapOfRanks;
    }
}
