package io.github.tomthebuddhacat.command;

public class CommandPermissionException extends RuntimeException {

    public CommandPermissionException() {

        super("You don't have the required permission to use this command.");
    }
}
