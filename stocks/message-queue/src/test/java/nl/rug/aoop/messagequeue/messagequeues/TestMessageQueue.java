package nl.rug.aoop.messagequeue.messagequeues;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * An interface that is used by the TestOrderedMessageQueue and TestUnorderedMessageQueue classes.
 */
public interface TestMessageQueue {
    /**
     * This getter generates a message with a header and body based on the value of the parameter x.
     * @param x represents the header of the message.
     * @return message.
     */
    default Message getMessage(int x) {
        return new Message(String.valueOf(x), "This is message number " + x + ".");
    }

    /**
     * Getter.
     * @return a null message.
     */
    default Message getMessageNull() {
        return new Message(null, null);
    }

    /**
     * Method that adds immutable objects(messages) to an ArrayList.
     * @return an ArrayList of messages.
     */
    default ArrayList<Message> generateArrayList() {
        ArrayList<Message> messageArrayList = new ArrayList<>();
        messageArrayList.add(getMessage(2));
        messageArrayList.add(getMessage(2));
        messageArrayList.add(getMessage(1));
        messageArrayList.add(getMessage(1));
        messageArrayList.add(getMessage(3));
        return messageArrayList;
    }

    /**
     * Defining the methods a TestMessageQueue class should implement.
     */
    @Test
    void TestConstructor();
    @Test
    void TestEnqueueIncrementsSizeBy1();
    @Test
    void TestEnqueueNullMessageReturnsMessage();
    @Test
    void TestEnqueueNullMessageSizeUnchanged();
    @Test
    void TestEnqueueSameMessageTwice();
    @Test
    void TestEnqueueMessageExists();
    @Test
    void TestDequeueDecrementsSizeBy1();
    @Test
    void TestDequeueWithNothingInQueue();
    @Test
    void TestDequeueSizeGreaterOrEqualTo0();
    @Test
    void TestDequeueNoNullMessages();
    @Test
    void TestDequeueElementsAddedInTheRightOrder();
}
