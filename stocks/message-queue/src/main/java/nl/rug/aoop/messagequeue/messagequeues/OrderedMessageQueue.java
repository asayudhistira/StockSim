package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Models an ordered queue.
 * Implements MessageQueue interface.
 */
public class OrderedMessageQueue implements MessageQueue {

    private final SortedMap<LocalDateTime, Queue<Message>> orderedQueue;

    /**
     * Constructor for ordered queue.
     */
    public OrderedMessageQueue() {
        this.orderedQueue = new TreeMap<>();
    }

    /**
     * Enqueues a message into the queue.
     * @param message the message to be enqueued.
     * @throws NullPointerException if the message key or message value is null.
     */
    @Override
    public void enqueue(Message message) throws NullPointerException {
        if (message == null || message.getHeader() == null || message.getBody() == null) {
            throw new NullPointerException("Header and body should not be null!");
        } else if (!orderedQueue.containsKey(message.getTimestamp())) {
            Queue<Message> values = new LinkedList<>();
            values.add(message);
            orderedQueue.put(message.getTimestamp(), values);
        } else {
            orderedQueue.get(message.getTimestamp()).add(message);
        }
    }

    /**
     * Remove a message from the queue.
     * @return the oldest message in the message queue.
     */
    @Override
    public Message dequeue() {
        if (orderedQueue.size() == 0) {
            return null;
        }
        int length = orderedQueue.get(orderedQueue.firstKey()).size();
        if (length == 0) {
            orderedQueue.remove(orderedQueue.firstKey());
        }
        if (orderedQueue.size() != 0) {
            Message message = orderedQueue.get(orderedQueue.firstKey()).peek();
            orderedQueue.get(orderedQueue.firstKey()).remove();
            return message;
        }
        return null;
    }

    /**
     * Get size of ordered queue.
     * @return the size of the ordered queue.
     */
    @Override
    public int getSize() {
        int size = 0;
        for (SortedMap.Entry<LocalDateTime, Queue<Message>> entry : orderedQueue.entrySet()) {
            size += entry.getValue().size();
        }
        return size;
    }
}
