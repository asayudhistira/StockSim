package nl.rug.aoop.stocksserver.stocks;

import lombok.Getter;
import nl.rug.aoop.model.StockDataModel;

/**
 * Models a stock.
 */
@Getter
public class Stock implements StockDataModel {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double initialPrice;
    private double price;
    private double marketCap;

    /**
     * Sets the price of a stock.
     * @param price the new price.
     */
    public void setPrice(double price) {
        this.price = price;
        setMarketCap();
    }

    /**
     * Update the market cap.
     */
    public void setMarketCap() {
        this.marketCap = sharesOutstanding * price;
    }
}
