package nl.rug.aoop.stockClient.traders;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.Map;

/**
 * Models a trader model.
 */
@Getter
public class TraderModel {
    private String id;
    private String name;
    private double funds;
    private Map<String, Integer> ownedShares;

    /**
     * Sets the infomation of a trader.
     * @param id the trader's id.
     * @param name the trader's name.
     * @param funds the trader's available funds.
     * @param ownedShares the trader's owned shares.
     */
    public void setInformation(String id, String name, double funds, Map<String, Integer> ownedShares) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.ownedShares = ownedShares;
    }

    /**
     * Converts a string to a trader model.
     * @param jsonMessage a json string.
     * @return a trader model.
     */
    public static TraderModel fromJson(String jsonMessage) {
        Gson gson = new Gson();
        return gson.fromJson(jsonMessage, TraderModel.class);
    }
}
