package nl.rug.aoop.messagequeue.producers;

import nl.rug.aoop.messagequeue.messagemodel.Message;
import nl.rug.aoop.messagequeue.messagemodel.NetworkMessage;
import nl.rug.aoop.networking.client.Client;

/**
 * Implementation of the MQProducer interface.
 */
public class NetworkProducer implements MQProducer {
    private Client client;

    /**
     * Constructor for Network Producer.
     * @param client the client,
     */
    public NetworkProducer(Client client) {
        this.client = client;
    }

    /**
     * Method used by the server to put a message in the message queue.
     * The header of the message contains the name of the command.
     * @param message to be enqueued in the message queue.
     * @return a new Message.
     */
    public NetworkMessage createPutMessage(Message message) {
        return new NetworkMessage("MqPut", message.toJson());
    }

    /**
     * Method to register a bot in the server.
     * @param message message that contains the id of bot.
     * @return a message with Register as its header.
     */
    public NetworkMessage createRegisterMessage(Message message) {
        return new NetworkMessage("Register", message.getBody());
    }

    /**
     * Method to deregister a bot in the server.
     * @param message message that contains the id of bot.
     * @return a message with Deregister as its header.
     */
    public NetworkMessage createDeregisterMessage(Message message) {
        return new NetworkMessage("Register", message.getBody());
    }

    /**
     * First, converts the message to JSON.
     * Then, it sends the json representation of the message to a client.
     *  @param message the message to be enqueued.
     */
    @Override
    public void put(Message message) {
        client.send(createPutMessage(message).toJson());
    }

    /**
     * Converts the message to Json and sending it to server.
     * @param message the message.
     */
    public void register(Message message) {
        client.send(createRegisterMessage(message).toJson());
    }

    /**
     * Converts the message to Json and sending it to server.
     * @param message the message.
     */
    public void deregister(Message message) {
        client.send(createDeregisterMessage(message).toJson());
    }
}
