package nl.rug.aoop.stocksserver.orders;

import com.google.gson.Gson;
import lombok.Getter;

/**
 * Models an order.
 */
@Getter
public class Order {

    private String stockSymbol;
    private String id;
    private long nrShares;
    private double priceLimit;

    /**
     * Conversion from Json to an Order.
     * @param jsonMessage the json message.
     * @return an Order.
     */
    public static Order fromJson(String jsonMessage) {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, Order.class);
    }
}
