package nl.rug.aoop.stocksserver.traders;

import lombok.Getter;

import java.util.List;

/**
 * Models a trader collection.
 */
@Getter
public class TraderCollection {
    private List<Trader> traders;
}
