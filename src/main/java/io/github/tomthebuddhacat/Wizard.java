package io.github.tomthebuddhacat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import io.github.tomthebuddhacat.commands.DummyCommand;
import io.github.tomthebuddhacat.gui.GUIListener;
import io.github.tomthebuddhacat.gui.GUIManager;
import io.github.tomthebuddhacat.listener.PlayerEventListener;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.DatabaseService;
import io.github.tomthebuddhacat.service.ItemService;
import io.github.tomthebuddhacat.service.LogService;
import io.github.tomthebuddhacat.service.RankService;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Wizard extends JavaPlugin {

    private DatabaseService databaseService;

    private ReportRepository reportRepository;

    private PlayerRepository playerRepository;

    private PaperCommandManager commandManager;

    private GUIManager guiManager;

    private GUIListener guiListener;

    private PlayerEventListener playerEventListener;

    private ItemService itemService;

    private RankService rankService;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        establishDatabaseServiceConnection();

        createDatabase();

        establishDatabaseConnection();

        registerAllRepositories();

        establishService();

        registerAllListeners();

        registerCommands();

        LogService.info("Successfully enabled the plugin.");
    }

    @Override
    public void onDisable() {

        databaseService.disbandConnection();

        LogService.info("Successfully disabled the plugin.");
    }

    private void establishDatabaseServiceConnection() {

        databaseService = new DatabaseService();

        try {

            databaseService.setupConnection(
                    getConfig().getString("mysqlDatabase.hostAddress"),
                    getConfig().getInt("mysqlDatabase.hostPort"),
                    getConfig().getString("mysqlDatabase.userName"),
                    getConfig().getString("mysqlDatabase.hostPassword")
            );

        } catch (SQLException sqlException) {
            LogService.error("Failed to establish connection to the database service.", sqlException);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private void createDatabase() {

        try {
            databaseService.createDatabase(this.getConfig().getString("mysqlDatabase.databaseName"));
        } catch (SQLException sqlException) {
            LogService.error("Failed to create database.", sqlException);
        }
    }

    private void establishDatabaseConnection() {

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
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private void establishService() {

        itemService = new ItemService();

        rankService = new RankService();
        rankService.registerRanks();
    }

    private void registerCommands() {

        commandManager = new PaperCommandManager(this);

        List<BaseCommand> commands = new ArrayList<>();

        commands.add(new DummyCommand(reportRepository, playerRepository, itemService, rankService, guiManager, this));

        int registeredCommands = 0;

        for (BaseCommand baseCommand : commands) {
            commandManager.registerCommand(baseCommand);
            registeredCommands++;
        }

        LogService.info("A total number of " + registeredCommands + " commands have been registered.");
    }

    private void registerAllRepositories() {

        reportRepository = new ReportRepository(databaseService, this);
        reportRepository.createTable();

        playerRepository = new PlayerRepository(databaseService, this);
        playerRepository.createPlayerTable();
    }

    private void registerAllListeners() {

        guiManager = new GUIManager();
        guiListener = new GUIListener(guiManager);

        playerEventListener = new PlayerEventListener(playerRepository, rankService);

        List<Listener> listOfListeners = new ArrayList<>();

        listOfListeners.add(guiListener);
        listOfListeners.add(playerEventListener);

        int registeredListeners = 0;

        for (Listener listener : listOfListeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
            registeredListeners++;
        }

        LogService.info("A total number of " + registeredListeners + " listeners have been registered.");
    }
}
