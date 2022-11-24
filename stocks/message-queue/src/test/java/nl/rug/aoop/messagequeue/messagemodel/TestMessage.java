package nl.rug.aoop.messagequeue.messagemodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the conversion methods found inside the Message class.
 */
public class TestMessage {
    private Message message;
    private Message message2;
    private String jsonMessage;

    /**
     * Set Up method.
     */
    @BeforeEach
    public void setUp() {
        message = new Message("1", "first");
        message2 = new Message("2", "second");
        jsonMessage = message.toJson();
    }

    /**
     * Test Message Constructor.
     */
    @Test
    public void TestConstructor() {
        assertEquals("1", message.getHeader());
        assertEquals("first", message.getBody());
    }

    /**
     * Testing conversion from Message to Json.
     */
    @Test
    public void TestToJson() {
        String timestamp = message.getTimestamp().toString();
        String json = "{\"Header\":\"1\",\"Body\":\"first\",\"Timestamp\":" + "\"" + timestamp + "\"}";
        assertEquals(json, jsonMessage);
    }

    /**
     * Testing Conversion from Json.
     */
    @Test
    public void TestFromJson() {
        Message newMessage = new Message("2", "second");
        String json = "{\"Header\":\"2\",\"Body\":\"second\"}";
        Message fromJson = Message.fromJson(json);
        assertEquals(newMessage.getBody(), fromJson.getBody());
        assertEquals(newMessage.getHeader(), fromJson.getHeader());
    }

    /**
     * Testing conversion from Json and to Json together.
     */
    @Test
    public void TestFromJsonToJson() {
        Message fromJson = Message.fromJson(jsonMessage);
        String timestamp = fromJson.getTimestamp().toString();
        String json = "{\"Header\":\"1\",\"Body\":\"first\",\"Timestamp\":" + "\"" + timestamp + "\"}";
        assertEquals(fromJson.toJson(), json);
    }

}
