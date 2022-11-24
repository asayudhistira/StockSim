package nl.rug.aoop.stocksserver.stockExchange;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.AbstractCommandHandlerFactory;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.consumer.LocalConsumer;
import nl.rug.aoop.messagequeue.consumer.MQConsumer;
import nl.rug.aoop.messagequeue.messagehandlers.MessageHandlerImplementation;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;
import nl.rug.aoop.messagequeue.messagequeues.PriorityBlockingMessageQueue;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.stocksserver.manager.PollManager;
import nl.rug.aoop.stocksserver.factories.OrderCommandHandlerFactory;
import nl.rug.aoop.stocksserver.manager.ConnectionManager;
import nl.rug.aoop.stocksserver.stocks.StockCollection;
import nl.rug.aoop.stocksserver.stocks.StockLoader;
import nl.rug.aoop.stocksserver.stocks.stocksSymbols.StockSymbolLoader;
import nl.rug.aoop.stocksserver.stocks.stocksSymbols.StockSymbolsCollection;
import nl.rug.aoop.stocksserver.traders.TraderCollection;
import nl.rug.aoop.stocksserver.traders.TraderLoader;
import nl.rug.aoop.stocksserver.transactions.TransactionManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Models a stock exchange.
 */
@Slf4j
@Getter
public class StockExchange implements StockExchangeDataModel {
    /**
     * Defining the number of threads.
     */
    public static final int NUM_THREADS = 2;
    private static StockExchange stockExchangeInstance;
    private static final int PORT = Integer.parseInt(System.getenv("STOCK_EXCHANGE_PORT"));
    private static final int PREDEFINED_PORT = 8080;

    static {
        try {
            stockExchangeInstance = new StockExchange();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StockCollection stocks;
    private StockLoader stockLoader;
    private StockSymbolsCollection stockSymbols;
    private StockSymbolLoader stockSymbolLoader;
    private TraderCollection traders;
    private TraderLoader traderLoader;
    private Server server;
    private AbstractCommandHandlerFactory factory;
    private CommandHandler commandHandler;
    private MessageHandler messageHandler;
    private MQConsumer localConsumer;
    private MessageQueue messageQueue;

    private final TransactionManager transactionManager;
    private final ConnectionManager connectionManager;
    private final StockExchangeSender stockExchangeSender;
    private final PollManager pollManager;
    private ExecutorService executorService;

    /**
     * Constructor of stock exchange.
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    private StockExchange() throws IOException {
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
        messageQueue = new PriorityBlockingMessageQueue();
        localConsumer = new LocalConsumer(messageQueue);
        factory = new OrderCommandHandlerFactory(messageQueue,this);
        commandHandler = factory.create();
        messageHandler = new MessageHandlerImplementation(commandHandler);
        if (!checkConnectionDetails()) {
            server = new Server(PREDEFINED_PORT, messageHandler);
        }
        server = new Server(PORT, messageHandler);
        loadStockAndTraderInformation();
        transactionManager = new TransactionManager(this);
        connectionManager = new ConnectionManager(this);
        pollManager = new PollManager(localConsumer, commandHandler);
        stockExchangeSender = new StockExchangeSender(this);
        manageThreads();
    }

    /**
     * Get instance of stock Exchange.
     * @return an instance of stock exchange.
     */
    public static StockExchange getInstance() {
        return stockExchangeInstance;
    }

    /**
     * Loads stock information.
     * @throws IOException error when loading.
     */
    public void loadStock() throws IOException {
        stocks = stockLoader.loadStock();
        for (String stockSymbol : stockSymbols.getStockSymbols()) {
            updatePrice(stockSymbol, stocks.getStocks().get(stockSymbol).getInitialPrice());
        }
    }

    /**
     * Loads stock symbols information.
     * @throws IOException error when loading.
     */
    public void loadSymbols() throws IOException {
        stockSymbols = stockSymbolLoader.loadSymbols();
    }

    /**
     * Loads trader information.
     * @throws IOException error when loading.
     */
    public void loadTrader() throws IOException {
        traders = traderLoader.loadTraders();
    }

    /**
     * Updates the price in the stock exchange.
     * @param stock the specific stock.
     * @param price the new price.
     */
    public void updatePrice(String stock, double price) {
        stocks.getStocks().get(stock).setPrice(price);
    }


    /**
     * Gets the stock Information of a particular stock by index.
     * @param index The index of the stock that should be accessed.
     * @return information about the stock.
     */
    @Override
    public StockDataModel getStockByIndex(int index) {
        return stocks.getStocks().get(stockSymbols.getStockSymbols().get(index));
    }

    /**
     * Get number of stocks.
     * @return the number of stocks that are traded in the stock exchange.
     */
    @Override
    public int getNumberOfStocks() {
        return stocks.getStocks().size();
    }

    /**
     * Gets the information of a trader by the index.
     * @param index The index of the trader that should be accessed.
     * @return information about the trader.
     */
    @Override
    public TraderDataModel getTraderByIndex(int index) {
        return traders.getTraders().get(index);
    }

    /**
     * get number of traders.
     * @return the number of traders in the stock exchange.
     */
    @Override
    public int getNumberOfTraders() {
        return traders.getTraders().size();
    }

    /**
     * If there is an error getting the values from the environment variable,
     * the app will still run on a predefined port.
     * @return tells the developer whether the predefined port will be used.
     */
    private boolean checkConnectionDetails() {
        return System.getenv("STOCK_EXCHANGE_PORT") != null;
    }

    /**
     * Method that loads stock and trader information from a YAML file.
     */
    private void loadStockAndTraderInformation() {
        stockLoader = new StockLoader();
        traderLoader = new TraderLoader();
        stockSymbolLoader = new StockSymbolLoader();
        try {
            loadSymbols();
            loadStock();
            loadTrader();
        } catch (IOException e) {
            log.error("Could not load the stocks from the stocks.yml file \n" + e);
        }
    }

    /**
     * Method that manages the threads of the stock exchange and Starts a new server.
     */
    private void manageThreads() {
        executorService.submit(stockExchangeSender);
        executorService.submit(pollManager);
        new Thread(server).start();
    }
}
