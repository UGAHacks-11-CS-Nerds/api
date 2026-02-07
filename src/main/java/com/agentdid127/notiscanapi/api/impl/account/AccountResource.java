package com.agentdid127.notiscanapi.api.impl.account;

import com.agentdid127.notiscanapi.NotiscanApiApplication;
import com.agentdid127.notiscanapi.Util;
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


/**
 * Account Resource
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "accountv1")
@Path("/account")
public class AccountResource {

    /**
     * Creates an account
     * @param body JSON Object body
     * @return JSON Object return.
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/")
    public String createAccount(String body) {
        JsonObject bodyObject = NotiscanApiApplication.GSON.fromJson(body, JsonObject.class);
        Account account =  new Account(bodyObject.get("username").getAsString(), bodyObject.get("email").getAsString(), bodyObject.get("password").getAsString());
        try {
            Account.findUserByEmail(account.getEmail());
            return "{\"success\": \"false\"}";
        } catch(IllegalStateException e) {
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
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/")
    public String loginAccount(String body) {
        JsonObject bodyObject = NotiscanApiApplication.GSON.fromJson(body, JsonObject.class);
        String email = bodyObject.get("email").getAsString();
        Account account = Account.findUserByEmail(email);
        String password = bodyObject.get("password").getAsString();
        return Util.authenticate(account, password);
    }

}
