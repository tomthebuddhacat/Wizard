package io.github.tomthebuddhacat.common.command;

import io.github.tomthebuddhacat.common.service.LogService;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

public class CommandHandler {

    public void unregisterCommand(String commandName) {

        try {

            SimpleCommandMap commandMap = (SimpleCommandMap) Bukkit
                    .getServer()
                    .getClass()
                    .getMethod("getCommandMap")
                    .invoke(Bukkit.getServer());

            commandMap.getKnownCommands().keySet().removeIf(key -> key.equalsIgnoreCase(commandName)
                    || key.equalsIgnoreCase("minecraft:" + commandName)
                    || key.equalsIgnoreCase("bukkit:" + commandName));

        } catch (Exception exception) {
            LogService.error("Failed to unregister command.", exception);
        }
    }
}
