package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A class that tests the UnorderedMessageQueue class.
 */
public class TestUnorderedMessageQueue implements TestMessageQueue {
    private MessageQueue unorderedQueue;

    /**
     * Create an UnorderedMessageQueue.
     * This method will be executed before each of the test methods.
     */
    @BeforeEach
    public void setUp() {
        unorderedQueue = new UnorderedMessageQueue();
    }

    /**
     * When created, an unordered message queue is empty (size = 0).
     */
    @Test @Override
    public void TestConstructor() {
        assertEquals(0, unorderedQueue.getSize(), "Size should be 0.");
        assertFalse(unorderedQueue.getSize() > 0);
    }

    /**
     * Enqueue a message to an unordered message queue.
     * The size of the queue will be incremented by one everytime we enqueue.
     */
    @Test @Override
    public void TestEnqueueIncrementsSizeBy1() {
        unorderedQueue.enqueue(getMessage(1));
        assertEquals(1, unorderedQueue.getSize(), "Size should be 1.");
        unorderedQueue.enqueue(getMessage(2));
        assertEquals(2, unorderedQueue.getSize(), "Size should be 2.");
    }

    /**
     * Enqueue a null message to an unordered message queue.
     * A NullPointerException will be thrown and a message is shown.
     */
    @Test @Override
    public void TestEnqueueNullMessageReturnsMessage() {
        assertThrows(NullPointerException.class, () -> {
            unorderedQueue.enqueue(getMessageNull());
        });
    }

    /**
     * Enqueue a null message to an unordered message queue.
     * Size of queue will not change.
     */
    @Test @Override
    public void TestEnqueueNullMessageSizeUnchanged() {
        assertThrows(NullPointerException.class, () -> {
            unorderedQueue.enqueue(getMessageNull());
        });
        assertEquals(0, unorderedQueue.getSize(), "Size should be 0.");
    }

    /**
     * Enqueue the same message twice.
     * Both messages are enqueued and the size of queue will be 2.
     */
    @Test @Override
    public void TestEnqueueSameMessageTwice() {
        unorderedQueue.enqueue(getMessage(1));
        unorderedQueue.enqueue(getMessage(1));
        assertEquals(2, unorderedQueue.getSize(), "Both messages should be enqueued; the size should be 2.");
    }

    /**
     * Make sure that the message in the message queue exists.
     */
    @Test @Override
    public void TestEnqueueMessageExists() {
        Message message = getMessage(1);
        unorderedQueue.enqueue(message);
        Message dequeuedMessage = unorderedQueue.dequeue();
        assertEquals(message, dequeuedMessage, "The message existed in the message queue.");
    }

    /**
     * Dequeue a message from an unordered message queue.
     * The size of the queue will be decremented by one everytime we dequeue.
     */
    @Test @Override
    public void TestDequeueDecrementsSizeBy1() {
        unorderedQueue.enqueue(getMessage(1));
        unorderedQueue.enqueue(getMessage(2));
        unorderedQueue.enqueue(getMessage(3));
        assertEquals(3, unorderedQueue.getSize(), "Size should be 3.");

        unorderedQueue.dequeue();
        assertEquals(2, unorderedQueue.getSize(), "Size should be 2.");
    }

    /**
     * Dequeue a message when no message exists.
     * returns null.
     */
    @Test @Override
    public void TestDequeueWithNothingInQueue() {
        assertNull(unorderedQueue.dequeue());
    }

    /**
     * Dequeue more times than enqueuing will not result in negative size.
     * Size of queue will be zero.
     */
    @Test @Override
    public void TestDequeueSizeGreaterOrEqualTo0() {
        unorderedQueue.enqueue(getMessage(3));
        unorderedQueue.dequeue();
        unorderedQueue.dequeue();
        assertEquals(0, unorderedQueue.getSize(), "The size is always greater or equal to 0.");
    }

    /**
     * Dequeued Message cannot be null.
     */
    @Test @Override
    public void TestDequeueNoNullMessages() {
        assertThrows(NullPointerException.class, () -> {
            unorderedQueue.enqueue(getMessageNull());
        });
        unorderedQueue.enqueue(getMessage(3));
        assertNotNull(unorderedQueue.dequeue(), "The message is not Null.");
    }

    /**
     * Checks if the message enqueued and dequeued is in the correct order.
     */
    @Test @Override
    public void TestDequeueElementsAddedInTheRightOrder() {
        ArrayList<Message> messages = generateArrayList();
        for (Message message : messages) {
            unorderedQueue.enqueue(message);
        }
        for (Message message: messages) {
            assertEquals(message, unorderedQueue.dequeue());
        }
    }
}
