package com.agentdid127.notiscanapi.api.impl.message;

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
@XmlRootElement(name = "messagev1")
@Path("/message")
public class MessageResource {

    /**
     * Retreives an account by id.
     * @param id ID of user to retreive
     * @return The user account.
     * @throws IOException If an error occured with the database.
     */
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public String GetMessage(@PathParam("id") String id) throws IOException {
        JsonObject out = new JsonObject();
        out.addProperty("id", id);
        return NotiscanApiApplication.GSON.toJson(out);
    }
}
