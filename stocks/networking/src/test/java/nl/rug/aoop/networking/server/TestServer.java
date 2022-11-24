package nl.rug.aoop.networking.server;

import nl.rug.aoop.networking.client.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

import static org.awaitility.Awaitility.await;

/**
 * Test for Server.
 */
public class TestServer {

    private static final int TIMEOUT = 1;
    private Server server;

    /**
     * Creates a server.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    @BeforeEach
    public void setUp()  throws IOException{
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        server = new Server(0, mockHandler);
        new Thread(server).start();
        await().atMost(1, TimeUnit.SECONDS).until(server::isRunning);
    }

    /**
     * Test if the server is running and port number is not null.
     */
    @Test
    public void TestServerRunning() {
        assertTrue(server.isRunning());
    }

    /**
     * Test to check that port number is not null
     */
    @Test
    public void TestPortNumberNotNull() {
        assertNotNull(server.getPort());
    }

    /**
     * Test to see that the initial number of clients is zero.
     */
    @Test
    public void TestNumberOfClientsZero() {
        assertEquals(0, server.getNumClients());
    }

    /**
     * Test the run method with a single connection.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    @Test
    public void TestRunWithSingleConnection() throws IOException {

        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", server.getPort()));
            await().atMost(TIMEOUT,TimeUnit.SECONDS).until(() -> server.getNumClients() == 1);
            assertEquals(server.getNumClients(), 1);
        }
    }

    /**
     * Test the run method with multiple connections.
     * @throws IOException if an I/O error occurs when creating the socket/
     */
    @Test
    public void TestRunWithMultipleConnections() throws IOException{
        try(Socket socket = new Socket(); Socket socket1 = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", server.getPort()));
            socket1.connect(new InetSocketAddress("localhost", server.getPort()));
            await().atMost(TIMEOUT, TimeUnit.SECONDS).until(() -> server.getNumClients() == 2);
            assertEquals(server.getNumClients(), 2);
        }
    }

    /**
     * Test to see if server terminates.
     * Since, termination of server is not done in implementation, we call the function here.
     */
    @Test
    public void TestTerminate() {
        assertTrue(server.isRunning());
        server.terminate();
        assertFalse(server.isRunning());

    }
}
