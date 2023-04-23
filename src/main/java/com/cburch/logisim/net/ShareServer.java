package com.cburch.logisim.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ShareServer {
    // Global Socket and Thread; Instanced once only
    private ServerSocket socket;
    private Thread serverThread;

    // Socket Connection Information
    private final String address;
    private final int port;


    public ShareServer(String address, int port) {
        this.address = (address != null && address.length() > 0)? address : NetworkConfiguration.DEFAULT_ADDR;
        this.port = (port > 0 && port < 65536)? port : NetworkConfiguration.DEFAULT_PORT;
    }

    public boolean init() {
        if (this.socket != null) return false;
        try {
            this.socket = new ServerSocket();
            return true;
        }
        catch (IOException | IllegalArgumentException | SecurityException ex) {
            System.out.println("An exception occurred while initializing the server.");
            return false;
        }
    }

    public void start() {
        if (this.socket == null) return;
        try {
            // Bind to interface
            this.socket.bind(new InetSocketAddress(this.address, this.port));

            // Start listener thread
            serverThread = new Thread(() -> {
                System.out.println("Server is running and awaiting a client...");
                while (socket != null && !socket.isClosed() && socket.isBound()) {
                    try {
                        new Connection(socket.accept()).start();
                    } catch (IOException e) {
                        System.out.println("Server has stopped");
                        return;
                    }
                }
                System.out.println("Server has stopped");
            });
            serverThread.start();

        } catch (IOException e) {
        }
    }

    public void stop() {
        if (this.socket == null) return;
        try {
            this.socket.close();
        } catch (IOException e) {
            return;
        }
        this.socket = null;
        this.serverThread.interrupt();
    }
}
