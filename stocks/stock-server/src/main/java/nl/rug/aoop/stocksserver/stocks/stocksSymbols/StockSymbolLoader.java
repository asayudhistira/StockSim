package nl.rug.aoop.stocksserver.stocks.stocksSymbols;

import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Models a stock symbol loader.
 */
public class StockSymbolLoader {
    private final YamlLoader yamlLoader = new YamlLoader(Path.of("data", "stock-symbols.yml"));

    /**
     * Loads a stock.
     * @return a collection of stock symbols.
     * @throws IOException error while loading.
     */
    public StockSymbolsCollection loadSymbols() throws IOException {
        return yamlLoader.load(StockSymbolsCollection.class);
    }
}
