package nl.rug.aoop.stockClient.traderCommands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.stockClient.traders.TraderModel;

import java.util.Map;

/**
 * Models an incoming trader information command.
 */
@Slf4j
public class IncomingTraderInformationCommand implements Command {
    private TraderModel traderModel;

    /**
     * Constructor for incoming trader information command.
     * @param traderModel the trader model.
     */
    public IncomingTraderInformationCommand(TraderModel traderModel) {
        //log.info("Entered the constructor of IncomingCommand");
        this.traderModel = traderModel;
    }

    /**
     * Execute command that sets the changes in trader info.
     * @param options the key value pairs.
     */
    @Override
    public void execute(Map<String, Object> options) {
        String message = (String) options.get("Body");
        //log.debug("This is the trader message :" + message);
        TraderModel p = TraderModel.fromJson(message);
        traderModel.setInformation(p.getId(), p.getName(), p.getFunds(), p.getOwnedShares());
    }
}
