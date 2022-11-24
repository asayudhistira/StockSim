package nl.rug.aoop.messagequeue.messagehandlers;

import nl.rug.aoop.command.CommandHandler;

import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.networking.server.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Tests for Message Handler Implementation.
 */
public class TestMessageHandlerImplementation {

    private static final int TIMEOUT = 2;
    private Client mockClient;
    private ClientHandler mockClientHandler;
    private CommandHandler mockCommandHandler;
    private MessageHandler messageHandler;
    private NetworkMessage message;
    private NetworkMessage message2;

    /**
     * Setting up before each test.
     * Mock a client and command handler.
     */
    @BeforeEach
    public void setUp() {
        mockClient = Mockito.mock(Client.class);
        mockClientHandler = Mockito.mock(ClientHandler.class);
        mockCommandHandler = Mockito.mock(CommandHandler.class);
        messageHandler = new MessageHandlerImplementation(mockCommandHandler);
        message = new NetworkMessage("1", "first");
        message2 = new NetworkMessage(null, null);
    }

    /**
     * Test Handle Message for client.
     */
    @Test
    public void TestHandleMessageClient() {
        messageHandler.handleMessage(message.toJson(),mockClient);

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockCommandHandler).executeCommand(message.getHeader(), Map.of(
                    "Header", message.getHeader(),
                    "Body", message.getBody(),
                    "Communicator", mockClient));
        });
    }

    /**
     * Test Handle Message for client handler.
     */
    @Test
    public void TestHandleMessageClientHandler() {
        messageHandler.handleMessage(message.toJson(),mockClientHandler);

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockCommandHandler).executeCommand(message.getHeader(), Map.of(
                    "Header", message.getHeader(),
                    "Body", message.getBody(),
                    "Communicator", mockClientHandler));
        });
    }

}
