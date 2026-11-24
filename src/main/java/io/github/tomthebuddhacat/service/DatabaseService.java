package io.github.tomthebuddhacat.service;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class DatabaseService {

    private Connection connection;

    public void setupConnection(
            String hostAddress,
            int hostPort,
            String userName,
            String databasePassword
    ) throws SQLException {

        String connectionUrl = "jdbc:mysql://" + hostAddress + ":" + hostPort + "/";

        connection = DriverManager.getConnection(
                connectionUrl,
                userName,
                databasePassword
        );
    }

    public void createDatabase(String databaseName) throws SQLException {

        String statement = """
        CREATE DATABASE IF NOT EXISTS `%s`
        """.replace("%s", databaseName);

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        }

        LogService.info("Successfully created database " + databaseName);
    }

    public void setupConnection(
            String hostAddress,
            int hostPort,
            String databaseName,
            String userName,
            String databasePassword
    ) throws SQLException {

        String connectionUrl = "jdbc:mysql://" + hostAddress + ":" + hostPort + "/" + databaseName;

        connection = DriverManager.getConnection(
                connectionUrl,
                userName,
                databasePassword
        );

        LogService.info("Successfully established connection to MySQL.");
    }
    public void disbandConnection() {

        if (connection != null) {
            try {
                connection.close();
                LogService.info("Successfully disbanded connection from MySQL database.");
            } catch (SQLException sqlException) {
                LogService.error("Failed to disband connection from MYSQL database.", sqlException);
            }
        }
    }
}
