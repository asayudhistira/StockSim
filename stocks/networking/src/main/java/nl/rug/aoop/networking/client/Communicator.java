package nl.rug.aoop.networking.client;

/**
 * This interface is mainly used for better design(decoupling).
 * The message handler takes in as a parameter a client.
 * But if the client implements this interface, then we can use this interface as a parameter instead of the client.
 * This basically means that behaviour is shared, but not the actual implementation, so decoupling of components is
 * achieved.
 */
public interface Communicator {

    /**
     * Sends a message.
     * @param message the message.
     */
    void send(String message);

    /**
     * Terminates the program.
     */
    void terminate();
}
