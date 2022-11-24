package nl.rug.aoop.messagequeue.producers;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;
import nl.rug.aoop.messagequeue.messagequeues.OrderedMessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLocalProducer {

    private MessageQueue messageQueue;
    private LocalProducer localProducer;
    private Message message;

    /**
     * Creates a message queue, local producer and a message.
     */
    @BeforeEach
    void setUp() {
        messageQueue = new OrderedMessageQueue();
        localProducer = new LocalProducer(messageQueue);
        message = new Message("first", "test");
    }

    /**
     * Test to make sure local producer is not null.
     */
    @Test
    public void TestConstructor() {
        assertNotNull(localProducer);
    }

    /**
     * Test to see that message queue size increments when a valid message is passed.
     */
    @Test
    public void TestPutValid() {
        localProducer.put(message);
        assertEquals(1,messageQueue.getSize());
    }

    /**
     * Test to see that a NullPointerException is obtained when a null message is passed.
     */
    @Test
    public void TestPutNullMessage() {
        assertThrows(NullPointerException.class, () -> {
            localProducer.put(null);
        });
    }
}
