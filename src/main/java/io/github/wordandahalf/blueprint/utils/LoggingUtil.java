package io.github.wordandahalf.blueprint.utils;

import io.github.wordandahalf.blueprint.Blueprints;

import java.util.logging.*;

public class LoggingUtil {
    private static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return "[" + record.getLevel().getName() + "] " + record.getMessage() + "\n";
        }
    }

    private static Logger logger = Logger.getLogger(LoggingUtil.class.getName());

    static {
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new CustomFormatter());

        if(Blueprints.DEBUG_ENABLED) {
            handler.setLevel(Level.FINE);
            logger.setLevel(Level.FINE);
        } else {
            handler.setLevel(Level.INFO);
            logger.setLevel(Level.INFO);
        }

        logger.addHandler(handler);
    }

    public static Logger getLogger() { return logger; }
}
