package de.uulm.in.vs.grn.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class PubSubServer implements Runnable {
    private ServerSocket serverSocket;
    private List<Socket> pubSubClients;
    private int port;

    public PubSubServer(int port) {
        this.port = port;
        this.pubSubClients = new LinkedList<>();
    }

    /**
     * Broadcast message to all connected clients
     */
    public void sendToAll(Message message) {
        Debug.println("PubSub Server sending message to all clients: " + message.toString());
        for (Socket client : pubSubClients) {
            try {
                client.getOutputStream().write(message.build());
                client.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Debug.println("PubSub Server starting");
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                Debug.println("PubSub Server new Client: " + client);
                pubSubClients.add(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
