package nl.rug.aoop.stockClient.traders;

import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Models a trader id loader.
 */
public class TraderIdLoader {
    private final YamlLoader yamlLoader = new YamlLoader(Path.of("data", "trader-ids.yml"));

    /**
     * Loads the trader ids from the trader id collection.
     * @return collection of trader ids.
     * @throws IOException error while loading yaml file.
     */
    public TraderIdCollection loadTraderIds() throws IOException {
        return yamlLoader.load(TraderIdCollection.class);
    }
}
