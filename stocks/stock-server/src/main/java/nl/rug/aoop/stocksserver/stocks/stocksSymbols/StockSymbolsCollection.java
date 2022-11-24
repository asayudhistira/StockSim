package nl.rug.aoop.stocksserver.stocks.stocksSymbols;

import lombok.Getter;

import java.util.List;

/**
 * Models a collection of stock symbol.
 */
public class StockSymbolsCollection {
    @Getter
    private List<String> stockSymbols;
}
