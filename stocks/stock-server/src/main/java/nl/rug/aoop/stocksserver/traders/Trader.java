package nl.rug.aoop.stocksserver.traders;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.model.TraderDataModel;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Models a Trader.
 */
@Getter
public class Trader implements TraderDataModel {
    private String id;
    private String name;
    @Setter
    private double funds;
    @Setter
    private Map<String, Long> ownedShares;

    /**
     * Gets owned stocks of a trader.
     * @return the trader's stocks.
     */
    @Override
    public List<String> getOwnedStocks() {
        return getOwnedShares().keySet().stream().toList();
    }

    @Override
    public long getNumberOfOwnedShares(String stockSymbol) {
        return getOwnedShares().get(stockSymbol);
    }

    /**
     * Converts to json.
     * @return string json containing trader info.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
