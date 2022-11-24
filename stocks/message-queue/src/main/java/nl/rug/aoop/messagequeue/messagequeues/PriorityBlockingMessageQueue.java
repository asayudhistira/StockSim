package nl.rug.aoop.messagequeue.messagequeues;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.messagemodel.Message;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Class that represents a MessageQueue and implements the MessageQueue interface.
 * The message queue is implemented as a priority blocking queue.
 */
@Slf4j
public class PriorityBlockingMessageQueue implements MessageQueue {
    private final PriorityBlockingQueue<Message> priorityBlockingQueue = new PriorityBlockingQueue<>();

    /**
     * Method that enqueues a message in the queue.
     * @param message the enqueued message.
     */
    @Override
    public void enqueue(Message message) {
        if (message == null || message.getHeader() == null || message.getBody() == null) {
            throw new NullPointerException("Header and body should not be null!");
        } else {
            priorityBlockingQueue.put(message);
        }
    }

    /**
     * Method that dequeues a message from the queue.
     * @return the oldest message in the queue.
     */
    @Override
    public Message dequeue() {
        if (getSize() > 0) {

            return priorityBlockingQueue.poll();
        }
        return null;
    }

    /**
     * Getter.
     * @return size of the priority blocking queue.
     */
    @Override
    public int getSize() {
        return priorityBlockingQueue.size();
    }
}
