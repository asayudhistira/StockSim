package nl.rug.aoop.stocksserver.unresolvedOrders;

import lombok.Getter;
import nl.rug.aoop.stocksserver.orders.BuyOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Models a bid.
 */
public class Bid {

    @Getter
    private Map<String, Queue<BuyOrder>> bids;

    /**
     * Constructor for bids.
     */
    public Bid() {
        this.bids = new HashMap<>();
    }

    /**
     * Adds an unsolved Buy Order into bids.
     * @param unsolvedOrder unsolved buy order.
     */
    public void addUnsolvedBuyOrder(BuyOrder unsolvedOrder) {
        if (!bids.containsKey(unsolvedOrder.getStockSymbol())) {
            Queue<BuyOrder> values = new PriorityQueue<>();
            values.add(unsolvedOrder);
            bids.put(unsolvedOrder.getStockSymbol(), values);
        } else {
            bids.get(unsolvedOrder.getStockSymbol()).add(unsolvedOrder);
        }
    }
}