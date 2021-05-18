package io.github.chase22.telegram.pumpkinbot.sender;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.http.util.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.grizzly.streams.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class WebhookUpdateProvider implements UpdateProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookUpdateProvider.class);

    private final List<Consumer<Update>> consumers = new LinkedList<>();

    final HttpServer server = new HttpServer();

    public WebhookUpdateProvider(
            final String externalUrl,
            final AbsSender sender,
            final int port,
            final ObjectMapper objectMapper
    ) {

        server.addListener(new NetworkListener("portBinder", "0.0.0.0", port));

        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            @Override
            public void service(final Request request, final Response response) throws Exception {
                if (request.getMethod() == Method.POST) {
                    if (request.getContentType().equalsIgnoreCase("application/json")) {
                        try {
                            LOGGER.info("Request received");
                            Update update = objectMapper.readValue(request.getReader(), Update.class);
                            consumers.parallelStream().forEach(consumer -> consumer.accept(update));
                            response.setStatus(HttpStatus.OK_200);
                        } catch (JsonMappingException e) {
                            response.setStatus(HttpStatus.BAD_REQUEST_400);
                            LOGGER.error("Couldn't map request to update", e);
                        }
                    } else {
                        response.setStatus(HttpStatus.BAD_REQUEST_400);
                    }
                } else {
                    response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
                    response.flush();
                }
            }
        });

        try {
            LOGGER.info("Starting server on port {}", port);
            server.start();

            sender.execute(
                    SetWebhook.builder()
                    .url(externalUrl)
                    .certificate(
                            new InputFile(
                                    WebhookUpdateProvider.class
                                            .getClassLoader()
                                            .getResourceAsStream("certificate.cert"),
                                    "certificate.cert")
                    ).build()
            );
        } catch (IOException e) {
            LOGGER.error("Error starting server", e);
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Error while setting webhook: " + e.getApiResponse(), e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while setting webhook", e);
        }

    }

    public boolean isRunning() {
        return server.isStarted();
    }

    @Override
    public void registerUpdateConsumer(Consumer<Update> updateConsumer) {
        consumers.add(updateConsumer);
    }
}
