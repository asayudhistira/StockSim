package nl.rug.aoop.stocksserver.traders;

import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Models a trader loader.
 */
public class TraderLoader {

    private final YamlLoader yamlLoader = new YamlLoader(Path.of("data", "traders.yml"));

    /**
     * Loads traders information.
     * @return a collection of traders information.
     * @throws IOException error while loading traders.
     */
    public TraderCollection loadTraders() throws IOException {
        return yamlLoader.load(TraderCollection.class);
    }
}
