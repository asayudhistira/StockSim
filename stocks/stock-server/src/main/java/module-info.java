module stock.server {
    //exports nl.rug.aoop.stocksserver.stockExchange to stock.client;
    exports nl.rug.aoop.stocksserver.traders to stock.client;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    opens nl.rug.aoop.stocksserver.stocks to com.fasterxml.jackson.databind, com.google.gson;
    opens nl.rug.aoop.stocksserver.stocks.stocksSymbols to com.fasterxml.jackson.databind;
    opens nl.rug.aoop.stocksserver.traders to com.fasterxml.jackson.databind, com.google.gson;
    opens nl.rug.aoop.stocksserver.orders to com.google.gson;
    exports nl.rug.aoop.stocksserver.manager to stock.client;
    exports nl.rug.aoop.stocksserver.stocks;
    requires command;
    requires stock.market.ui;
    requires util;
    requires messagequeue;
    requires networking;
    requires com.google.gson;
}