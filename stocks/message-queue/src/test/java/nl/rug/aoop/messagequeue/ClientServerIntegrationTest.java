package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.messagehandlers.MessageHandlerImplementation;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.networking.server.ClientHandler;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

public class ClientServerIntegrationTest {

    private static final int TIMEOUT = 1;
    private Server server;
    private Client client;
    private int serverPort;
    private InetSocketAddress address;
    private MessageHandler messageHandler;
    private MessageHandler messageHandler2;

    @Test
    public void TestClientServerConnection() throws IOException {
        messageHandler = Mockito.mock(MessageHandlerImplementation.class);
        server = new Server(0, messageHandler);
        serverPort = server.getPort();
        new Thread(server).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(server::isRunning);

        address = new InetSocketAddress("localhost", serverPort);
        messageHandler2 = Mockito.mock(MessageHandlerImplementation.class);
        client = new Client(address,messageHandler2);
        new Thread(client).start();
        await().atMost(TIMEOUT, TimeUnit.SECONDS).until(client::isRunning);
        assertTrue(client.isRunning());

        String message = "test message";
        client.send(message);

        ClientHandler clientHandler = server.getClientsList().get(0);

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageHandler).handleMessage(message, clientHandler);
        });
    }
}
