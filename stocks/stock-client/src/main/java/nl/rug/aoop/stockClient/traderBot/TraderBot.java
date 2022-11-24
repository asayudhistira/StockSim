package nl.rug.aoop.stockClient.traderBot;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.stockClient.interactor.Interactor;
import nl.rug.aoop.messagequeue.messagemodel.Message;

import java.util.Random;

/**
 * Models a trader bot.
 */
@Slf4j
public class TraderBot implements Runnable {

    private static final int SLEEP_MIN = 1;
    private static final int SLEEP_MAX = 4;
    private Boolean running = false;
    private String botId;
    private Interactor interactor;

    /**
     * Constructor for trader bot.
     * @param botId the bot id.
     * @param interactor the interactor.
     */
    public TraderBot(String botId, Interactor interactor) {
        this.botId = botId;
        this.interactor = interactor;
    }

    /**
     * Run method which first registers a bot, then creates orders.
     */
    @Override
    public void run() {
        running = true;
        interactor.register(new Message("Register", botId));
        while (running) {
            try {
                interactor.placeOrder();
            } catch (NullPointerException e) {
                log.error(String.valueOf(e), e);
                interactor.deregister(new Message("Deregister", botId));
                terminate();
            }
            sleepOneToFourSeconds();
        }
    }

    /**
     * Terminates the run method.
     */
    public void terminate() {
        running = false;
    }

    /**
     * Function to sleep the thread between 1 and 4 seconds.
     */
    private void sleepOneToFourSeconds() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt((SLEEP_MAX - SLEEP_MIN) + 1) + SLEEP_MIN * 1000);
        } catch (InterruptedException e) {
            log.error("Error trying to sleep bots");
        }
    }
}
