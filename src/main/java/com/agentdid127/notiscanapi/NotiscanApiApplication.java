package com.agentdid127.notiscanapi;

import com.erliapp.utilities.Snowflake;
import com.erliapp.utilities.database.Database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main Application
 */
@SpringBootApplication
public class NotiscanApiApplication {

    // Tokens
    public static Map<String,Long> tokens = new LinkedHashMap<>(); // Token, User ID

    // Sessions
    public static Map<Long,Long> sessions = new LinkedHashMap<>();

    // Database connection
    public static Database DATABASE;

    // GSON for json
    public static Gson GSON = new GsonBuilder().create();

    // Snowflake IDs
    public static Snowflake SNOWFLAKE;

    /**
     * Main method
     * @param args command line args
     * @throws IOException database issues
     * @throws ClassNotFoundException database concerns
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Load the config file, and save default if not found
        loadConfig();

        // Create snowflakes (the number is the timestamp for 2/5/2026 at midnight UTC.
        SNOWFLAKE = new Snowflake(1770267600000L);

        // Start API.
        SpringApplication.run(NotiscanApiApplication.class, args);
    }

    /**
     * Loads a default config if not found, or the config file.
     * @throws IOException When we can't access the file
     * @throws ClassNotFoundException Failing to load database
     */
    public static void loadConfig() throws IOException, ClassNotFoundException {
        File configFile = new File("./config.yml");
        if (!configFile.exists()) {
            InputStream is = NotiscanApiApplication.class.getResourceAsStream("/config.yml");
            FileOutputStream fos = new FileOutputStream(configFile);
            assert is != null;
            fos.write(is.readAllBytes());
            is.close();
            fos.close();
        }

        // Set up database
        DATABASE = new DatabaseManager(configFile.toPath().toAbsolutePath().getParent().toString(), configFile.getName()).getDatabase();

    }

}
