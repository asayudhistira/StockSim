package nl.rug.aoop.stocksserver.unresolvedOrders;

import lombok.Getter;
import nl.rug.aoop.stocksserver.orders.SellOrder;

import java.util.*;

/**
 * Models an Ask.
 */
public class Ask {

    @Getter
    private Map<String, Queue<SellOrder>> asks;

    /**
     * Constructor for Asks.
     */
    public Ask() {
        this.asks = new HashMap<>();
    }

    /**
     * Adds an unsolved Sell Order into the asks.
     * @param unsolvedOrder the unsolved order.
     */
    public void addUnsolvedSellOrder(SellOrder unsolvedOrder) {
        if (!asks.containsKey(unsolvedOrder.getStockSymbol())) {
            Queue<SellOrder> values = new PriorityQueue<>();
            values.add(unsolvedOrder);
            asks.put(unsolvedOrder.getStockSymbol(), values);
        } else {
            asks.get(unsolvedOrder.getStockSymbol()).add(unsolvedOrder);
        }
    }
}