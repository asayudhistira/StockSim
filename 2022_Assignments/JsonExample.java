// package ...

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;


// Missing some null checks of course... Be sure to test your JSON conversions.
public record JsonExample(String description, int number, LocalDateTime time) {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(JsonExample.class, new JsonExampleAdapter().nullSafe())
            .create();

    public String toJson() {
        return gson.toJson(this);
    }

    public static JsonExample fromJson(String string) {
        return gson.fromJson(string, JsonExample.class);
    }

    /**
     * Custom type adapter for this JsonExample class. Determines how the JsonExample class is converted to/from json.
     */
    private static final class JsonExampleAdapter extends TypeAdapter<JsonExample> {

        public static final String DESCRIPTION_FIELD = "Description";
        public static final String NUMBER_FIELD = "Number";
        public static final String TIME_FIELD = "Time";

        @Override
        public JsonExample read(JsonReader reader) throws IOException {
            reader.beginObject();
            String description = null;
            int number = 0;
            LocalDateTime time = null;
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                String fieldName = null;
                if (token.equals(JsonToken.NAME)) {
                    fieldName = reader.nextName();
                }
                if (fieldName == null) {
                    continue;
                }
                switch (fieldName) {
                    case DESCRIPTION_FIELD -> description = reader.nextString();
                    case NUMBER_FIELD -> number = reader.nextInt();
                    case TIME_FIELD -> time = LocalDateTime.parse(reader.nextString());
                }
            }
            reader.endObject();
            return new JsonExample(description, number, time);
        }

        @Override
        public void write(JsonWriter writer, JsonExample jsonExample) throws IOException {
            writer.beginObject();
            writer.name(DESCRIPTION_FIELD);
            writer.value(jsonExample.description());
            writer.name(NUMBER_FIELD);
            writer.value(jsonExample.number());
            writer.name(TIME_FIELD);
            writer.value(jsonExample.time().toString());
            writer.endObject();
        }
    }
}
