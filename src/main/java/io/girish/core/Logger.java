package io.girish.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static boolean verbose = true;

    private static String getFormattedTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy@HH:mm:ss");

        return now.format(formatter);
    }

    public static void info(String message) {
        if (!verbose) return;

        System.out.println(Color.CYAN + """
                [%s] %s""".formatted(Logger.getFormattedTime(), message) + Color.RESET);
    }

    public static void taskFinished(String message) {
        if (!verbose) return;

        System.out.println(Color.MAGENTA + """
                [%s] %s""".formatted(Logger.getFormattedTime(), message) + Color.RESET);
    }

    public static void done(String message) {
        System.out.println();
        System.out.println(Color.GREEN + """
                [%s] %s""".formatted(Logger.getFormattedTime(), message) + Color.RESET);
    }

    public static void notice(String message) {
        System.out.println(Color.CYAN + """
                [%s] %s""".formatted(Logger.getFormattedTime(), message) + Color.RESET);
    }

    public static void setVerbose(boolean verbose) {
        Logger.verbose = verbose;
    }
}
