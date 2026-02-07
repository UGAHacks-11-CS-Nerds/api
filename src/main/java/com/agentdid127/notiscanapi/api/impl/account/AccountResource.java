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
public class AccountResource {

    private static final HashMap<String, AccountResource> accounts = new HashMap<String, AccountResource>();

    public static String createSalt() {
        String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 16; ++i) {
            int randInt = (int) (Math.random() * 48);
            salt.append(alphabet.charAt(randInt));
        }
        // Check its a unique Salt by pulling from database
        return salt.toString();
    }

//    public static boolean uniqueSalt(String salt) {
//
//    }

    /**
     * Retreives an account by id.
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
        out.addProperty("id", id);
        return NotiscanApiApplication.GSON.toJson(out);
    }
    /**
     * Called during the setup and returns email, password, and username
     *
     * @param email
     * @param password
     */
//    public static void createLogin(String password) {
//        this.email = email;
//        this.username = username;
//        String intoHash = getSalt() + password;
//        setHash(String.valueOf(intoHash.hashCode()));
//    }
//
//    void insertAccount() {
//    }

}
