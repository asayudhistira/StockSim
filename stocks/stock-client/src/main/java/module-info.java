module stock.client {

    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires stock.market.ui;
    requires util;
    requires stock.server;
    requires messagequeue;
    requires networking;
    requires command;
    requires com.google.gson;
    opens nl.rug.aoop.stockClient.traders to com.fasterxml.jackson.databind, com.google.gson;
    opens nl.rug.aoop.stockClient.requests to com.google.gson;
    opens nl.rug.aoop.stockClient.stockMarket to com.google.gson;
    exports nl.rug.aoop.stockClient.traders to com.fasterxml.jackson.databind;
    //exports nl.rug.aoop.models to com.fasterxml.jackson.databind;
    //opens nl.rug.aoop.models to com.fasterxml.jackson.databind, com.google.gson;
    //exports nl.rug.aoop.models.trader to com.fasterxml.jackson.databind;
    //opens nl.rug.aoop.models.trader to com.fasterxml.jackson.databind, com.google.gson;
}