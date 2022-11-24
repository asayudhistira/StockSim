package nl.rug.aoop.stockClient.tradeStrategy;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.stockClient.requests.Request;
import nl.rug.aoop.stockClient.stockMarket.StockCollectionModel;
import nl.rug.aoop.stockClient.traders.TraderModel;

import java.util.List;
import java.util.Random;

/**
 * Models a strategy.
 */
public class Strategy {

    private static final double PRICE_DIFFERENCE = 5.0;
    private static final int FUND_DIVISOR = 10;
    private TraderModel traderModel;
    private StockCollectionModel stockCollectionModel;

    /**
     * Constructor of startegy.
     * @param traderModel the trader model.
     * @param stockModel the stock model.
     */
    public Strategy(TraderModel traderModel, StockCollectionModel stockModel) {
        this.traderModel = traderModel;
        this.stockCollectionModel = stockModel;
    }

    /**
     * Randomly choose between a buy order or sell order.
     * @return a message from the result of a buy and sell order.
     */
    public Message chooseBuyOrSellOrder() {
        Random r = new Random();
        if(r.nextInt(2) == 0) {
            return createBuyOrder();
        }
        return createSellOrder();
    }

    /**
     * Creates a random buy order.
     * @return a message with BuyOrder as header and a request as the body.
     */
    private Message createBuyOrder() {
        Random r = new Random();
        if(stockCollectionModel.getStocks() != null && traderModel != null) {
            String traderId = traderModel.getId();
            int stockCollectionSize = stockCollectionModel.getStocks().size();
            int randomStockNumber = r.nextInt((stockCollectionSize));
            List<String> stockLists = stockCollectionModel.getStocks().keySet().stream().toList();
            // I have the stock I want to buy.
            String stockSymbol = stockLists.get(randomStockNumber);

            //Get the price of the stock and randomize + 5
            double currentPrice = stockCollectionModel.getStocks().get(stockSymbol).getPrice();
            double upperPriceLimit = currentPrice + PRICE_DIFFERENCE;
            double priceLimit = r.nextDouble((upperPriceLimit - currentPrice)+ 1) + currentPrice;

            //get max amount of shares
            double funds = traderModel.getFunds()/FUND_DIVISOR;
            long maxShares = (long) (funds/priceLimit);

            if( maxShares != 0) {
                long nrShares = r.nextLong(maxShares) + 1;
                Request request = new Request(traderId, stockSymbol, nrShares, priceLimit);
                return new Message("BuyOrder", request.toJson());
            }
            return new Message("FailOrder", "Trader has insufficient funds.");
        }
        return new Message("FailOrder", "problem with Trader or Stocks");
    }

    /**
     * Creates a sell order.
     * @return a message with SellOrder as header and a request as the body.
     */
    private Message createSellOrder() {
        Random r = new Random();
        if (stockCollectionModel.getStocks() != null && traderModel.getOwnedShares() != null) {
            String traderId = traderModel.getId();

            int traderStockSize = traderModel.getOwnedShares().size();
            if(traderStockSize == 0) {
                return new Message("FailOrder", "Trader has no stocks to sell.");
            }

            int randomStockNumber = r.nextInt(traderStockSize);
            List<String> traderOwnedStock = traderModel.getOwnedShares().keySet().stream().toList();
            String stockSymbol = traderOwnedStock.get(randomStockNumber);

            double currentPrice = stockCollectionModel.getStocks().get(stockSymbol).getPrice();
            double lowerPriceLimit = currentPrice - PRICE_DIFFERENCE;
            double priceLimit = r.nextDouble((currentPrice - lowerPriceLimit)+ 1) + lowerPriceLimit;

            long nrShares = traderModel.getOwnedShares().get(stockSymbol);
            long randomNrShares = r.nextLong(nrShares) + 1;

            Request request = new Request(traderId, stockSymbol, randomNrShares, priceLimit);
            return new Message("SellOrder", request.toJson());
        }
        return new Message("FailOrder", "problem with Trader or Stocks");
    }

}
