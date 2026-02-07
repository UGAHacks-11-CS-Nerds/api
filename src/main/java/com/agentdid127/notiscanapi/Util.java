package com.agentdid127.notiscanapi;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Util {
    /**
     * Loads a YAML config file.
     * @param file A config file name.
     * @return A map of configuration objects.
     * @throws IOException Occurs when config fails to load or be found.
     */
    public static Map<String,Object> loadYAMLConfig(String path, String file) throws IOException {
        Path pluginDataPath = Paths.get(path);

        if (!pluginDataPath.toFile().exists()) {
            pluginDataPath.toFile().mkdirs();
        } // if

        Path aliasConfigPath = pluginDataPath.resolve(file);

        Yaml yaml = new Yaml();

        InputStream inputStream = new FileInputStream(aliasConfigPath.toFile());

        return yaml.load(inputStream);
    } // loadYAMLConfig
}
