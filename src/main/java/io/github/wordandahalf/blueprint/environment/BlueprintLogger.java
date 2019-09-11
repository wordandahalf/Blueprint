package io.github.wordandahalf.blueprint.environment;

import io.github.wordandahalf.blueprint.Blueprints;

import java.io.OutputStream;
import java.util.logging.*;

public class BlueprintLogger {
    private static class BlueprintLogFormatter extends Formatter {
        public String format(LogRecord record) {
            return "[" + record.getLevel().getName() + "] " + record.getMessage() + "\n";
        }
    }

    public static class BlueprintLogHandler extends ConsoleHandler {
        public BlueprintLogHandler(OutputStream outputStream) {
            super();
            super.setOutputStream(outputStream);
        }
    }

    private static Logger logger = Logger.getLogger(BlueprintLogger.class.getName());

    static {
        LogManager.getLogManager().reset();

        BlueprintLogHandler handler = new BlueprintLogHandler(System.out);
        handler.setFormatter(new BlueprintLogFormatter());

        if(Blueprints.DEBUG_MODE) {
            handler.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
        } else {
            handler.setLevel(Level.INFO);
            logger.setLevel(Level.INFO);
        }

        logger.addHandler(handler);
    }

    public static void finest(Class clazz, String message) {
        log(Level.FINEST, clazz, message);
    }

    public static void finer(Class clazz, String message) {
        log(Level.FINER, clazz, message);
    }

    public static void fine(Class clazz, String message) {
        log(Level.FINE, clazz, message);
    }

    public static void config(Class clazz, String message) {
        log(Level.CONFIG, clazz, message);
    }

    public static void info(Class clazz, String message) {
        log(Level.INFO, clazz, message);
    }

    public static void warn(Class clazz, String message) {
        log(Level.WARNING, clazz, message);
    }

    public static void severe(Class clazz, String message) {
        log(Level.SEVERE, clazz, message);
    }

    public static void log(Level level, Class clazz, String message) {
        log(level, "[" + clazz.getSimpleName() + "] " + message);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}