package nl.rug.aoop.stocksserver.stockExchange;

/**
 * Interface for sending the information from the StockExchangeApplication to the TraderApplication.
 */
public interface InformationSender {
    /**
     * Sends information to client.
     */
    void sendInformation();
}
