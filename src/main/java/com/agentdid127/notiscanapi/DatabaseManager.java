package com.agentdid127.notiscanapi;

import com.erliapp.utilities.database.Database;
import com.erliapp.utilities.database.DatabaseBuilder;
import java.io.IOException;
import java.util.Map;

/**
 * Utility Class to manage Databases with the EarlyApp Utils.
 */
public class DatabaseManager {

    /**
     * Database to work with.
     */
    private Database database;

    /**
     * Constructs a DatabaseManager with a SkyVelocity Config File.
     * @param path Path of Configuration file.
     * @param configFile Configuration file.
     * @throws IOException If the database fails to work.
     * @throws ClassNotFoundException If some utility classes fail to be found.
     */
    public DatabaseManager(String path, String configFile) throws IOException, ClassNotFoundException {
        // Load Yaml Configuration file
        Map<String,Object> configYaml = Util.loadYAMLConfig(path, configFile);

        // Gather Database Configuration
        if (configYaml.containsKey("database") && configYaml.get("database") instanceof Map) {
            Map<String,Object> databaseConfigYaml = (Map<String, Object>) configYaml.get("database");

            DatabaseBuilder builder = new DatabaseBuilder();

            // Database Contact info.
            if (databaseConfigYaml.get("contact") instanceof String contact) {
                builder.setContact(contact);
            } // if

            // Database Port
            if (databaseConfigYaml.get("port") instanceof String port) {
                if (com.erliapp.utilities.Util.isNumeric(port)) {
                    builder.setPort(Integer.parseInt(port));
                } // if
            } // if

            // Database info
            if (databaseConfigYaml.get("database") instanceof String database) {
                builder.setKeyspace(database);
            } // if

            //Authentication
            if (databaseConfigYaml.containsKey("credentials") && databaseConfigYaml.get("credentials") instanceof Map) {
                builder.setUseAuth(true);

                Map<String, Object> credentialDatabaseYaml = (Map<String, Object>) databaseConfigYaml.get(
                        "credentials");

                if (credentialDatabaseYaml.get("user") instanceof String user) {
                    builder.setUser(user);
                } // if
                if (credentialDatabaseYaml.get("password") instanceof String password) {
                    builder.setPassword(password);
                } // if
            } // if

            //Type
            builder.setType("sqlite");
            // builder.addDatabaseValue("{table_name}, {key}, {type}"
            builder.addDatabaseValue("account", "username", "TEXT");


            builder.setUseSSL(false);

            this.database = builder.build();
        } // if

    } // Database

    /**
     * Gets the Database that is built.
     * @return The Database.
     */
    public Database getDatabase() {
        if (database == null) {
            throw new UnsupportedOperationException(
                    new IllegalAccessException("Database could not be found."));
        } // if
        return database;
    } // getDatabase

} // DatabaseManager