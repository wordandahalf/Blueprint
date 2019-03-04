package io.github.wordandahalf.blueprint.logging;

import io.github.wordandahalf.blueprint.Blueprints;

import java.util.logging.*;

public class BlueprintLogger {
    private static class BlueprintLogFormatter extends Formatter {
        public String format(LogRecord record) {
            return "[" + record.getLevel().getName() + "] " + record.getMessage() + "\n";
        }
    }

    private static Logger logger = Logger.getLogger(BlueprintLogger.class.getName());

    static {
        logger.setUseParentHandlers(false);

        StreamHandler handler = new StreamHandler(System.out, new BlueprintLogFormatter());

        if(Blueprints.DEBUG) {
            handler.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } else {
            handler.setLevel(Level.INFO);
            logger.setLevel(Level.INFO);
        }

        logger.addHandler(handler);
    }

    public static void log(Level level, Class clazz, String message) {
        log(level, "[" + clazz.getSimpleName() + "] " + message);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
