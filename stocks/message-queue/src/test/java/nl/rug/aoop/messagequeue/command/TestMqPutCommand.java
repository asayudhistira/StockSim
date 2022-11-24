package nl.rug.aoop.messagequeue.command;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;
import nl.rug.aoop.messagequeue.messagequeues.PriorityBlockingMessageQueue;
import nl.rug.aoop.messagequeue.producers.NetworkProducer;
import nl.rug.aoop.networking.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MqPutCommand.
 */
public class TestMqPutCommand {

    private MqPutCommand mqPutCommand;
    private MessageQueue messageQueue;
    private Message message;
    private Message message2;
    private NetworkMessage messagePut;
    private NetworkMessage messagePut2;
    private Client mockClient;
    private NetworkProducer networkProducer;

    /**
     * Set Up method.
     */
    @BeforeEach
    public void setUp() {
        messageQueue = new PriorityBlockingMessageQueue();
        mqPutCommand = new MqPutCommand(messageQueue);
        mockClient = Mockito.mock(Client.class);
        networkProducer = new NetworkProducer(mockClient);

        message = new Message("1", "first");
        messagePut = networkProducer.createPutMessage(message);

        message2 = new Message("2", "second");
        messagePut2 = networkProducer.createPutMessage(message2);
    }

    /**
     * Initial size of message Queue is Zero.
     * The messagePut header is "MqPut".
     */
    @Test
    public void TestConstructor() {
        assertEquals(0, messageQueue.getSize());
        assertEquals("1", message.getHeader());
        assertEquals("MqPut", messagePut.getHeader());
    }

    /**
     * Test to see if the execute method works.
     * The size of message queue increments.
     */
    @Test
    public void TestExecuteSingleMessage() {
        mqPutCommand.execute(Map.of(
                "Header", messagePut.getHeader(),
                "Body", messagePut.getBody()));

        assertEquals(1, messageQueue.getSize());
    }

    /**
     * Test to see if the execute method works.
     * The size of the message queue is 2.
     */
    @Test
    public void TestExecuteMultipleMessage() {
        mqPutCommand.execute(Map.of(
                "Header", messagePut.getHeader(),
                "Body", messagePut.getBody()));

        mqPutCommand.execute(Map.of(
                "Header", messagePut2.getHeader(),
                "Body", messagePut2.getBody()));

        assertEquals(2, messageQueue.getSize());
    }

    /**
     * Test to check if the execute method returns a NullPointerException when null message is passed.     */
    @Test
    public void TestExecuteNullMessage() {
        assertThrows(NullPointerException.class, () -> {
            mqPutCommand.execute(null);
        });
    }
}
