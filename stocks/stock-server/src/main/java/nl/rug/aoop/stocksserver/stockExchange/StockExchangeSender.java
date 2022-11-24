package nl.rug.aoop.stocksserver.stockExchange;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.networking.client.Communicator;
import nl.rug.aoop.stocksserver.manager.ConnectionManager;
import nl.rug.aoop.stocksserver.stocks.StockCollection;
import nl.rug.aoop.stocksserver.traders.Trader;

import java.util.List;
import java.util.Map;

/**
 * Models a stock exchange sender.
 */
@Slf4j
public class StockExchangeSender implements InformationSender, Runnable {

    private Boolean running = false;

    private StockExchange stockExchange;
    private ConnectionManager connectionManager;
    private Map<String, Communicator> connectedTraders;

    /**
     * Constructor of stock exchange sender.
     * @param stockExchange the stock exchange.
     */
    public StockExchangeSender(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
        this.connectionManager = stockExchange.getConnectionManager();
        this.connectedTraders = stockExchange.getConnectionManager().getConnectedTraders();
    }

    /**
     * Continuously sends information about trader and stocks to a registered clients.
     */
    @Override
    public void sendInformation() {
        try {
            List<Trader> traders = stockExchange.getTraders().getTraders();
            StockCollection stockCollection = stockExchange.getStocks();

            for(Trader t : traders) {
                if(connectedTraders.containsKey(t.getId())) {

                    String stockInformation = stockCollection.toJson();
                    NetworkMessage stockInfo = new NetworkMessage("IncomingStockInfo", stockInformation);
                    log.debug("This is the stockInformation: " + stockInformation);
                    connectionManager.send(t.getId(), stockInfo);

                    String traderInformation = t.toJson();
                    NetworkMessage traderInfo = new NetworkMessage("IncomingTraderInfo", traderInformation);
                    log.debug("This is the traderInformation: " + traderInformation);
                    connectionManager.send(t.getId(), traderInfo);
                }
            }
        } catch (NullPointerException e) {
            log.info("There was an error when attempting to send the information.", e);
        }
    }

    /**
     * Run method that sends the information.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                sendInformation();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.info("There was an error when attempting to send the information.");
            }
        }
    }
}
