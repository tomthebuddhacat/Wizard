package io.github.tomthebuddhacat;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.github.tomthebuddhacat.command.CommandManager;
import io.github.tomthebuddhacat.command.commands.TestCommand;
import io.github.tomthebuddhacat.gui.GUIListener;
import io.github.tomthebuddhacat.gui.GUIManager;
import io.github.tomthebuddhacat.listener.PlayerEventListener;
import io.github.tomthebuddhacat.repository.PlayerRepository;
import io.github.tomthebuddhacat.repository.RankRepository;
import io.github.tomthebuddhacat.repository.ReportRepository;
import io.github.tomthebuddhacat.service.*;
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

    private RankRepository rankRepository;

    private CommandManager commandManager;

    private GUIManager guiManager;

    private GUIListener guiListener;

    private PlayerEventListener playerEventListener;

    private ItemService itemService;

    private RankService rankService;

    private TabListService tabListService;

    @Override
    public void onLoad() {

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

    }

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

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getSettings().checkForUpdates(false);

        LogService.info("Successfully enabled the plugin.");
    }

    @Override
    public void onDisable() {

        databaseService.disbandConnection();

        PacketEvents.getAPI().terminate();

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

        rankService = new RankService(rankRepository);
        rankService.registerRanks();

        tabListService = new TabListService(this, rankService);

        commandManager = new CommandManager();
    }

    private void registerCommands() {

        new TestCommand().register(rankService);

        LogService.info("Successfully registered all the commands.");
    }

    private void registerAllRepositories() {

        reportRepository = new ReportRepository(databaseService, this);
        reportRepository.createReportsTable();

        playerRepository = new PlayerRepository(databaseService, this);
        playerRepository.createPlayerTable();

        rankRepository = new RankRepository(databaseService, this);
        rankRepository.createRankTable();
        rankRepository.createDefaultRank();
    }

    private void registerAllListeners() {

        guiManager = new GUIManager();
        guiListener = new GUIListener(guiManager);

        playerEventListener = new PlayerEventListener(playerRepository, rankService, tabListService, this);

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
