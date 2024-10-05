package lewan24.profileStaticItem.Loggers;

import lewan24.profileStaticItem.main;
import org.bukkit.Bukkit;

public final class Logger {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    private static final String PLUGIN_NAME = PURPLE + "[" + main.getPlugin(main.class).getName() + "] ";

    public static void info(String message) {
        log(PLUGIN_NAME + BLUE + "[INFO] " + RESET + message);
    }

    public static void success(String message) {
        log(PLUGIN_NAME + GREEN + "[SUCCESS INFO] " + message);
    }

    public static void warning(String message) {
        log(PLUGIN_NAME + YELLOW + "[WARN] " + message);
    }

    public static void error(String message) {
        log(PLUGIN_NAME + RED + "[ERROR] " + message);
    }

    private static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(message + RESET);
    }
}
