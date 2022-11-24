package nl.rug.aoop.stockClient.stockMarket;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.Map;

/**
 * Models a stock collection model.
 */
@Getter
public class StockCollectionModel {
    private Map<String, StockModel> stocks;

    /**
     * Constructor for stock collection.
     * @param stocks a map of stock symbol to stocks.
     */
    public void setInformation(Map<String, StockModel> stocks) {
        this.stocks = stocks;
    }

    /**
     * Conversion from Json to StockCollection Model.
     * @param jsonMessage the string message.
     * @return a stockCollection model.
     */
    public static StockCollectionModel fromJson(String jsonMessage) {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, StockCollectionModel.class);
    }
}
