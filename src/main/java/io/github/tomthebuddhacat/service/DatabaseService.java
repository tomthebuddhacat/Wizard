package io.github.tomthebuddhacat.service;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DatabaseService {

    private Connection connection;

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

        LogService.info("Successfully connected to MySQL database.");
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
