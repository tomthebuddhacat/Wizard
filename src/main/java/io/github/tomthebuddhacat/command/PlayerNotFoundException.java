package io.github.tomthebuddhacat.command;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException() {

        super("Unable to find a player with that name.");
    }
}
