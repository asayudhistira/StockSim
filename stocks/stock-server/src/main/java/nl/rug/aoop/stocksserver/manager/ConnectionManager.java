package nl.rug.aoop.stocksserver.manager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.networking.client.Communicator;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;
import nl.rug.aoop.stocksserver.traders.Trader;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a connection manager.
 */
@Slf4j
public class ConnectionManager {

    private final StockExchange stockExchange;
    @Getter
    private Map<String, Communicator> connectedTraders;

    /**
     * Constructor for connection manager.
     * @param stockExchange the stock exchange.
     */
    public ConnectionManager(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
        this.connectedTraders = new HashMap<>();
    }

    /**
     * Registers a client to the server.
     * @param message the botId.
     * @param communicator the clientHandler handling the message.
     */
    public void registerClient(String message, Communicator communicator) {
        for (Trader t: stockExchange.getTraders().getTraders()) {
            if(t.getId().equals(message)) {
                connectedTraders.put(message, communicator);
                //log.info("Successfully register " + message);
            }
        }
    }

    /**
     * De-registers a client from the server.
     * @param message the notId.
     * @param communicator the client handler.
     */
    //TODO: Why it is only taken into consideration the trader at index up to size - 2?
    public void deregisterClient(String message, Communicator communicator) {
        for (int i = 0; i < connectedTraders.size() - 1; i++) {
            if(connectedTraders.containsKey(message) && connectedTraders.get(message) == communicator) {
                connectedTraders.remove(message);
            }
        }
    }

    /**
     * Sends information back to the client side.
     * @param id the id of the client.
     * @param message message of stock and trader info.
     */
    public void send(String id, NetworkMessage message) {
        Communicator communicator = connectedTraders.get(id);
        //log.debug("Using communicator " + communicator.toString() + " to send message " + message.toJson());
        communicator.send(message.toJson());
        //log.debug("The message we want to send: " + message.toJson());
    }

}