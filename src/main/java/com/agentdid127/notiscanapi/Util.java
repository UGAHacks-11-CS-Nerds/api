package com.agentdid127.notiscanapi;

import com.agentdid127.notiscanapi.api.impl.account.Account;
import com.google.gson.JsonObject;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * Creates a user salt
     * @return salt string
     */
    public static String createSalt() {
        String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder salt = new StringBuilder();

        for (int i = 0; i < 16; ++i) {
            int randInt = (int) (Math.random() * 48);
            salt.append(alphabet.charAt(randInt));
        }
        return salt.toString();
    }

    /**
     * Sets up the hash using password
     *
     * @param password Entered password
     */
    public static void createHash(Account account, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(convertStringtoByteArray((account.getSalt())));
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            account.setHash(convertByteArraytoString(hashedPassword));
        } catch (NoSuchAlgorithmException exception) {
            System.err.println("SHA-512 not available");
        }
    }

    /**
     * Helper method for createHash
     * @param string Any string
     * @return byte array from string
     */
    public static byte[] convertStringtoByteArray(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Helper method to convert byte arrays to strings.
     * @param bytes byte array
     * @return String.
     */
    public static String convertByteArraytoString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * When logging in the account helper method
     *
     * @param account Found when searching for account
     * @param password Entered by user with password
     */
    public static String authenticate(Account account, String password) {
        try {
            // Testing password
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(convertStringtoByteArray((account.getSalt())));
            byte[] testHash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String testHashString = convertByteArraytoString(testHash);
            if(testHashString.equals(account.getHash())) {
                JsonObject out = new JsonObject();
                String token = Long.toString(NotiscanApiApplication.SNOWFLAKE.nextId());
                NotiscanApiApplication.tokens.put(token, account.getId());
                out.addProperty("id", account.getId());
                out.addProperty("token", NotiscanApiApplication.SNOWFLAKE.nextId());
                return NotiscanApiApplication.GSON.toJson(out);
            }
            System.err.println("Incorrect password");
        } catch (NoSuchAlgorithmException exception) {
            System.err.println("SHA-512 not available");
        }
        return "{}";
    }
}
