package nl.rug.aoop.messagequeue.messagemodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

/**
 * Models a network message.
 */
public class NetworkMessage {

    @Getter
    private final String header;
    @Getter
    private final String body;

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageTypeAdapter().nullSafe())
            .create();

    /**
     * Constructor for network message.
     * @param header the header of the message.
     * @param body the body of the message.
     */
    public NetworkMessage(String header, String body) {
        this.header = header;
        this.body = body;
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
    public static NetworkMessage fromJson(String jsonMessage) {
        return GSON.fromJson(jsonMessage, NetworkMessage.class);
    }

}
