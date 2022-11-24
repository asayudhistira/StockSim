package nl.rug.aoop.stocksserver.orders;

import lombok.Getter;
import lombok.Setter;

/**
 * Models a buy order.
 */
@Getter
public class BuyOrder extends Order implements Comparable<BuyOrder> {

    private String stockSymbol;
    private String id;
    @Setter
    private long nrShares;
    private double priceLimit;

    /**
     * Constructor for buy Order.
     * @param id the buyer id.
     * @param stockSymbol the stock symbol.
     * @param nrShares the number of shares.
     * @param priceLimit the price limit.
     */
    public BuyOrder(String id, String stockSymbol, long nrShares, double priceLimit) {
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
    public int compareTo(BuyOrder o) {
        if (this.getPriceLimit() < o.getPriceLimit()) {
            return 1;
        } else if (this.getPriceLimit() == o.getPriceLimit()) {
            return 0;
        } else {
            return -1;
        }
    }
}
