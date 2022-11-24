package nl.rug.aoop.stocksserver.command;

import nl.rug.aoop.command.Command;
import nl.rug.aoop.networking.client.Communicator;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;

import java.util.Map;

/**
 * Models a stock exchange.
 */
public class RegisterCommand implements Command {

    private final StockExchange stockExchange;

    /**
     * Constructor for register command.
     * @param stockExchange the stock exchange.
     */
    public RegisterCommand(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }


    /**
     * executes the register client method in the connection manager.
     * @param options the key value pairs.
     */
    @Override
    public void execute(Map<String, Object> options) {
        String message = (String) options.get("Body");
        Communicator communicator = (Communicator) options.get("Communicator");
        stockExchange.getConnectionManager().registerClient(message, communicator);
    }
}
