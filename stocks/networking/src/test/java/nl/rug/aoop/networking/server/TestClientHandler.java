package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doThrow;

/**
 * tests for Client Handler.
 */
@Slf4j
public class TestClientHandler {

    private static final int TIMEOUT = 1;
    private int serverPort;
    private Socket serverSideSocket = null;
    private boolean serverStarted = false;
    private String message;
    private String message2;
    private String message3;
    private String message4;

    /**
     * Starts a temporary server.
     */
    @BeforeEach
    public void setUp() {
        new Thread(() ->{
            try {
                ServerSocket s = new ServerSocket(0);
                serverPort = s.getLocalPort();
                serverStarted = true;
                serverSideSocket = s.accept();
            } catch (IOException e) {
                fail("Could not start server: " + e.getMessage());
            }
        }).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(() -> serverStarted);

        message  = "MQPut";
        message2 = "MQPoll";
        message3 = null;
        message4 = "";
    }

    /**
     * Test to see if single message are accepted by the client handler.
     * @throws IOException  if an I/O error occurs when creating the socket.
     */
    @Test
    public void TestRun() throws IOException {

        try (Socket socket = new Socket()) {
            MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
            InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
            socket.connect(address);
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(() -> serverSideSocket != null);
            ClientHandler clientHandler = new ClientHandler(serverSideSocket, 0, mockHandler);
            new Thread(clientHandler).start();
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(clientHandler::isRunning);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(message);
            await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
                Mockito.verify(mockHandler).handleMessage(message, clientHandler);
            });
        }
    }

    /**
     * Test to see if multiple message are accepted by the client handler.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    @Test
    public void TestRunMultiple() throws IOException {

        try (Socket socket = new Socket()) {
            MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
            InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
            socket.connect(address);
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(() -> serverSideSocket != null);
            ClientHandler clientHandler = new ClientHandler(serverSideSocket, 0, mockHandler);
            new Thread(clientHandler).start();
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(clientHandler::isRunning);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(message);
            out.println(message2);
            await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
                Mockito.verify(mockHandler).handleMessage(message, clientHandler);
                Mockito.verify(mockHandler).handleMessage(message2, clientHandler);
            });
        }
    }

    /**
     * Test to see if an IllegalArgumentException is thrown when message is null.
     * @throws IOException  if an I/O error occurs when creating the socket.
     */
    @Test
    public void TestRunNull() throws IOException {

        try (Socket socket = new Socket()) {
            MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
            InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
            socket.connect(address);
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(() -> serverSideSocket != null);
            ClientHandler clientHandler = new ClientHandler(serverSideSocket, 0, mockHandler);
            new Thread(clientHandler).start();
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(clientHandler::isRunning);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(message3);
            out.println(message4);
            await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
                doThrow(IllegalArgumentException.class).when(mockHandler).handleMessage(message3, clientHandler);
                doThrow(IllegalArgumentException.class).when(mockHandler).handleMessage(message4, clientHandler);
            });
        }
    }

    /**
     * Test to see if client handler terminates when BYE message is passed.
     * @throws IOException  if an I/O error occurs when creating the socket.
     */
    @Test
    public void TestTerminate() throws IOException{

        try (Socket socket = new Socket()) {
            MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
            InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
            socket.connect(address);
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(() -> serverSideSocket != null);
            ClientHandler clientHandler = new ClientHandler(serverSideSocket, 0, messageHandler);
            new Thread(clientHandler).start();
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(clientHandler::isRunning);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            assertTrue(clientHandler.isRunning());
            clientHandler.terminate();
            assertFalse(clientHandler.isRunning());
        }
    }
}
