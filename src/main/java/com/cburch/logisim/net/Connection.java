package com.cburch.logisim.net;

import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
    protected Socket socket;

    public Connection(Socket clientSocket) {
        this.socket = clientSocket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = this.socket.getInputStream();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
            }
        }
        catch (IOException ex) {
            this.print("An IO exception occurred while trying to handle an incoming client request. Details: " + ex.getMessage(), true);
        }

        if (inputStream == null) return;

        String data;

        while (!this.socket.isClosed()) {
            try {
                if (!reader.ready()) continue;
                data = reader.readLine();

                this.print(data, false);
            }
            catch (IOException ex) {
                this.print("Error reading line from client buffer. Details: " + ex.getMessage(), true);
            }
        }

        try {
            reader.close();
            inputStream.close();
        }
        catch (IOException ignored) { }

        this.print("Closed", true);
    }

    public void close() throws IOException {
        if (this.socket != null) this.socket.close();
    }

    private void print(String text, boolean isConsoleText) {
        if (this.socket == null) {
            System.out.println("Print was called but the corresponding client socket was null!");
            return;
        }
        if (isConsoleText) {
            System.out.printf("[SER:%s:%s]: %s%n", this.socket.getInetAddress(), this.socket.getPort(), text);
        }
        else {
            System.out.printf("[CLI:%s:%s]: %s%n", this.socket.getInetAddress(), this.socket.getPort(), text);
        }
    }
}
