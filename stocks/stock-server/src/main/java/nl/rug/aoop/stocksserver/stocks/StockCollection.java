package nl.rug.aoop.stocksserver.stocks;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.Map;

/**
 * Models a stock collection.
 */
@Getter
public class StockCollection {
    private Map<String, Stock> stocks;

    /**
     * Converts to json.
     * @return a json string.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
