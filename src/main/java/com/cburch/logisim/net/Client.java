package com.cburch.logisim.net;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends SocketWrapper {
    Socket socket;

    OutputStreamWriter outputStream;

    BufferedWriter outputWriter;

    /**
     * <p>Creates a new client instance.</p>
     * <br />
     * <ul>
     *      <li>If the <code>address</code> parameter is null or an otherwise empty string, localhost will be used (which probably isn't what you want).</li>
     *      <li>If the <code>port</code> parameter is less than 0 or greater than 65535, then the default port will be used. Thus, if you want to use the default port, then pass a value like -1.</li>
     * </ul>
     * <br />
     * <p><strong>NOTE</strong> You must initialize the actual socket by calling the <code>Init()</code> method on this instance.</p>
     *
     * @param address the address that the client will try to connect to
     * @param port the port to use on the corresponding address
     */
    public Client(String address, int port) {
        this.address = (address != null && address.length() > 0) ? address : DEFAULT_LISTEN_ADDR;
        this.port = (port > 0 && port < 65536) ? port : DEFAULT_LISTEN_PORT;
    }

    public boolean connect() {
        if (this.socket != null) return false;
        this.socket = new Socket();
        try {
            this.socket.connect(new InetSocketAddress(this.address, this.port));
            this.outputStream = new OutputStreamWriter(this.socket.getOutputStream());
            this.outputWriter = new BufferedWriter(this.outputStream);
            System.out.printf("Connected to %s:%s%n", this.address, this.port);
            return true;
        }
        catch (IOException ex) {
            System.out.printf("Failed to connect to %s:%s%n", this.address, this.port);
        }
        return false;
    }

    public void send(String message) {
        if (this.socket == null || !this.socket.isConnected() || this.socket.isClosed()) return;
        if (this.outputStream == null) return;

        try {
            this.outputWriter.write(message);
            this.outputWriter.newLine();
            this.outputWriter.flush();
            System.out.println("Wrote message.");
        }
        catch (IOException ex) {
            System.out.println("Couldn't send message.");
        }
    }
}
