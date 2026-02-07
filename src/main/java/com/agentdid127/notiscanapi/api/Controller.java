package com.agentdid127.notiscanapi.api;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.springframework.stereotype.Component;

@Path("/Welcome")
@Component
public class Controller {

    /**
     * Quick test script. Never felt like removing it
     *
     * @param user user to return.
     * @return Welcome, {user}!
     */
    @GET
    public String welcomeUser(@QueryParam("user") String user) {
        return "Welcome " + user;
    }
}