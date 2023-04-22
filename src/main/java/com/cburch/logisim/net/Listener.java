package com.cburch.logisim.net;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Listener implements Runnable {

    ServerSocket Socket;

    private final LinkedList<Connection> Clients;

    public Listener(ServerSocket socket) {
        this.Socket = socket;
        this.Clients = new LinkedList<Connection>();
    }

    private void onClientDisconnect(Connection client) {
        if (client != null) {
            this.Clients.remove(client);
        }
    }

    /**
     * The actual thread for listening to incoming client requests.
     */
    @Override
    public void run() {
        Connection client;
        while (this.Socket != null && !this.Socket.isClosed() && this.Socket.isBound()) {
            try {
                client = new Connection(this.Socket.accept());
                this.Clients.add(client);

                client.start();
            }
            catch (SocketException ex) {
                this.stop();
                return;
            }
            catch (IOException ex) {
                System.out.println("An IO exception occurred while trying to handle an incoming client request. Details: " + ex.getMessage());
            }

        }
    }

    private void stop() {
        System.out.println("Stopping server...");

        Iterator<Connection> iterator = this.Clients.iterator();

        while (iterator.hasNext()) {
            try {
                Connection client = iterator.next();
                if (client != null) client.close();
            }
            catch (NoSuchElementException | IOException ignored) { }

        }

        this.Clients.clear();
    }
}
