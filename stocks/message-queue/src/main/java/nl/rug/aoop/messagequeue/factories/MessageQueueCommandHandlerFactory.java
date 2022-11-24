package nl.rug.aoop.messagequeue.factories;

import nl.rug.aoop.command.AbstractCommandHandlerFactory;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.command.MqPutCommand;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;

/**
 * Models a message Queue Command Handler Factory
 * Implements the AbstractCommandHandlerFactory Interface.
 */
public class MessageQueueCommandHandlerFactory implements AbstractCommandHandlerFactory {
    private MessageQueue messageQueue;

    /**
     * Constructor for message queue command handler factory.
     * @param messageQueue the message queue used.
     */
    public MessageQueueCommandHandlerFactory(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Creates a new command handler.
     * @return the command handler.
     */
    @Override
    public CommandHandler create() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand("MqPut", new MqPutCommand(messageQueue));
        return commandHandler;
    }
}
