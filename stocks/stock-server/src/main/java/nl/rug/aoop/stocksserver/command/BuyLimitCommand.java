package nl.rug.aoop.stocksserver.command;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.stocksserver.orders.BuyOrder;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;

import java.util.Map;

/**
 * Models a Buy Limit command.
 */
@Slf4j
public class BuyLimitCommand implements Command {

    private final StockExchange stockExchange;

    /**
     * Constructor for a buy-limit command.
     * @param stockExchange the stock exchange.
     */
    public BuyLimitCommand(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * Executes a buy-limit by resolving order in the transaction manager.
     * @param options the key value pairs.
     */
    @Override
    public synchronized void execute(Map<String, Object> options) {
        String id = (String) options.get("id");
        String stockSymbol = (String) options.get("stockSymbol");
        long nrShares = (long) options.get("nrShares");
        double priceLimit = (double) options.get("priceLimit");

        //log.debug("This is the id received :" + id);
        //log.debug("This is the stock symbol :" + stockSymbol);
        //log.debug("This is the number of shares :" + nrShares);
        //log.debug("This is the price limit :" + priceLimit);

        BuyOrder buyOrder = new BuyOrder(id,stockSymbol, nrShares,priceLimit);
        stockExchange.getTransactionManager().finalizeBuyOrder(buyOrder);
    }
}
