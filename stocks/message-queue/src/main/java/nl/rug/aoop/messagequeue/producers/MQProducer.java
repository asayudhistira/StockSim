package nl.rug.aoop.messagequeue.producers;

import nl.rug.aoop.messagequeue.messagemodel.Message;

/**
 * An interface for MQProducer.
 */
public interface MQProducer {
    /**
     * Enqueue a message to a message queue.
     * @param message the message to be enqueued.
     */
    void put(Message message);
}
