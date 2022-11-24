package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Models a client.
 * Implements Runnable and Communicator interface.
 */
@Slf4j
public class Client implements Runnable, Communicator {

    private static final int TIMEOUT = 10000;
    private Socket socket;
    @Getter
    private boolean connected;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * This running variable allows for controlling the state of the thread.
     */
    @Getter
    private boolean running = false;
    private final MessageHandler messageHandler;

    /**
     * Constructor for client.
     * @param address the IP address and port number.
     * @param messageHandler the message handler.
     * @throws IOException if an error occurs during the connection.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        this.initSocket(address);
        this.connected = true;
    }

    /**
     * Initializing the socket.
     * @param address the IP address and port number.
     * @throws IOException if an error occurs during the connection
     */
    private void initSocket(InetSocketAddress address) throws IOException {
        this.socket = new Socket();
        socket.connect(address, TIMEOUT);
        if(!socket.isConnected()) {
            throw new IOException("Socket is not connected to the server");
        }
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Runs a client.
     */
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                //log.debug("Running Client");
                String incomingMessage = in.readLine();
                //log.debug(incomingMessage);
                if (incomingMessage == null || incomingMessage.equals("")) {
                    terminate();
                    break;
                }
                messageHandler.handleMessage(incomingMessage, this);

            } catch (IOException e) {
                log.error("Unable to receive message " + e.getMessage());
            }
        }
    }

    /**
     * Sends a message.
     * @param message the message.
     * @throws IllegalArgumentException null message.
     */
    @Override
    public void send(String message) throws IllegalArgumentException {
        if (message == null || message.equals("")) {
            throw new IllegalArgumentException("Attempting to send an invalid message.");
        }
        out.println(message);
    }

    /**
     * Terminates the client.
     */
    @Override
    public void terminate() {
        log.info("Attempting to terminate client  ");
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Cannot close the sockets.", e);
        }
    }

}
