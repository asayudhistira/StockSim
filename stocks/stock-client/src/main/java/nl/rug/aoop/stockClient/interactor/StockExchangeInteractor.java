package nl.rug.aoop.stockClient.interactor;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.producers.NetworkProducer;
import nl.rug.aoop.stockClient.tradeStrategy.Strategy;

import java.util.*;

/**
 * Models a stock exchange interactor.
 */
@Slf4j
public class StockExchangeInteractor implements Interactor {
    private final String traderId;
    private NetworkProducer networkProducer;
    private Strategy strategy;

    /**
     * Constructor of stock exchange interactor.
     * @param strategy the strategy.
     * @param networkProducer the network producer.
     * @param traderId the trader's id.
     */
    public StockExchangeInteractor(Strategy strategy, NetworkProducer networkProducer, String traderId) {
        this.traderId = traderId;
        this.networkProducer = networkProducer;
        this.strategy = strategy;
    }

    /**
     * Places an order.
     */
    @Override
    public void placeOrder() {
        Message message = strategy.chooseBuyOrSellOrder();
        if (Objects.equals(message.getHeader(), "BuyOrder") || Objects.equals(message.getHeader(), "SellOrder")) {
            networkProducer.put(message);
        }
    }

    /**
     * Registers client to server.
     * @param message the message with botId as body.
     */
    @Override
    public void register(Message message) {
        networkProducer.register(message);
    }

    /**
     * De-registers client from server.
     * @param message the message with botId as body.
     */
    @Override
    public void deregister(Message message) {
        networkProducer.deregister(message);
    }
}
