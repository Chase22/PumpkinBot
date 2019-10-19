package io.github.chase22.telegram.pumpkinbot;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Portbinder {

    Portbinder(final int port) {
        try {
            String host = null;
            ServerSocket socket = new ServerSocket(port);
            LoggerFactory.getLogger(Portbinder.class).info("Bound port {}", port);
            Executors.newFixedThreadPool(1).submit(() -> {
                try {
                    while (!Thread.interrupted()) {
                        final Socket clientSocket = socket.accept();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
