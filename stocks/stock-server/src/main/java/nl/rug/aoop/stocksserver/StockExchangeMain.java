package nl.rug.aoop.stocksserver;

import lombok.extern.slf4j.Slf4j;

import nl.rug.aoop.initialization.AbstractViewFactory;
import nl.rug.aoop.initialization.WebViewFactory;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;

/**
 * Models a stock exchange main.
 */
@Slf4j
public class StockExchangeMain {
    /**
     * Main function.
     * @param args the argument.
     */
    public static void main(String[] args) {
        //log.debug(String.valueOf(StockExchange.PORT));
        StockExchange stockExchange = StockExchange.getInstance();
        AbstractViewFactory viewFactory = new WebViewFactory();
        viewFactory.createView(stockExchange);
    }
}
