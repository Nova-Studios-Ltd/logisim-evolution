package com.cburch.logisim.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * <p>A wrapper for ServerSocket that focuses on handling synchronizing editor actions between clients.</p>
 * <br />
 * <p>LiveShare&reg; for Logisim if you will.</p>
 */
public class Server {
    public final static String DEFAULT_LISTEN_ADDR = "127.0.0.1";
    public final static int DEFAULT_LISTEN_PORT = 5217;

    private final String Address;
    private final int Port;

    private ServerSocket Socket;

    /**
     * <p>Creates a new server instance.</p>
     * <br />
     * <ul>
     *      <li>If the <code>address</code> parameter is null or an otherwise empty string, localhost will be used (which probably isn't what you want).</li>
     *      <li>If the <code>port</code> parameter is less than 0 or greater than 65535, then the default port will be used. Thus, if you want to use the default port, then pass a value like -1.</li>
     * </ul>
     * <br />
     * <p><strong>NOTE</strong> You must initialize the actual socket by calling the <code>Init()</code> method on this instance.</p>
     *
     * @param address the address of the interface that this server will bind to
     * @param port the port that this server will bind to on the corresponding interface
     */
    public Server(String address, int port) {
        this.Address = (address != null && address.length() > 0) ? address : DEFAULT_LISTEN_ADDR;
        this.Port = (port > 0 && port < 65536) ? port : DEFAULT_LISTEN_PORT;
    }

    /**
     * <p>Attempts to initialize the internal socket of this instance with the values given in the constructor.</p>
     * <p>If you need to reinitialize the socket, either create a new instance of this class or call <code>close()</code> before calling <code>Init()</code> again.</p>
     * <br />
     * <p>Will return false if the socket is already initialized or if an exception is encountered.</p>
     * @return a boolean indicating whether the server initialized successfully
     */
    public boolean Init() {
        if (this.Socket != null) return false; // Already initialized
        try {
            this.Socket = new ServerSocket();
            this.Socket.bind(new InetSocketAddress(this.Address, this.Port));
            return true;
        }
        catch (IOException | IllegalArgumentException | SecurityException ex) {
            return false;
        }
    }

    /**
     * Begin listening to incoming client requests.
     */
    public void listen() {
        if (this.Socket == null) {
            System.out.println("Not listening because the internal socket is null.");
            return;
        }
        new Thread(new Listener(this.Socket)).start();
    }

    /**
     * <p>Calls <code>close()</code> on the internal socket and sets the reference of the internal socket to null.</p>
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void close() throws IOException {
        if (this.Socket == null) return;
        this.Socket.close();
        this.Socket = null;
    }
}
