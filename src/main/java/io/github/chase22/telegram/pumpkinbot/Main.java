package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.FilesConfig;
import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;

import java.io.IOException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        try {
            ApiContextInitializer.init();

            Injector injector = new Injector().initialize();

            final PumpkinConfig config = injector.pumpkinConfig;
            final FilesConfig filesConfig = injector.filesConfig;

            TelegramBotsApi telegramBotsApi;

            if (config.isWebhook()) {
                LOGGER.info("Configuring Webhook");
                telegramBotsApi = new TelegramBotsApi(
                        filesConfig.getKeystorePath(),
                        config.getKeystorePassword(),
                        config.getExternalUrl(),
                        config.getInternalUrl() + ":" + config.getPort(),
                        filesConfig.getCertificatePath()
                );
            } else {
                LOGGER.info("Configuring Longpolling");
                telegramBotsApi = new TelegramBotsApi();
            }

            if (injector.sender instanceof TelegramLongPollingBot) {
                LOGGER.info("Registering LongpollingBot");
                telegramBotsApi.registerBot((LongPollingBot) injector.sender);
            } else {
                LOGGER.info("Registering WebhookBot");
                telegramBotsApi.registerBot((WebhookBot) injector.sender);
            }
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Telegram api returned" + e.getApiResponse(), e);
        } catch (IOException e) {
            LOGGER.error("Error injecting dependencies", e);
        }
    }
}