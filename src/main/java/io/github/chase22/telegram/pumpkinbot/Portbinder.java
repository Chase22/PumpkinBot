package io.github.chase22.telegram.pumpkinbot;

import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;

public class Portbinder {

    Portbinder(final int port) {
        BlockingHandler blockingHandler = new BlockingHandler(exchange ->
                exchange.getResponseSender().send("OK"));

            Undertow undertow = Undertow.builder()
                    .addHttpListener(port, "localhost")
                    .setHandler(blockingHandler).build();

            undertow.start();
        }
}
