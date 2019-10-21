package io.github.chase22.telegram.pumpkinbot;

import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Portbinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Portbinder.class);

    Portbinder(final int port) {
        final HttpServer server = new HttpServer();

        server.addListener(new NetworkListener("portBinder", "0.0.0.0", port));

        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            @Override
            public void service(final Request request, final Response response) throws Exception {
                LOGGER.info("Request received");
                response.setStatus(HttpStatus.OK_200);
                response.getWriter().write("Ok");
            }
        });

        try {
            LOGGER.info("Starting server on port {}", port);
            server.start();
        } catch (IOException e) {
            LOGGER.error("Error starting server", e);
        }

    }
}
