package com.cburch.logisim.net;

/**
 * <p>A wrapper for ServerSocket that focuses on handling synchronizing editor actions between clients.</p>
 * <br />
 * <p>LiveShare&reg; for Logisim if you will.</p>
 */
public abstract class SocketWrapper {
    public final static String DEFAULT_LISTEN_ADDR = "127.0.0.1";

    public final static int DEFAULT_LISTEN_PORT = 5217;

    protected String address;

    protected int port;
}
