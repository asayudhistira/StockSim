package nl.rug.aoop.messagequeue.consumer;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;

/**
 * Models a Local Consumer.
 * Implements MQConsumer interface.
 */
public class LocalConsumer implements MQConsumer {
    private MessageQueue messageQueue;

    /**
     * Constructor for LocalConsumer.
     * @param messageQueue the message queue.
     */
    public LocalConsumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Dequeue a message from a message queue.
     */
    @Override
    public Message poll() {
        return messageQueue.dequeue();
    }
}
