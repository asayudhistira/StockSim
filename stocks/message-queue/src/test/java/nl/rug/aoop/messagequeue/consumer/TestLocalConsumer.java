package nl.rug.aoop.messagequeue.consumer;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;
import nl.rug.aoop.messagequeue.messagequeues.OrderedMessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLocalConsumer {
    public static final int NUMBER_OF_PUTS = 10;
    private LocalConsumer localConsumer;
    private MessageQueue messageQueue;
    private Message message;

    @BeforeEach
    void setUp() {
        messageQueue = new OrderedMessageQueue();
        localConsumer = new LocalConsumer(messageQueue);
        message = new Message("1", "first");
    }

    @Test
    public void TestConstructor() {
        assertNotNull(localConsumer);
    }

    @Test
    public void TestPoll() {
        messageQueue.enqueue(message);
        assertSame(message, localConsumer.poll());
    }

    @Test
    public void TestPollNotNull() {
        messageQueue.enqueue(message);
        assertNotNull(localConsumer.poll());
    }

    @Test
    public void TestPollSizeDecreased() {
        for (int i = 0; i < NUMBER_OF_PUTS; ++i) {
            messageQueue.enqueue(message);
        }
        int size = NUMBER_OF_PUTS;
        for (int i = 0; i < NUMBER_OF_PUTS; ++i, --size) {
            assertEquals(messageQueue.getSize(), size);
            localConsumer.poll();
        }
    }
}
