package nl.rug.aoop.stockClient.traderCommands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.stockClient.stockMarket.StockCollectionModel;

import java.util.Map;

/**
 * Models a incoming stock information command.
 */
@Slf4j
public class IncomingStockInformationCommand implements Command {
    private StockCollectionModel stockModel;

    /**
     * Constructor for incoming stock information command.
     * @param stockModel the stock model.
     */
    public IncomingStockInformationCommand(StockCollectionModel stockModel) {
        this.stockModel = stockModel;
    }

    /**
     * Execute command that sets the changes in stock prices.
     * @param options the key value pairs.
     */
    @Override
    public void execute(Map<String, Object> options) {
        String message = (String) options.get("Body");
        //log.debug("This is the stock message :" + message);
        StockCollectionModel s = StockCollectionModel.fromJson(message);
        stockModel.setInformation(s.getStocks());
        //log.debug(Arrays.toString(s.getStocks().keySet().toArray()));
        //log.debug("This is the Stocks information received: " + s.getStocks().get("AAPL").getPrice());
    }
}
