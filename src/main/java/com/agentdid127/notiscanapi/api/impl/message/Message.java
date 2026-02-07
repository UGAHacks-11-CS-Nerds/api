package com.agentdid127.notiscanapi.api.impl.message;

import com.agentdid127.notiscanapi.NotiscanApiApplication;
import com.erliapp.utilities.database.DatabaseSelection;

import java.util.ArrayList;
import java.util.Date;

public class Message {

    protected long id;
    protected long owner;
    protected long date;
    protected String msg;
    protected long session;
    protected long is_owner;

    private static String[] vars = new String[]{"id", "owner", "date", "msg", "session", "is_owner"}

    /**
     * Default Constructor
     */
    public Message() {
        this.id = 0;
        this.owner = 0;
        this.date = 0;
        this.msg = "Unknown";
        this.session = 0;
        this.is_owner = 0;
    }

    /**
     * Constructor with all values
     * @param id id of message
     * @param owner Owner of message
     * @param date Date message was sent
     * @param msg Message
     * @param session Session id
     * @param is_owner Whether the owner sent it
     */
    public Message(long id, long owner, long date, String msg, long session, long is_owner) {
        this.id = id;
        this.owner = owner;
        this.date = date;
        this.msg = msg;
        this.session = session;
        this.is_owner = is_owner;
    }

    /**
     * Normal constructor. Auto generates what's needed
     * @param owner Owner id
     * @param msg Message value
     * @param session Session Id
     * @param is_owner whether the owner sent it
     */
    public Message(long owner, String msg, long session, boolean is_owner) {
        this(NotiscanApiApplication.SNOWFLAKE.nextId(), owner, (new Date()).getTime(), msg, session, is_owner ? 1L : 0L);
    }

    /**
     * Sets the message date
     * @param date date of message
     */
    public void setDate(Date date) {
        this.date = date.getTime();
    }

    /**
     * Gets the message date
     * @return message date
     */
    public Date getDate() {
        return new Date(date);
    }

    /**
     * Sets the message
     * @param msg message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Gets the message
     * @return message text
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the message ids
     * @param id message id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the message id.
     * @return message id.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets whether the owner sent it
     * @param is_owner whether the owner sent it
     */
    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner ? 1 : 0;
    }

    /**
     * Gets whether the owner sent it.
     * @return whether the owner sent it.
     */
    public boolean getIs_owner() {
        return is_owner != 0;
    }

    /**
     * Sets the owner id
     * @param owner owner id.
     */
    public void setOwner(long owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner id
     * @return the owner id.
     */
    public long getOwner() {
        return owner;
    }

    /**
     * Gets the session id
     * @return The session.
     */
    public long getSession() {
        return session;
    }

    /**
     * Sets the session id
     * @param session session id.
     */
    public void setSession(long session) {
        this.session = session;
    }

    /**
     * Converts the object to JSON.
     * @return Json string.
     */
    public String toJson() {
        return NotiscanApiApplication.GSON.toJson(this, Message.class);
    }

    /**
     * Inserts a new message into the database
     */
    void insertMessage() {
        NotiscanApiApplication.DATABASE.insert("messages", vars, this.id, this.owner, this.date, this.msg, this.session, this.is_owner);
    }

    /**
     * Updates message in database.
     */
    void updateMessage() {
        NotiscanApiApplication.DATABASE.update("UPDATE messages SET owner = " + this.owner + ", date = " + this.date + ", msg = \"" + this.msg + "\", session = " + this.session + ", is_owner = " + this.is_owner + " WHERE id = " + this.id);
    }

    /**
     * Loads a message by id
     * @param id id of message
     * @return message.
     */
    public static Message loadMessageById(long id) {
        DatabaseSelection selection = NotiscanApiApplication.DATABASE.select(vars, "messages", "id = " + id + " LIMIT 1");
        if (selection.size() == 0) {
            throw new IllegalStateException("Message does not exist!");
        }
        return fromSelection(selection, 0);
    }


    /**
     * Loads all messages in a session
     * @param session Session id
     * @return array of all messages in session
     */
    public static Message[] loadMessagesBySession(long session) {
        DatabaseSelection selection = NotiscanApiApplication.DATABASE.select(vars, "messages", "session = " + session + " LIMIT 1");
        if (selection.size() == 0) {
            throw new IllegalStateException("Message does not exist!");
        }

        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < selection.size(); i++) {
            messages.add(fromSelection(selection, i));
        }
        return messages.toArray(new Message[0]);
    }

    /**
     * Grabs a message from a selectino
     * @param selection selection in database
     * @param idx index of selection
     * @return Message from index in selection.
     */
    private static Message fromSelection(DatabaseSelection selection, int idx) {
        long id_out = selection.getRow(idx).get("id").getLong();
        long owner_out = selection.getRow(idx).get("owner").getLong();
        long date_out = selection.getRow(idx).get("date").getLong();
        String msg_out = selection.getRow(idx).get("msg").getString();
        long session_out = selection.getRow(idx).get("session").getLong();
        long is_owner_out = selection.getRow(idx).get("is_owner").getLong();
        return new Message(id_out, owner_out, date_out, msg_out, session_out, is_owner_out);
    }
}
