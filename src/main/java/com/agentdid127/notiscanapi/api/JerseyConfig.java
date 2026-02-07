package com.agentdid127.notiscanapi.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.agentdid127.notiscanapi.api.impl.ClassManager;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    /**
     * Constructor. Registers webpages.
     */
    public JerseyConfig() {

        // Temporary welcome
        register(Controller.class);

        // Filters
        register(CORSResponseFilter.class);
        register(CORSRequestFilter.class);

        // Register all handlers needed.
        for (Class aClass : ClassManager.classes) {
            register(aClass);
        }

        // Enable debug logs
        register(
                new LoggingFeature(
                        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                        Level.INFO,
                        LoggingFeature.Verbosity.PAYLOAD_ANY,
                        10000));
    }
}