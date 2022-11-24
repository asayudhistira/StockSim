package nl.rug.aoop.stocksserver.orders;

import lombok.Getter;
import lombok.Setter;

/**
 * models a sell order.
 */
@Getter
public class SellOrder extends Order implements Comparable<SellOrder> {

    private String stockSymbol;
    private String id;
    @Setter
    private long nrShares;
    private double priceLimit;

    /**
     * Constructor for sell order.
     * @param id the seller id.
     * @param stockSymbol the stock symbol.
     * @param nrShares the number of shares.
     * @param priceLimit the price limit.
     */
    public SellOrder(String id, String stockSymbol, long nrShares, double priceLimit) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.nrShares = nrShares;
        this.priceLimit = priceLimit;
    }

    /**
     * Comparable, to compare the values of price in each order.
     * @param o the other buy order.
     * @return 1, 0 or -1 depending on the price limit value.
     */
    @Override
    public int compareTo(SellOrder o) {
        if (this.getPriceLimit() > o.getPriceLimit()) {
            return 1;
        } else if (this.getPriceLimit() == o.getPriceLimit()) {
            return 0;
        } else {
            return -1;
        }
    }
}
