package com.agentdid127.notiscanapi;

import com.erliapp.utilities.database.Database;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Main Application
 */
@SpringBootApplication
public class NotiscanApiApplication {

    // Database connection
    public static Database DATABASE;

    // GSON for json
    public static Gson GSON = new Gson();

    /**
     * Main method
     * @param args command line args
     * @throws IOException database issues
     * @throws ClassNotFoundException database concerns
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Load the config file, and save default if not found
        // TODO: put this in another function.
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

        // Start API.
        SpringApplication.run(NotiscanApiApplication.class, args);

    }

}
