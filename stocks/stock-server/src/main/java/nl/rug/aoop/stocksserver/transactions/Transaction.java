package nl.rug.aoop.stocksserver.transactions;

import lombok.Getter;

/**
 * Models a Transaction.
 */
@Getter
public class Transaction {
    private String buyerId;
    private String sellerId;
    private String stockSymbol;
    private long nrShares;
    private double price;

    /**
     * Constructor for transaction.
     * @param buyerId buyer's Id.
     * @param sellerId seller's id.
     * @param stockSymbol the stock being traded.
     * @param nrShares the amount of stocks traded.
     * @param price the price of the stock traded at.
     */
    public Transaction(String buyerId, String sellerId, String stockSymbol, long nrShares, double price) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.stockSymbol = stockSymbol;
        this.nrShares = nrShares;
        this.price = price;
    }
}
