package io.github.tomthebuddhacat.repository;

import io.github.tomthebuddhacat.Wizard;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.LogService;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseRepository {

    private final Wizard plugin;

    private final DatabaseService databaseService;

    public DatabaseRepository(Wizard plugin, DatabaseService databaseService) {
        this.plugin = plugin;
        this.databaseService = databaseService;
    }

    public void createDatabase() {

        String databaseName = plugin.getConfig().getString("mysqlDatabase.databaseName");

        String statement = """
        CREATE DATABASE IF NOT EXIST `%s`
        """.replace("%s", databaseName);

        try (PreparedStatement preparedStatement = databaseService.getConnection().prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LogService.error("Failed to create database.", sqlException);
        }
    }
}
