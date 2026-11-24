package io.github.tomthebuddhacat.service;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class LogService {

    private static final String PREFIX = "[Wizard]";

    public void info(String message) {
        log.info(PREFIX + " {}", message);
    }

    public void debug(String message) {
        log.debug(PREFIX + " {}", message);
    }

    public void warning(String message) {
        log.warn(PREFIX + " {}", message);
    }

    public void error(String message) {
        log.error(PREFIX + " {}", message);
    }

    public void error(String message, Throwable throwable) {
        log.error(PREFIX + " {}", message, throwable);
    }
}
