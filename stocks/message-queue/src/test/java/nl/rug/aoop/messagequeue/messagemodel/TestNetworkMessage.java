package nl.rug.aoop.messagequeue.messagemodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNetworkMessage {

    private NetworkMessage networkMessage;
    private String jsonMessage;

    /**
     * Set Up method.
     */
    @BeforeEach
    public void setUp() {
        networkMessage = new NetworkMessage("1", "first");
        jsonMessage = networkMessage.toJson();
    }

    /**
     * Testing the constructor.
     */
    @Test
    public void TestConstructor() {
        assertEquals("1", networkMessage.getHeader());
        assertEquals("first", networkMessage.getBody());
    }

    /**
     * Testing Conversion to Json.
     */
    @Test
    public void TestToJson() {
        String json = "{\"header\":\"1\",\"body\":\"first\"}";
        assertEquals(json, jsonMessage);
    }

    /**
     * Testing Conversion from Json.
     */
    @Test
    public void TestFromJson() {
        String json = "{\"header\":\"1\",\"body\":\"first\"}";
        NetworkMessage fromJson = NetworkMessage.fromJson(json);
        assertEquals(networkMessage.getBody(), fromJson.getBody());
        assertEquals(networkMessage.getHeader(), fromJson.getHeader());
    }
}
