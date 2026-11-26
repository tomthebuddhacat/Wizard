package io.github.tomthebuddhacat.command;

public class CommandFailException extends RuntimeException {

    public CommandFailException(String exceptionMessage) {

        super(exceptionMessage);
    }
}
