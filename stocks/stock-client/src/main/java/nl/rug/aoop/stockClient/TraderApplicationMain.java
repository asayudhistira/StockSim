package nl.rug.aoop.stockClient;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stockClient.traderBot.TraderBotManager;
import nl.rug.aoop.stockClient.traders.TraderIdCollection;
import nl.rug.aoop.stockClient.traders.TraderIdLoader;

import java.io.IOException;

/**
 * Models a trader application.
 */
@Slf4j
public class TraderApplicationMain {
    /**
     * Main function.
     * @param args arguments.
     * @throws IOException error starting up the trader application.
     */
    public static void main(String[] args) throws IOException {
        TraderIdLoader traderIdLoader = new TraderIdLoader();
        TraderIdCollection traderIdCollection= traderIdLoader.loadTraderIds();
        int numbBots = traderIdCollection.getTraderIds().size();
        TraderBotManager traderBotManager = new TraderBotManager(numbBots);
        traderBotManager.startBots();
    }
}
