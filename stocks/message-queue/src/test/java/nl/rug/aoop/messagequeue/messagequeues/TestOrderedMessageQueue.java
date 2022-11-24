package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A class that tests the OrderedMessageQueue class.
 */
public class TestOrderedMessageQueue implements TestMessageQueue {
    private MessageQueue orderedQueue;

    /**
     * Create an OrderedMessageQueue.
     * This method will be executed before each of the test methods.
     */
    @BeforeEach
    public void setUp() {
        orderedQueue = new OrderedMessageQueue();
    }

    /**
     * When created, an ordered message queue is empty (size = 0).
     */
    @Test @Override
    public void TestConstructor() {
        assertEquals(0, orderedQueue.getSize(), "Size should be 0.");
        assertFalse(orderedQueue.getSize() > 0);
    }

    /**
     * Enqueue a message to an ordered message queue.
     * The size of the message queue will be incremented by one everytime we enqueue.
     */
    @Test @Override
    public void TestEnqueueIncrementsSizeBy1() {
        orderedQueue.enqueue(getMessage(1));
        assertEquals(1, orderedQueue.getSize(), "Size should be 1.");
        orderedQueue.enqueue(getMessage(1));
        assertEquals(2, orderedQueue.getSize(), "Size should be 2.");
    }

    /**
     * Enqueue a null message to an ordered message queue.
     * A NullPointerException will be thrown and a message is shown.
     */
    @Test @Override
    public void TestEnqueueNullMessageReturnsMessage() {
        assertThrows(NullPointerException.class, () -> {
            orderedQueue.enqueue(getMessageNull());
        });
    }

    /**
     * Enqueue a null message to an ordered message queue.
     * Size of queue will not change.
     */
    @Test @Override
    public void TestEnqueueNullMessageSizeUnchanged() {
        assertThrows(NullPointerException.class, () -> {
            orderedQueue.enqueue(getMessageNull());
        });
        assertEquals(0, orderedQueue.getSize(), "Size should be 0.");
    }

    /**
     * Enqueue the same message twice.
     * Both messages are enqueued and the size of queue will be 2.
     */
    @Test @Override
    public void TestEnqueueSameMessageTwice() {
        orderedQueue.enqueue(getMessage(1));
        orderedQueue.enqueue(getMessage(1));
        assertEquals(2, orderedQueue.getSize(), "Both messages should be enqueued; the size should be 2.");
    }

    /**
     * Make sure that the message in the queue exists.
     */
    @Test @Override
    public void TestEnqueueMessageExists() {
        Message message = getMessage(1);
        orderedQueue.enqueue(message);
        Message dequeuedMessage = orderedQueue.dequeue();
        assertEquals(message, dequeuedMessage, "The message existed in the message queue.");
    }

    /**
     * Dequeue a message from an ordered message queue.
     * The size of the queue will be decremented by one everytime we dequeue.
     */
    @Test @Override
    public void TestDequeueDecrementsSizeBy1() {
        orderedQueue.enqueue(getMessage(1));
        orderedQueue.enqueue(getMessage(2));
        orderedQueue.enqueue(getMessage(3));
        assertEquals(3, orderedQueue.getSize(), "Size should be 3.");

        orderedQueue.dequeue();
        assertEquals(2, orderedQueue.getSize(), "Size should be 2.");
    }

    /**
     * Dequeue a message when no message exists.
     * returns null.
     */
    @Test @Override
    public void TestDequeueWithNothingInQueue() {
        assertNull(orderedQueue.dequeue());
    }

    /**
     * Dequeue more times than enqueuing will not result in negative size.
     * Size of queue will be zero.
     */
    @Test @Override
    public void TestDequeueSizeGreaterOrEqualTo0() {
        orderedQueue.enqueue(getMessage(3));
        orderedQueue.dequeue();
        orderedQueue.dequeue();
        assertEquals(0, orderedQueue.getSize(), "The size is always greater or equal to 0.");
    }

    /**
     * Dequeued Message cannot be null.
     */
    @Test @Override
    public void TestDequeueNoNullMessages() {
        assertThrows(NullPointerException.class, () -> {
            orderedQueue.enqueue(getMessageNull());
        });
        orderedQueue.enqueue(getMessage(2));
        assertNotNull(orderedQueue.dequeue(), "The message is not Null.");
    }

    /**
     * Checks if the message enqueued and dequeued is in the correct order.
     */
    @Test @Override
    public void TestDequeueElementsAddedInTheRightOrder() {
        for (Message message: generateArrayList()) {
            orderedQueue.enqueue(message);
        }
        int orderedQueueSize = orderedQueue.getSize();
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < orderedQueueSize; ++i) {
            messages.add(orderedQueue.dequeue());
        }
        for (int i = 0; i < orderedQueueSize - 1; ++i) {
            assertTrue(messages.get(i).getTimestamp().compareTo(messages.get(i + 1).getTimestamp()) <= 0);
        }
    }
}
