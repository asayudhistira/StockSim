package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Communicator;
import nl.rug.aoop.networking.client.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a Client Handler.
 * Implements Runnable and Communicator.
 */
@Slf4j
public class ClientHandler implements Runnable, Communicator {
    /**
     * The socket can be final because the socket is not going to change.
     * Once the client is disconnected, the socket is disconnected.
     */
    private final Socket socket;
    private final int threadNr;

    private PrintWriter out;
    private BufferedReader in;
    @Getter
    private List<String> messages;
    @Getter
    private boolean running = false;
    private final MessageHandler messageHandler;

    /**
     * Constructor for client handler.
     * @param socket the socket.
     * @param threadNr the thread number.
     * @param messageHandler the message handler.
     * @throws IOException if an I/O error occurs when creating the input stream, the socket is closed, the socket
     * is not connected, or the socket input has been shutdown using shutdownInput() or when creating the output stream
     * or if the socket is not connected.
     */
    public ClientHandler(Socket socket, int threadNr, MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        this.socket = socket;
        this.threadNr = threadNr;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.messages = new ArrayList<>();
    }

    /**
     * Run method for client handler.
     */
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                String line = in.readLine();
                //System.out.println(line);
                if(line == null || line.equals("")) {
                    terminate();
                    break;
                }
                messageHandler.handleMessage(line, this);
                //log.debug("test" + line);
                //log.info("Echoed to thread " + threadNr + ": " + line);
            } catch (IOException e) {
                log.error("Could not read line.");
            }
        }
    }

    /**
     * Send a message.
     * @param message the message.
     */
    @Override
    public void send(String message) {
        if (message == null || message.equals("")) {
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }
        out.println(message);
    }

    /**
     * Terminates the client handler.
     */
    @Override
    public void terminate() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Cannot successfully close socket.");
        }
    }
}
