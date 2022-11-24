package nl.rug.aoop.messagequeue.producers;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;

/**
 * Models a Local Producer.
 * Implements MQProducer interface.
 */
public class LocalProducer implements MQProducer {

    private MessageQueue messageQueue;

    /**
     * Constructor for LocalProducer.
     * @param messageQueue the message queue.
     */
    public LocalProducer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Enqueue a message to a message queue.
     * @param message the message.
     */
    @Override
    public void put(Message message) {
        messageQueue.enqueue(message);
    }
}
