package nl.rug.aoop.stockClient.requests;

import com.google.gson.Gson;
import lombok.Getter;

/**
 * Models a request.
 */
@Getter
public class Request {

    private String id;
    private String stockSymbol;
    private long nrShares;
    private double priceLimit;

    /**
     * Constructor for request.
     * @param id the id.
     * @param stockSymbol the stock being traded.
     * @param nrShares the number of shares being traded.
     * @param priceLimit the price of the stock.
     */
    public Request(String id, String stockSymbol, long nrShares, double priceLimit) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.nrShares = nrShares;
        this.priceLimit = priceLimit;
    }

    /**
     * Conversion to json.
     * @return string with json information.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
