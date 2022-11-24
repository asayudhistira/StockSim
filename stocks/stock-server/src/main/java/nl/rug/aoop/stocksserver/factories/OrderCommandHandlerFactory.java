package nl.rug.aoop.stocksserver.factories;

import nl.rug.aoop.command.AbstractCommandHandlerFactory;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.command.MqPutCommand;
import nl.rug.aoop.messagequeue.messagequeues.MessageQueue;
import nl.rug.aoop.stocksserver.command.DeregisterCommand;
import nl.rug.aoop.stocksserver.command.RegisterCommand;
import nl.rug.aoop.stocksserver.stockExchange.StockExchange;
import nl.rug.aoop.stocksserver.command.BuyLimitCommand;
import nl.rug.aoop.stocksserver.command.SellLimitCommand;

/**
 * Models the order command handler factory.
 */
public class OrderCommandHandlerFactory implements AbstractCommandHandlerFactory {

    private final StockExchange stockExchange;
    private MessageQueue messageQueue;

    /**
     * Constructor for order command handler factory.
     * @param messageQueue the message queue where messages will arrive at.
     * @param stockExchange the stock exchange where trade will take place.
     */
    public OrderCommandHandlerFactory(MessageQueue messageQueue, StockExchange stockExchange) {
        this.stockExchange = stockExchange;
        this.messageQueue = messageQueue;
    }

    /**
     * Creates a command handler with multiple commands.
     * @return the command handler.
     */
    @Override
    public CommandHandler create() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.addCommand("BuyOrder", new BuyLimitCommand(stockExchange));
        commandHandler.addCommand("SellOrder", new SellLimitCommand(stockExchange));
        commandHandler.addCommand("Register", new RegisterCommand(stockExchange));
        commandHandler.addCommand("Deregister", new DeregisterCommand(stockExchange));
        commandHandler.addCommand("MqPut", new MqPutCommand(messageQueue));
        return commandHandler;
    }
}
