package nl.rug.aoop.stocksserver.command;

import nl.rug.aoop.command.Command;
import nl.rug.aoop.networking.client.Communicator;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;

import java.util.Map;

/**
 * Models a deregister command.
 */
public class DeregisterCommand implements Command {

    private final StockExchange stockExchange;

    /**
     * Constructor for deregister command.
     * @param stockExchange the stock exchange.
     */
    public DeregisterCommand(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * executes the de-register client method in the connection manager.
     * @param options the key value pairs.
     */
    @Override
    public void execute(Map<String, Object> options) {
        String message = (String) options.get("Body");
        Communicator communicator = (Communicator) options.get("Communicator");
        stockExchange.getConnectionManager().deregisterClient(message, communicator);
    }
}
