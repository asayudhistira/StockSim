package nl.rug.aoop.messagequeue.producers;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.networking.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

public class TestNetworkProducer {

    private static final int TIMEOUT = 2;
    private Client mockClient;
    private NetworkProducer networkProducer;
    private Message message;
    private NetworkMessage messageReturn;

    /**
     * Mock a client, create a network producer and a message.
     */
    @BeforeEach
    void setUp() {
        mockClient = Mockito.mock(Client.class);
        networkProducer = new NetworkProducer(mockClient);
        message = new Message("Test", "Message");
    }

    /**
     * Test to see if the createPutMessage works.
     * We check that the header is "MqPut" and the body is the json.
     */
    @Test
    void TestCreatePutMessage() {
        String timestamp = message.getTimestamp().toString();
        String json = "{\"Header\":\"Test\",\"Body\":\"Message\",\"Timestamp\":" + "\"" + timestamp + "\"}";
        messageReturn = networkProducer.createPutMessage(message);

        assertEquals("MqPut", messageReturn.getHeader());
        assertEquals(json, messageReturn.getBody());
    }

    /**
     * Test to see that a NullPointerException is passed when a null message is passed.
     */
    @Test
    void TestCreatePutMessageNull() {
        assertThrows(NullPointerException.class, () -> {
            networkProducer.createPutMessage(null);
        });
    }

    /**
     * Test to see if the put method works.
     */
    @Test
    void TestPut() {
        String test = networkProducer.createPutMessage(message).toJson();
        networkProducer.put(message);

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockClient).send(test);
        });
    }

    /**
     * Test to see that a NullPointerException is passed when a null message is passed.
     */
    @Test
    void TestPutNull() {
        assertThrows(NullPointerException.class, () -> {
            networkProducer.put(null);
        });

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            doThrow(NullPointerException.class).when(mockClient).send(null);
        });
    }
}
