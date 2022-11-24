package nl.rug.aoop.networking.client;

/**
 * An interface for handling message.
 *
 * Passing a communicator to the handle message method so networking is decoupled from the part that handles
 * messages, but also the other way around.
 */
public interface MessageHandler {
    /**
     * Handles the message.
     * @param message the message sent.
     * @param communicator the client (handler).
     */
    void handleMessage(String message, Communicator communicator);
}
