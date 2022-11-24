package nl.rug.aoop.command;

import java.util.Map;

/**
 * An Interface for command.
 */
public interface Command {

    /**
     * Executes a command.
     * @param options the key value pairs.
     */
    void execute(Map<String, Object> options);
}
