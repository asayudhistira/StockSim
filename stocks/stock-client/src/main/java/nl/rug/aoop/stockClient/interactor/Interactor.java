package nl.rug.aoop.stockClient.interactor;

import nl.rug.aoop.messagequeue.messagemodel.Message;

/**
 * Models an Interactor.
 */
public interface Interactor {

    /**
     * Places an order.
     */
    void placeOrder();

    /**
     * Registers a client to the server.
     * @param message the message with botId as body.
     */
    void register(Message message);

    /**
     * De-registers a client from the server.
     * @param message the message with botId as body.
     */
    void deregister(Message message);
}
