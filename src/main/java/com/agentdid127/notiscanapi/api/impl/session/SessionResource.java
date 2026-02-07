package com.agentdid127.notiscanapi.api.impl.session;

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

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "sessionv1")
@Path("/session")
public class SessionResource {

    /**
     * Generates a session id tied to an owner.
     * @param owner of session
     * @return The user account.
     * @throws IOException If an error occured with the database.
     */
    @GET
    @Produces("application/json")
    @Path("/{owner}")
    public String GetSession(@PathParam("owner") long owner) throws IOException {
        long sessionId = NotiscanApiApplication.SNOWFLAKE.nextId();
        NotiscanApiApplication.sessions.put(sessionId, owner);
        JsonObject out = new JsonObject();
        out.addProperty("id", sessionId);
        out.addProperty("owner", owner);
        return NotiscanApiApplication.GSON.toJson(out);
    }
}
