package nl.rug.aoop.messagequeue.messagemodel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Creating a custom adapter for the serialization and deserialization of a message using GSON.
 * Overriding the method means implementing the custom deserialization and serialization respectively.
 * The super class TypeAdapter needs to be passed the type of the object targeted.
 * We do not make this class static because a static class is the same as a non-static class, except for the fact that
 * a static class cannot be instantiated.
 */
public final class MessageTypeAdapter extends TypeAdapter<Message> {
    /**
     * Header Field.
     */
    public static final String HEADER_FIELD = "Header";

    /**
     * Body Field.
     */
    public static final String BODY_FIELD = "Body";

    /**
     * Timestamp Field.
     */
    public static final String TIMESTAMP_FIELD = "Timestamp";

    /**
     * Custom method to convert to Json.
     * @param jsonWriter provides a convenient way of producing Json.
     * @param message to be converted.
     * @throws IOException signals that an error occurred.
     */
    @Override
    public void write(JsonWriter jsonWriter, Message message) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name(HEADER_FIELD);
        jsonWriter.value(message.getHeader());
        jsonWriter.name(BODY_FIELD);
        jsonWriter.value(message.getBody());
        jsonWriter.name(TIMESTAMP_FIELD);
        jsonWriter.value(message.getTimestamp().toString());
        jsonWriter.endObject();
    }

    /**
     * Custom method to convert from Json.
     * @param jsonReader read a Json object.
     * @return a new Message constructed from the provided Json.
     * @throws IOException signals that an error occurred.
     */
    @Override
    public Message read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        String header = null;
        String body = null;
        while (jsonReader.hasNext()) {
            JsonToken jsonToken = jsonReader.peek();
            String fieldName = null;
            if (jsonToken.equals(JsonToken.NAME)) {
                fieldName = jsonReader.nextName();
            }
            if (fieldName == null) {
                continue;
            }
            switch (fieldName) {
                case HEADER_FIELD -> {
                    header = jsonReader.nextString();
                }
                case BODY_FIELD -> {
                    body = jsonReader.nextString();
                }
                case TIMESTAMP_FIELD -> {
                    LocalDateTime.parse(jsonReader.nextString());
                }
            }
        }
        jsonReader.endObject();
        return new Message(header, body);
    }
}
