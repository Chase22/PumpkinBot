package io.github.chase22.telegram.pumpkinbot;

import io.undertow.Undertow;
import io.undertow.util.StatusCodes;

public class Portbinder {

    Portbinder(final int port) {
            Undertow undertow = Undertow.builder()
                    .addHttpListener(port, "localhost")
                    .setHandler(exchange ->
                            exchange.setStatusCode(StatusCodes.OK).endExchange()).build();

            undertow.start();
        }
}
