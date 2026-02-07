package com.agentdid127.notiscanapi.api.impl.message;

import com.agentdid127.notiscanapi.NotiscanApiApplication;
import com.google.gson.JsonObject;
import jakarta.ws.rs.*;
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
        return Message.loadMessageById(Long.parseLong(id)).toJson();
    }

    /**
     * Creates a message
     * @param body JSON Object containing body information, as well as session token
     * @return The message object that was created.
     */
    @POST
    @Produces("application/json")
    @Path("/")
    public String createMessage(String body) {
        JsonObject bodyObject = NotiscanApiApplication.GSON.fromJson(body, JsonObject.class);
        // TODO: verify token
        boolean is_owner = bodyObject.has("token");

        Message msg = new Message(bodyObject.get("owner").getAsLong(), bodyObject.get("msg").getAsString(), bodyObject.get("session").getAsLong(), is_owner);
        msg.insertMessage();
        return msg.toJson();
    }

    /**
     * Gets all messages in a session
     * @param session Session id
     * @return Message array in session
     * @throws IOException if something with the database screws up.
     */
    @GET
    @Produces("application/json")
    @Path("/session/{session}")
    public String getMessageFromSession(@PathParam("session") long session) throws IOException {
        Message[] messages = Message.loadMessagesBySession(session);

        StringBuilder out = new StringBuilder("[");
        for (Message m : messages) {
            out.append(m.toJson()).append(", ");
        };
        if (messages.length > 0) out = new StringBuilder(out.substring(0, out.length() - 2));
        return out + "]";
    }
}
