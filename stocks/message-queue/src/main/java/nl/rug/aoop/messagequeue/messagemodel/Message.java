package nl.rug.aoop.messagequeue.messagemodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Models a Message.
 */
public final class Message implements Comparable<Message> {

    @Getter
    private final String header;
    @Getter
    private final String body;
    @Getter
    private final LocalDateTime timestamp;
    
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageTypeAdapter().nullSafe())
            .create();

    /**
     * Constructor for Message.
     * @param header  header of message
     * @param body   body of message
     */
    public Message(String header, String body) {
        this.header = header;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Convert a message string into a JSON string.
     * @return a Json string.
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Converts a JSON string into a message.
     * @param jsonMessage the Json string.
     * @return the messages.
     */
    public static Message fromJson(String jsonMessage) {
        return GSON.fromJson(jsonMessage, Message.class);
    }

    /**
     * Compares the timestamp of two messages.
     * @param message the message we want to compare with.
     * @return integer value 0 if message has equal timestamp.
     */
    @Override
    public int compareTo(Message message) {
        return this.timestamp.compareTo(message.getTimestamp());
    }
}

