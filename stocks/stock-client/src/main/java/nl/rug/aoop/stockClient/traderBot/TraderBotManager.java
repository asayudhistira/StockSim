package nl.rug.aoop.stockClient.traderBot;

import nl.rug.aoop.command.AbstractCommandHandlerFactory;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.messagehandlers.MessageHandlerImplementation;
import nl.rug.aoop.messagequeue.producers.NetworkProducer;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.stockClient.factories.TraderCommandHandlerFactory;
import nl.rug.aoop.stockClient.interactor.Interactor;
import nl.rug.aoop.stockClient.interactor.StockExchangeInteractor;
import nl.rug.aoop.stockClient.stockMarket.StockCollectionModel;
import nl.rug.aoop.stockClient.tradeStrategy.Strategy;
import nl.rug.aoop.stockClient.traders.TraderIdCollection;
import nl.rug.aoop.stockClient.traders.TraderIdLoader;
import nl.rug.aoop.stockClient.traders.TraderModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Models a trader bot manager.
 */
public class TraderBotManager {

    private ExecutorService executorService;
    private TraderBot traderBot;
    private int numberBots;
    private static final int PORT = Integer.parseInt(System.getenv("STOCK_EXCHANGE_PORT"));
    private static final int PREDEFINED_PORT = 8080;
    private static final String HOST = System.getenv("STOCK_EXCHANGE_HOST");
    private static final String PREDEFINED_HOST = "localhost";

    /**
     * Constructor for trader bot manager.
     * @param numberBots the number of bots needed.
     */
    public TraderBotManager(int numberBots) {
        this.numberBots = numberBots;
        this.executorService = Executors.newFixedThreadPool(numberBots);
    }

    /**
     * Starts all the bots.
     * @throws IOException error when starting up the bots.
     */
    public void startBots() throws IOException {
        TraderIdLoader traderIdLoader = new TraderIdLoader();
        TraderIdCollection traderIdCollection= traderIdLoader.loadTraderIds();
        for(String id : traderIdCollection.getTraderIds()) {
            TraderModel traderModel = new TraderModel();
            StockCollectionModel stockModel = new StockCollectionModel();
            AbstractCommandHandlerFactory factory = new TraderCommandHandlerFactory(traderModel, stockModel);
            CommandHandler commandHandler = factory.create();
            MessageHandler messageHandler = new MessageHandlerImplementation(commandHandler);
            Client client;
            if (checkHost() && checkPort()) {
                client = new Client(new InetSocketAddress(HOST,PORT), messageHandler);
            } else {
                client = new Client(new InetSocketAddress(PREDEFINED_HOST,PREDEFINED_PORT), messageHandler);
            }
            NetworkProducer producer = new NetworkProducer(client);
            Strategy strategy = new Strategy(traderModel, stockModel);
            Interactor interactor = new StockExchangeInteractor(strategy, producer, id);
            traderBot = new TraderBot(id, interactor);
            executorService.submit(traderBot);
            new Thread(client).start();
        }
    }

    /**
     * If there is an error getting the values from the environment variable,
     * the app will still run on a predefined host.
     * @return tells the developer whether a predefined host will be used.
     */
    private boolean checkHost() {
        return HOST != null;
    }

    /**
     * If there is an error getting the values from the environment variable,
     * the app will still run on a predefined port.
     * @return tells the developer whether a predefined port will be used.
     */
    private boolean checkPort() {
        return System.getenv("STOCK_EXCHANGE_PORT") != null;
    }
}
