package nl.rug.aoop.stockClient.factories;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.AbstractCommandHandlerFactory;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.stockClient.stockMarket.StockCollectionModel;
import nl.rug.aoop.stockClient.traderCommands.IncomingStockInformationCommand;
import nl.rug.aoop.stockClient.traderCommands.IncomingTraderInformationCommand;
import nl.rug.aoop.stockClient.traders.TraderModel;

/**
 * Models a Trader Command Handler factory.
 */
@Slf4j
public class TraderCommandHandlerFactory implements AbstractCommandHandlerFactory {
    private TraderModel traderModel;
    private StockCollectionModel stockModel;

    /**
     * Constructor of a trader command handler factory.
     * @param traderModel the trader information.
     * @param stockModel the stock information.
     */
    public TraderCommandHandlerFactory(TraderModel traderModel, StockCollectionModel stockModel) {
        this.traderModel = traderModel;
        this.stockModel = stockModel;
    }

    /**
     * Creates a command handler with the incoming information command.
     * @return a command handler.
     */
    @Override
    public CommandHandler create() {
        CommandHandler commandHandler = new CommandHandler();
        //log.debug("Entered create ");
        commandHandler.addCommand("IncomingTraderInfo", new IncomingTraderInformationCommand(traderModel));
        commandHandler.addCommand("IncomingStockInfo", new IncomingStockInformationCommand(stockModel));
        return commandHandler;
    }
}
