package nl.rug.aoop.messagequeue.command;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;

import java.util.Map;

/**
 * Models a MqPutCommand.
 * Implements a command interface.
 */
@Slf4j
public class MqPutCommand implements Command {
    private MessageQueue messageQueue;

    /**
     * Constructor for MqPutCommand.
     * @param messageQueue the message queue used.
     */
    public MqPutCommand(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Executes a command.
     * @param options the key value pairs.
     */
    @Override
    public synchronized void execute(Map<String, Object> options) {
        String message = (String) options.get("Body");
        messageQueue.enqueue(Message.fromJson(message));
    }
}
