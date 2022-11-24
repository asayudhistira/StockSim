package nl.rug.aoop.stockClient.stockMarket;

import lombok.Getter;

/**
 * Models a stock model.
 */
@Getter
public class StockModel {

    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double initialPrice;
    private double price;
    private double marketCap;
}
