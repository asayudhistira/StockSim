package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;

/**
 * An interface for Message Queue.
 */
public interface MessageQueue {
    /**
     * Enqueue a message from the message queue.
     * @param message the enqueued message.
     */
    void enqueue(Message message);

    /**
     * Dequeue a message from the message queue.
     * @return the oldest message in the queue.
     */
    Message dequeue();

    /**
     * get size of the queue.
     * @return size of the queue.
     */
    int getSize();
}
