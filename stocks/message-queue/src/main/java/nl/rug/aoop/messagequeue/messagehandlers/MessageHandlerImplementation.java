package nl.rug.aoop.messagequeue.messagehandlers;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.networking.client.Communicator;
import nl.rug.aoop.networking.client.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * A message handler implementation.
 */
@Slf4j
public class MessageHandlerImplementation implements MessageHandler {
    private CommandHandler commandHandler;

    /**
     * Constructor for Message handler implementation.
     * @param commandHandler the command handler.
     */
    public MessageHandlerImplementation(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Handles a message.
     * @param message the message being passed.
     * @param communicator the client.
     * @throws IllegalArgumentException if message is null.
     */
    @Override
    public void handleMessage(String message, Communicator communicator) throws IllegalArgumentException {

        if(message == null) {
            throw new IllegalArgumentException("Message could not be null");
        }

        log.info(message);

        NetworkMessage fromJson = NetworkMessage.fromJson(message);
        Map<String, Object> options = new HashMap<>();
        //log.debug("This is the ");
        options.put("Header", fromJson.getHeader());
        options.put("Body", fromJson.getBody());
        options.put("Communicator", communicator);
        commandHandler.executeCommand(fromJson.getHeader(), options);
    }
}