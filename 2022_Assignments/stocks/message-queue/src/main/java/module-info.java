module messagequeue {
    // Needed for gson to work. If your message queue resides in a sub-package,
    // be sure to open this to com.google.gson as well.
    //    opens nl.rug.aoop.messagequeue to com.google.gson;
    requires static lombok;
    requires org.slf4j;
    requires org.mockito;
    requires com.google.gson;
}