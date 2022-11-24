package nl.rug.aoop.command;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Models a command handler.
 */
@Slf4j
public class CommandHandler {
    private final Map<String, Command> commandMap;

    /**
     * Constructor for command handler.
     */
    public CommandHandler() {
        commandMap = new HashMap<>();
    }

    /**
     * Adds a command.
     * @param name name of the command.
     * @param command the command.
     */
    public void addCommand(String name, Command command) {
        commandMap.put(name, command);
    }

    /**
     * Execute a certain action/command.
     * @param command name of the command.
     * @param options the key values pairs.
     */
    public void executeCommand(String command, Map<String, Object> options) {

        if (commandMap.containsKey(command)) {
            commandMap.get(command).execute(options);
        } else {
            log.error("Command not found");
            //log.error(command);
            throw new IllegalArgumentException();
        }
    }
}
