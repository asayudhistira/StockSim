module messagequeue {
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires com.google.gson;
    opens nl.rug.aoop.messagequeue.producers to com.google.gson;
    opens nl.rug.aoop.messagequeue.consumer to com.google.gson;
    opens nl.rug.aoop.messagequeue.messagequeues to com.google.gson;
    opens nl.rug.aoop.messagequeue.messagemodel to com.google.gson;
    opens nl.rug.aoop.messagequeue.messagehandlers to com.google.gson;
    exports nl.rug.aoop.messagequeue.messagequeues;
    exports nl.rug.aoop.messagequeue.messagehandlers;
    exports nl.rug.aoop.messagequeue.factories;
    exports nl.rug.aoop.messagequeue.producers;
    exports nl.rug.aoop.messagequeue.messagemodel;
    exports nl.rug.aoop.messagequeue.consumer;
    exports nl.rug.aoop.messagequeue.command;
    requires networking;
    requires command;
}