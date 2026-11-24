package io.github.tomthebuddhacat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import io.github.tomthebuddhacat.commands.DummyCommand;
import io.github.tomthebuddhacat.gui.TestGUI;
import io.github.tomthebuddhacat.repository.DatabaseRepository;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.LogService;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Wizard extends JavaPlugin {

    private DatabaseService databaseService;

    private DatabaseRepository databaseRepository;

    private ReportRepository reportRepository;

    private PaperCommandManager commandManager;

    private TestGUI testGUI;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        establishDatabaseConnection();

        establishDatabase();

        establishReportRepository();

        registerListener();

        registerCommands();

        LogService.info("Successfully enabled the plugin.");
    }

    @Override
    public void onDisable() {

        databaseService.disbandConnection();

        LogService.info("Successfully disabled the plugin.");
    }

    private void establishDatabaseConnection() {

        databaseService = new DatabaseService();

        try {
            databaseService.setupConnection(
                    getConfig().getString("mysqlDatabase.hostAddress"),
                    getConfig().getInt("mysqlDatabase.hostPort"),
                    getConfig().getString("mysqlDatabase.databaseName"),
                    getConfig().getString("mysqlDatabase.userName"),
                    getConfig().getString("mysqlDatabase.hostPassword")
            );
        } catch (SQLException sqlException) {
            LogService.error("Failed to establish connection to the database.", sqlException);
        }
    }

    private void establishDatabase() {

        databaseRepository = new DatabaseRepository(this, databaseService);

        databaseRepository.createDatabase();
    }

    private void registerCommands() {

        commandManager = new PaperCommandManager(this);

        List<BaseCommand> commands = new ArrayList<>();

        commands.add(new DummyCommand(reportRepository, testGUI));

        int registeredCommands = 0;

        for (BaseCommand baseCommand : commands) {
            commandManager.registerCommand(baseCommand);
            registeredCommands++;
        }

        LogService.info("A total number of " + registeredCommands + " commands have been registered.");
    }

    private void establishReportRepository() {

        reportRepository = new ReportRepository(databaseService, this);

        reportRepository.createTable();
    }

    private void registerListener() {

        testGUI = new TestGUI(reportRepository);

        getServer().getPluginManager().registerEvents(testGUI, this);
    }
}
