package com.agentdid127.notiscanapi.api.impl.account;

import com.agentdid127.notiscanapi.NotiscanApiApplication;
import com.agentdid127.notiscanapi.api.impl.message.Message;
import com.erliapp.utilities.database.DatabaseSelection;
import com.google.gson.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.IOException;

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
        this(NotiscanApiApplication.SNOWFLAKE.nextId(), username, email, AccountResource.createSalt(), null);
        AccountResource.createHash(this, password);
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

    public void insertAccount() {
        NotiscanApiApplication.DATABASE.insert("account", vars, this.id, this.username, this.email, this.salt, this.hash);
    }

    public static Account findUserByID(long id) {
        DatabaseSelection selection = NotiscanApiApplication.DATABASE.select(vars, "account", "id = " + id + " LIMIT 1");
        if (selection.size() == 0) throw new IllegalStateException("User Account does not exist");
        return fromSelection(selection, 0);
    }

    public static Account findUserByName(String username) {
        DatabaseSelection selection = NotiscanApiApplication.DATABASE.select(vars, "account", "username = \"" + username + "\" LIMIT 1");
        if (selection.size() == 0) throw new IllegalStateException("User Account does not exist");
        return fromSelection(selection, 0);
    }
    public static Account findUserByEmail(String email) {
        DatabaseSelection selection = NotiscanApiApplication.DATABASE.select(vars, "account", "email = \"" + email + "\" LIMIT 1");
        if (selection.size() == 0) throw new IllegalStateException("User Account does not exist");
        return fromSelection(selection, 0);
    }

    private static Account fromSelection(DatabaseSelection selection, int idx) {
        long id_out = selection.getRow(idx).get("id").getLong();
        String username_out = selection.getRow(idx).get("username").getString();
        String email_out = selection.getRow(idx).get("email").getString();
        String salt_out = selection.getRow(idx).get("salt").getString();
        String hash_out = selection.getRow(idx).get("hash").getString();
        return new Account(id_out, username_out, email_out, salt_out, hash_out);
    }
}
