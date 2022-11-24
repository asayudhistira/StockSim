package nl.rug.aoop.stocksserver.stocks;

import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Models a stock loader.
 */
public class StockLoader {

    private final YamlLoader yamlLoader = new YamlLoader(Path.of("data", "stocks.yaml"));

    /**
     * Loads stocks into stockCollection.
     * @return a collection of stocks.
     * @throws IOException error while loading stocks.
     */
    public StockCollection loadStock() throws IOException {
        return yamlLoader.load(StockCollection.class);
    }
}
