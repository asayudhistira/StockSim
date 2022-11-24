package nl.rug.aoop.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doThrow;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for command handler.
 */
public class TestCommandHandler {

    private static final int TIMEOUT = 1;
    private CommandHandler commandHandler;
    private String commandName;
    private String commandName2;
    private Command mockCommand;

    /**
     * Set up method.
     */
    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        mockCommand = Mockito.mock(Command.class);
        commandName = "MqPut";
        commandName2 = "Test";
    }

    /**
     * Test to see if a command is executed.
     */
    @Test
    public void TestAddExecuteCommand() {
        commandHandler.addCommand(commandName, mockCommand);

        Map param = Map.of("commandName", commandName);
        commandHandler.executeCommand(commandName,param);

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockCommand).execute(param);
        });
    }

    /**
     * test to see if an IllegalArgumentException is thrown when a command that doesn't exist is passed.
     */
    @Test
    public void TestAddExecuteCommandNotAvailable() {
        Map param = Map.of("commandName", commandName2);

        assertThrows(IllegalArgumentException.class, () -> {
            commandHandler.executeCommand(commandName2,param);
        });

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            doThrow(IllegalArgumentException.class).when(mockCommand).execute(param);
        });
    }

    /**
     * Test to see if multiple command is executed.
     */
    @Test
    public void TestAddExecuteMultipleCommand() {
        commandHandler.addCommand(commandName, mockCommand);
        commandHandler.addCommand(commandName2, mockCommand);

        Map param = Map.of("commandName", commandName);
        Map param2 = Map.of("commandName", commandName2);
        commandHandler.executeCommand(commandName,param);
        commandHandler.executeCommand(commandName,param2);

        await().atMost(TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            Mockito.verify(mockCommand).execute(param);
            Mockito.verify(mockCommand).execute(param2);
        });

    }
}
