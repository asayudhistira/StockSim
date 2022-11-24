package nl.rug.aoop.stocksserver.transactions;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;
import nl.rug.aoop.stocksserver.orders.BuyOrder;
import nl.rug.aoop.stocksserver.orders.SellOrder;
import nl.rug.aoop.stocksserver.traders.Trader;
import nl.rug.aoop.stocksserver.unresolvedOrders.Ask;
import nl.rug.aoop.stocksserver.unresolvedOrders.Bid;

import java.util.*;

import static java.lang.Math.min;

/**
 * Models a Transaction Manager.
 */
@Slf4j
public class TransactionManager {

    private final StockExchange stockExchange;
    private final Map<String,List<Transaction>> transactionHistory;
    private Ask asks;
    private Bid bids;
    private List<Trader> traders;

    /**
     * Constructor for Transaction Manager.
     * @param stockExchange the stock exchange.
     */
    public TransactionManager(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
        this.transactionHistory = new HashMap<>();
        this.asks = new Ask();
        this.bids = new Bid();
        this.traders = stockExchange.getTraders().getTraders();
    }

    /**
     * Update trader;s owned stock.
     * @param traderId the trader's id.
     * @param stockSymbol the stock symbol.
     * @param nrShares the number of shares.
     * @param isBuyOrder checks if it is a buy order or not.
     */
    public void updateTraderOwnedStocks(String traderId, String stockSymbol, long nrShares, boolean isBuyOrder) {
        for (Trader trader : traders) {
            if (Objects.equals(trader.getId(), traderId)) {
                if (isBuyOrder) {
                    for (String symbol : trader.getOwnedStocks()) {
                        if (Objects.equals(symbol, stockSymbol)) {
                            return;
                        }
                    }
                    trader.getOwnedShares().put(stockSymbol, nrShares);
                    return;
                }
                if (trader.getOwnedShares().get(stockSymbol) <= 0) {
                    trader.getOwnedShares().remove(stockSymbol);
                }
                return;
            }
        }
    }

    /**
     * Finalize a buy order. Polls the first element from the Asks and checks if it is a suitable purchase.
     * If it is, create a transaction. Once, it is no longer suitable, add the buy order into bids.
     * @param buyOrder the buy order.
     */
    public void finalizeBuyOrder(BuyOrder buyOrder) {
        String stockSymbol = buyOrder.getStockSymbol();
        long nrShares = buyOrder.getNrShares();
        double priceLimit = buyOrder.getPriceLimit();
        String traderId = buyOrder.getId();
        updateTraderFunds(traderId, nrShares, priceLimit, true);
        if (asks.getAsks().get(stockSymbol) == null) {
            bids.addUnsolvedBuyOrder(buyOrder);
            return;
        }

        while (buyOrder.getNrShares() > 0) {
            SellOrder sellOrder = asks.getAsks().get(stockSymbol).poll();
            if(sellOrder != null && sellOrder.getPriceLimit() <= buyOrder.getPriceLimit()) {
                Transaction transaction = newTransaction(buyOrder,sellOrder,sellOrder.getPriceLimit());
                buyOrder.setNrShares(buyOrder.getNrShares() - transaction.getNrShares());
                sellOrder.setNrShares(sellOrder.getNrShares() - transaction.getNrShares());
                updateTraderFunds(sellOrder.getId(), transaction.getNrShares(), transaction.getPrice(), false);
                updateTraderOwnedStocks(traderId, buyOrder.getStockSymbol(), nrShares, true);
                addTransaction(transaction);
            } else {
                bids.addUnsolvedBuyOrder(buyOrder);
                buyOrder.setNrShares(0);
            }
            if(sellOrder != null && sellOrder.getNrShares() != 0) {
                asks.addUnsolvedSellOrder(sellOrder);
            }
        }
    }

    /**
     * Updates the trader funds.
     * @param traderId id of the trader that placed the order.
     * @param nrShares nrShares specified in the order.
     * @param price price limit of the buy or sell order.
     * @param isBuyOrder flag that signals if an order is either a sell or a buy order.
     */
    public void updateTraderFunds(String traderId, long nrShares, double price, boolean isBuyOrder) {
        for (Trader trader : traders) {
            if (Objects.equals(trader.getId(), traderId)) {
                if (isBuyOrder) {
                    trader.setFunds(trader.getFunds() - nrShares * price);
                    return;
                }
                trader.setFunds(trader.getFunds() + nrShares * price);
            }
        }
    }

    /**
     * Finalize a sell order. Polls the first element from the bids and checks if it is a suitable purchase.
     * If it is, create a transaction. Once, it is no longer suitable, add the buy order into asks.
     * @param sellOrder the sell order.
     */
    public void finalizeSellOrder(SellOrder sellOrder) {
        String stockSymbol = sellOrder.getStockSymbol();
        long nrShares = sellOrder.getNrShares();
        double priceLimit = sellOrder.getPriceLimit();
        String traderId = sellOrder.getId();
        if (bids.getBids().get(stockSymbol) == null) {
            asks.addUnsolvedSellOrder(sellOrder);
            sellOrder.setNrShares(0);
        }

        while (sellOrder.getNrShares() > 0) {
            BuyOrder buyOrder = bids.getBids().get(stockSymbol).poll();
            if (buyOrder != null && sellOrder.getPriceLimit() <= buyOrder.getPriceLimit()) {
                Transaction transaction = newTransaction(buyOrder, sellOrder, buyOrder.getPriceLimit());
                buyOrder.setNrShares(buyOrder.getNrShares() - transaction.getNrShares());
                sellOrder.setNrShares(sellOrder.getNrShares() - transaction.getNrShares());
                updateTraderFunds(traderId, nrShares, priceLimit, false);
                updateTraderOwnedStocks(traderId, sellOrder.getStockSymbol(), nrShares, true);
                addTransaction(transaction);
            } else {
                asks.addUnsolvedSellOrder(sellOrder);
                sellOrder.setNrShares(0);
            }

            if (buyOrder !=  null && buyOrder.getNrShares() != 0 ) {
                bids.addUnsolvedBuyOrder(buyOrder);
            }
        }
    }

    /**
     * Adds a transaction into a transaction history.
     * @param transaction the transaction to be added.
     */
    public void addTransaction(Transaction transaction) {
        //log.debug("Transaction is added");

        transactionHistory.computeIfAbsent(transaction.getBuyerId(), a -> new ArrayList<>());
        transactionHistory.computeIfAbsent(transaction.getSellerId(), a -> new ArrayList<>());

        if(!Objects.equals(transaction.getBuyerId(), transaction.getSellerId())) {
            transactionHistory.get(transaction.getBuyerId()).add(transaction);
            transactionHistory.get(transaction.getSellerId()).add(transaction);
            stockExchange.updatePrice(transaction.getStockSymbol(), transaction.getPrice());
        }
    }

    /**
     * Creates a new transaction and adds the transaction into the transaction history.
     * @param buyOrder the buy order.
     * @param sellOrder the sell order.
     * @param priceLimit the price.
     * @return a transaction.
     */
    public Transaction newTransaction(BuyOrder buyOrder, SellOrder sellOrder, double priceLimit) {
        String buyerId = buyOrder.getId();
        String sellerId = sellOrder.getId();
        String stockSymbol = buyOrder.getStockSymbol();
        long nrShares = min(buyOrder.getNrShares(), sellOrder.getNrShares());

        return new Transaction(buyerId, sellerId, stockSymbol, nrShares, priceLimit);
    }
}
