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

    // private static final HashMap<String, AccountResource> accounts = new HashMap<String, AccountResource>();

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
     * Sets up the hash using password
     *
     * @param password
     */
    public static void createHash(Account account, String password) {
        String intoHash = account.getSalt() + password;
        account.setHash(String.valueOf(intoHash.hashCode()));
    }

}
