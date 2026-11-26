package io.github.tomthebuddhacat.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {

    String getCommandDescription() default "";

    String getCommandUsage() default "/<command>";

    String getCommandAliases() default "";

    int getCommandPowerLevel() default 0;
}
