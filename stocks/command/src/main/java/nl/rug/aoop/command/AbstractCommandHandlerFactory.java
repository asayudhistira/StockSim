package nl.rug.aoop.command;

/**
 * An Interface for the Command Handlers.
 */
public interface AbstractCommandHandlerFactory {

    /**
     * Creates a commandHandler.
     * @return a new commandHandler.
     */
    CommandHandler create();
}
