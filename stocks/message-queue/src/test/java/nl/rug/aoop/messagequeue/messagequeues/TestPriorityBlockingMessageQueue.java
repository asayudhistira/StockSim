package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestPriorityBlockingMessageQueue implements TestMessageQueue {

    private MessageQueue priorityBlockingQueue;

    /**
     * Create an PriorityBlockingMessageQueue.
     * This method will be executed before each of the test methods.
     */
    @BeforeEach
    public void setUp() {
        priorityBlockingQueue = new PriorityBlockingMessageQueue();
    }


    /**
     * When created, a priority blocking queue message queue is empty (size = 0).
     */
    @Test @Override
    public void TestConstructor() {
        assertEquals(0, priorityBlockingQueue.getSize(), "Size should be 0.");
        assertFalse(priorityBlockingQueue.getSize() > 0);
    }

    /**
     * Enqueue a message to a priority blocking message queue.
     * The size of the queue will be incremented by one everytime we enqueue.
     */
    @Test @Override
    public void TestEnqueueIncrementsSizeBy1() {
        priorityBlockingQueue.enqueue(getMessage(1));
        assertEquals(1, priorityBlockingQueue.getSize(), "Size should be 1.");
        priorityBlockingQueue.enqueue(getMessage(2));
        assertEquals(2, priorityBlockingQueue.getSize(), "Size should be 2.");
    }

    /**
     * Enqueue a null message to a priority blocking message queue.
     * A NullPointerException will be thrown and a message is shown.
     */
    @Test @Override
    public void TestEnqueueNullMessageReturnsMessage() {
        assertThrows(NullPointerException.class, () -> {
            priorityBlockingQueue.enqueue(getMessageNull());
        });

    }

    /**
     * Enqueue a null message to a priority blocking message queue.
     * Size of queue will not change.
     */
    @Test @Override
    public void TestEnqueueNullMessageSizeUnchanged() {
        assertThrows(NullPointerException.class, () -> {
            priorityBlockingQueue.enqueue(getMessageNull());
        });
        assertEquals(0, priorityBlockingQueue.getSize(), "Size should be 0.");
    }

    /**
     * Enqueue the same message twice.
     * Both messages are enqueued and the size of queue will be 2.
     */
    @Test @Override
    public void TestEnqueueSameMessageTwice() {
        priorityBlockingQueue.enqueue(getMessage(1));
        priorityBlockingQueue.enqueue(getMessage(1));
        assertEquals(2, priorityBlockingQueue.getSize(), "Both messages should be enqueued; the size should be 2.");
    }

    /**
     * Make sure that the message in the message queue exists.
     */
    @Test @Override
    public void TestEnqueueMessageExists() {
        Message message = getMessage(1);
        priorityBlockingQueue.enqueue(message);
        Message dequeuedMessage = priorityBlockingQueue.dequeue();
        assertEquals(message, dequeuedMessage, "The message existed in the message queue.");
    }

    /**
     * Dequeue a message from a priority blocking message queue.
     * The size of the queue will be decremented by one everytime we dequeue.
     */
    @Test @Override
    public void TestDequeueDecrementsSizeBy1() {
        priorityBlockingQueue.enqueue(getMessage(1));
        priorityBlockingQueue.enqueue(getMessage(2));
        priorityBlockingQueue.enqueue(getMessage(3));
        assertEquals(3, priorityBlockingQueue.getSize(), "Size should be 3.");

        priorityBlockingQueue.dequeue();
        assertEquals(2, priorityBlockingQueue.getSize(), "Size should be 2.");
    }

    /**
     * Dequeue a message when no message exists.
     * returns null.
     */
    @Test @Override
    public void TestDequeueWithNothingInQueue() {
        assertNull(priorityBlockingQueue.dequeue());
    }

    /**
     * Dequeue more times than enqueuing will not result in negative size.
     * Size of queue will be zero.
     */
    @Test @Override
    public void TestDequeueSizeGreaterOrEqualTo0() {
        priorityBlockingQueue.enqueue(getMessage(3));
        priorityBlockingQueue.dequeue();
        priorityBlockingQueue.dequeue();
        assertEquals(0, priorityBlockingQueue.getSize(), "The size is always greater or equal to 0.");
    }

    /**
     * Dequeued Message cannot be null.
     */
    @Test @Override
    public void TestDequeueNoNullMessages() {
        assertThrows(NullPointerException.class, () -> {
            priorityBlockingQueue.enqueue(getMessageNull());
        });
        priorityBlockingQueue.enqueue(getMessage(3));
        assertNotNull(priorityBlockingQueue.dequeue(), "The message is not Null.");
    }

    /**
     * Checks if the message enqueued and dequeued is in the correct order.
     */
    @Test @Override
    public void TestDequeueElementsAddedInTheRightOrder() {
        for (Message message: generateArrayList()) {
            priorityBlockingQueue.enqueue(message);
        }
        int orderedQueueSize = priorityBlockingQueue.getSize();
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < orderedQueueSize; ++i) {
            messages.add(priorityBlockingQueue.dequeue());
        }
        for (int i = 0; i < orderedQueueSize - 1; ++i) {
            assertTrue(messages.get(i).getTimestamp().compareTo(messages.get(i + 1).getTimestamp()) <= 0);
        }
    }
}
