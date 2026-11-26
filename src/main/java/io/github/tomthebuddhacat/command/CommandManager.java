package io.github.tomthebuddhacat.command;

import io.github.tomthebuddhacat.service.LogService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandManager {

    private final CommandMap commandMap;

    public CommandManager() {

        CommandMap map = null;

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            map = (CommandMap) field.get(Bukkit.getServer());

        } catch (NoSuchFieldException noSuchFieldException) {
           LogService.error("Failed to register command map.", noSuchFieldException);

        } catch (IllegalAccessException illegalAccessException) {
            LogService.error("Failed to register command map.", illegalAccessException);
        }

        this.commandMap = map;
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }
}
