package io.girish;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Config {
    public static String NEO4J_URI;
    public static String NEO4J_USERNAME;
    public static String NEO4J_PASSWORD;

    public static void initConfig(String configFilePath) {
        Path p = Paths.get(configFilePath);
        String configFileParentDir = p.getParent().toString();
        String configFileName = p.getFileName().toString();

        Dotenv dotenv = Dotenv.configure()
                .directory(configFileParentDir)
                .filename(configFileName)
                .load();

        NEO4J_URI = dotenv.get("NEO4J_URI");
        NEO4J_USERNAME = dotenv.get("NEO4J_USERNAME");
        NEO4J_PASSWORD = dotenv.get("NEO4J_PASSWORD");
    }
}
