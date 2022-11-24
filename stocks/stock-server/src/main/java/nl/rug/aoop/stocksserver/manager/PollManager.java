package nl.rug.aoop.stocksserver.manager;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.consumer.MQConsumer;
import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.stocksserver.orders.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a poll manager.
 */
@Slf4j
public class PollManager implements Runnable {

    private MQConsumer localConsumer;
    private CommandHandler commandHandler;
    private Boolean running = false;

    /**
     * Constructor of a poll Manager.
     * @param localConsumer the local consumer who polls from the message queue.
     * @param commandHandler the command handler with the commands.
     */
    public PollManager(MQConsumer localConsumer, CommandHandler commandHandler) {
        this.localConsumer = localConsumer;
        this.commandHandler = commandHandler;
    }

    /**
     * Run method where message is polled continuously.
     * Create a hash map with the information from the body of the polled message.
     * executes a command depending on the message header.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Message message = localConsumer.poll();
                if (message != null) {
                    String header = message.getHeader();
                    String body = message.getBody();
                    Order order = Order.fromJson(body);

                    //log.debug("id : " + order.getId());
                    //log.debug("stock Symbol : " + order.getStockSymbol());
                    //log.debug("Number of shares : " + order.getNrShares());
                    //log.debug("Price : " + order.getPriceLimit());

                    Map<String, Object> options = new HashMap<>();
                    options.put("id", order.getId());
                    options.put("stockSymbol", order.getStockSymbol());
                    options.put("nrShares", order.getNrShares());
                    options.put("priceLimit", order.getPriceLimit());
                    //log.debug("values in the map : " + options.values());
                    commandHandler.executeCommand(header, options);
                }
            } catch (NullPointerException e) {
                log.info(e.getMessage(), e);
                terminate();
            }
        }
    }

    /**
     * Terminates the run method.
     */
    public void terminate() {
        running = false;
    }

}
