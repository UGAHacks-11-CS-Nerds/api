package com.agentdid127.notiscanapi.api.impl.account;

import com.agentdid127.notiscanapi.NotiscanApiApplication;
import com.erliapp.utilities.database.DatabaseSelection;
import com.google.gson.JsonObject;
import jakarta.ws.rs.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "accountv1")
@Path("/account")
public class AccountResource {
    public static Map<String,Long> tokens = new LinkedHashMap<>(); // Token, User ID

    public static String createSalt() {
        String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder salt = new StringBuilder();

        for (int i = 0; i < 16; ++i) {
            int randInt = (int) (Math.random() * 48);
            salt.append(alphabet.charAt(randInt));
        }
        return salt.toString();
    }


    @POST
    @Produces("application/json")
    @Path("/")
    public String createAccount(String body) {
        JsonObject bodyObject = NotiscanApiApplication.GSON.fromJson(body, JsonObject.class);
        Account account =  new Account(bodyObject.get("username").getAsString(), bodyObject.get("email").getAsString(), bodyObject.get("password").getAsString());
        try {
            Account.findUserByEmail(account.getEmail());
            return "{\"success\": \"false\"}";
        } catch(IllegalArgumentException e) {
            account.insertAccount();
            return "{\"success\": \"true\"}";
        }
    }
    /**
     * Return username and email
     *
     * @param id ID of user to retreive
     * @return The user account.
     * @throws IOException If an error occured with the database.
     */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String GetAccount(@PathParam("id") String id) throws IOException {
        JsonObject out = new JsonObject();
        try {
            Account account = Account.findUserByID(Long.parseLong(id));
            out.addProperty("username", account.getUsername());
            out.addProperty("email", account.getEmail());
            return NotiscanApiApplication.GSON.toJson(out);
        } catch (Exception e) {
            System.err.println("Could not find account with id " + id);
            return "{\"username\": \"none\", \"email\": \"none\"}";
        }
    }
    /**
     * Login using email and password
     *
     * @param body Entered the email and password to login
     */
    @POST
    @Produces("application/json")
    @Path("/")
    public String loginAccount(String body) {
        JsonObject bodyObject = NotiscanApiApplication.GSON.fromJson(body, JsonObject.class);
        String email = bodyObject.get("email").getAsString();
        Account account = Account.findUserByEmail(email);
        String password = bodyObject.get("password").getAsString();
        return authenticate(account, password);
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
     * @return
     */
    public static byte[] convertStringtoByteArray(String string) {
        byte[] bytes = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            bytes[i] = (byte) string.charAt(i);
        }
        return bytes;
    }

    public static String convertByteArraytoString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append((char) aByte);
        }
        return sb.toString();
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
                tokens.put(token, account.getId());
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
