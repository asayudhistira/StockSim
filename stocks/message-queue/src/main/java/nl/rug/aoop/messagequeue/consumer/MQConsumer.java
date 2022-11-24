package nl.rug.aoop.messagequeue.consumer;

import nl.rug.aoop.messagequeue.messagemodel.Message;

/**
 * An interface for MQConsumer.
 */
public interface MQConsumer {

    /**
     * Dequeue a message from a message queue.
     * @return the dequeued message.
     */
    Message poll();
}
