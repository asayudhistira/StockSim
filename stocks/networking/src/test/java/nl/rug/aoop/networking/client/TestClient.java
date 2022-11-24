package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

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
import static org.mockito.Mockito.*;

/**
 * Tests for Client.
 */
@Slf4j
public class TestClient {

    private static final int TIMEOUT = 1;
    private int serverPort;
    private boolean serverStarted = false;
    private PrintWriter serverSideOut;
    private BufferedReader serverSideIn;
    private Client client;
    private InetSocketAddress address;
    private MessageHandler mockHandler;

    /**
     * Starts a temporary server.
     * @throws IOException if the socket is not connected to the server.
     */
    @BeforeEach
    public void setUp() throws IOException{
        new Thread(() ->{
            try {
                ServerSocket s = new ServerSocket(0);
                serverPort = s.getLocalPort();
                serverStarted = true;

                Socket serverSideSocket = s.accept();
                serverSideOut = new PrintWriter(serverSideSocket.getOutputStream());
                serverSideIn = new BufferedReader(new InputStreamReader(serverSideSocket.getInputStream()));
                log.info("client accepted");
            } catch (IOException e) {
                fail("Could not start server: " + e.getMessage());
            }
        }).start();

        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(() -> serverStarted);

        address = new InetSocketAddress("localhost", serverPort);
        mockHandler = Mockito.mock(MessageHandler.class);
        client = new Client(address,mockHandler);
    }

    /**
     * Test to see if the client is connected to the temporary server.
     */
    @Test
    public void TestConstructorWithServer() {
        assertTrue(client.isConnected());
    }

    /**
     * Test to see if a client is running and a single message is sent.
     */
    @Test
    public void TestRun() {

        new Thread(client).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());

        String message = "This is a test message";
        serverSideOut.println(message);
        serverSideOut.flush();

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockHandler).handleMessage(message, client);
        });
    }

    /**
     * Test to see if a client is running and multiple message is sent.
     */
    @Test
    public void TestRunMultipleMessages() {

        new Thread(client).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());

        String message = "This is a test message";
        String message2 = "This is also a test message.";
        serverSideOut.println(message);
        serverSideOut.println(message2);
        serverSideOut.flush();

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockHandler).handleMessage(message, client);
            Mockito.verify(mockHandler).handleMessage(message2, client);
        });

    }

    /**
     * Test to see if a client is running and a null message is sent.
     */
    @Test
    public void TestRunNullMessage() {

        String message = null;
        String message2 = "";
        serverSideOut.println(message);
        serverSideOut.println(message2);
        serverSideOut.flush();

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            doThrow(NullPointerException.class).when(mockHandler).handleMessage(message, client);
            doThrow(NullPointerException.class).when(mockHandler).handleMessage(message2, client);
        });
    }

    /**
     * Test to see if the terminate method works.
     */
    @Test
    public void TestTerminate() {

        new Thread(client).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());

        client.terminate();
        assertFalse(client.isRunning());
    }

    @Test
    public void TestSend() {

        new Thread(client).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());

        String message = null;
        assertThrows(IllegalArgumentException.class, () -> {
            client.send(message);
        });
    }
}

