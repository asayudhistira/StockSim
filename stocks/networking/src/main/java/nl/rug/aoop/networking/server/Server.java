package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Models a server.
 * Implements Runnable.
 */
@Slf4j
public class Server implements Runnable {
    @Getter
    private boolean running = false;
    private int threadNr;
    private final ServerSocket serverSocket;
    private ExecutorService executorService;
    private MessageHandler messageHandler;
    private static final int TIMEOUT = 10000;
    @Getter
    private List<ClientHandler> clientsList = new ArrayList<>();

    /**
     * Constructor for a server.
     * @param port the port number.
     * @param messageHandler the message Handler.
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public Server(int port, MessageHandler messageHandler) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newCachedThreadPool();
        this.threadNr = 1;
        this.messageHandler = messageHandler;
    }

    /**
     * Run method for server.
     */
    @Override
    public void run() {
        running = true;
        log.info("Server starting on port: " + serverSocket.getLocalPort());
        while(running) {
            try {
                Socket socket = serverSocket.accept();
                // Whenever this line is reached, there is a certainty that a new Client is trying to connect.
                log.info("Spawning thread: " + threadNr);
                // We create a new thread so every client has its own thread. Hence, enabling multiple connections.
                // we pass the socket to enable the communication.
                ClientHandler clientHandler = new ClientHandler(socket, threadNr, messageHandler);
                clientsList.add(clientHandler);
                executorService.submit(clientHandler);
                threadNr++;
            } catch (IOException e) {
                log.error("Something went wrong with the client handler.");
            }
        }
    }

    /**
     * Terminates the server.
     * This method is here so if someone will need to control the state of the thread, the developer has a clean way
     * of doing it.
     */
    public void terminate() {
        running = false;
        executorService.shutdown();
        try {
            executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdownNow();
    }

    /**
     * Get port number of server.
     * @return the port number.
     */
    public int getPort() {
        return  serverSocket.getLocalPort();
    }

    /**
     * Getter for the number of client handler.
     * @return the number of client handler.
     */
    public int getNumClients() {
        return clientsList.size();
    }
}
