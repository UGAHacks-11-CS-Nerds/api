package com.agentdid127.notiscanapi.api.impl.account;

import com.agentdid127.notiscanapi.NotiscanApiApplication;
import com.google.gson.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.IOException;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "accountv1")
@Path("/account")
public class Account {

    protected long id;
    protected String username;
    protected String email;
    protected String salt;
    protected String hash;

    private static final String[] vars = new String[]{"id","username","email","salt","hash"};
    private static final HashMap<String, Account> accounts = new HashMap<String, Account>();
    /**
     * Default Constructor
     */
    public Account() {
        this.id = 0;
        this.username = "Unknown";
        this.email = "Unknown";
        this.salt = "Unknown";
        this.hash = "Unknown";
    }

    /**
     * Constructor with all values
     * @param id id of message
     * @param username Username of account
     * @param email Email of account
     * @param salt Random unique salt for account
     * @param hash Unique hash for account
     */
    public Account(long id, String username, String email, String salt, String hash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.salt = salt;
        this.hash = hash;
    }

    /**
     * Default used during account creation
     *
     * @param username username provided by user
     * @param email email provided by user
     */
    public Account(String username, String email, String password) {
        this(NotiscanApiApplication.SNOWFLAKE.nextId(), username, email, createSalt(), "Unimplemented");
        // accounts.put(username, this);
    }

    public static String createSalt() {
        String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 16; ++i) {
            int randInt = (int) (Math.random() * 48);
            salt.append(alphabet.charAt(randInt));
        }
        // Check its a unique Salt

        return salt.toString();
    }
    public static boolean uniqueSalt(String salt) {

    }

    /**
     * Retreives an account by id.
     * @param id ID of user to retreive
     * @return The user account.
     * @throws IOException If an error occured with the database.
     */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String GetAccount(@PathParam("id") String id) throws IOException {
        JsonObject out = new JsonObject();
        out.addProperty("id", id);
        return NotiscanApiApplication.GSON.toJson(out);
    }

    public long getId() {return id;}

    public String getUsername() {return username;}

    public String getEmail() {return email;}

    public String getSalt() {return salt;}

    public String getHash() {return hash;}

    public void setId(long id) {this.id = id;}

    public void setUsername(String username) {this.username = username;}

    public void setEmail(String email) {this.email = email;}

    public void setSalt(String salt) {this.salt = salt;}

    public void setHash(String hash) {this.hash = hash;}


}
