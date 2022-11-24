package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Models an unordered queue.
 * Implements MessageQueue interface.
 */
public class UnorderedMessageQueue implements MessageQueue {
    private Queue<Message> unorderedQueue;

    /**
     * Constructor for unordered queue.
     */
    public UnorderedMessageQueue() {
        this.unorderedQueue = new LinkedList<>();
    }

    /**
     * Enqueues a message into the queue.
     * @param message the message to be enqueued.
     * @throws NullPointerException if the message key or message value is null.
     */
    @Override
    public void enqueue(Message message) throws NullPointerException {
        if (message == null || message.getHeader() != null || message.getBody() != null) {
            unorderedQueue.add(message);
        } else {
            throw new NullPointerException("Header and body should not be null!");
        }
    }

    /**
     * Remove a message from the queue.
     * @return the oldest message in the message queue.
     */
    @Override
    public Message dequeue() {
        return unorderedQueue.poll();
    }

    /**
     * get size of unordered queue.
     * @return size of message queue.
     */
    @Override
    public int getSize() {
        return unorderedQueue.size();
    }
}
