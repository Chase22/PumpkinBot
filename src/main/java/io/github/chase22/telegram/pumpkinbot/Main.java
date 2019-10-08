package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;

public class Main {
    public static void main(String... args) throws TelegramApiRequestException {
        ApiContextInitializer.init();

        Injector injector = new Injector().initialize();

        final PumpkinConfig config = injector.pumpkinConfig;

        TelegramBotsApi telegramBotsApi;

        if (config.isWebhook()) {
            telegramBotsApi = new TelegramBotsApi(
                    Main.class.getResource("pumpkinbot.jsk").toString(),
                    config.getKeystorePassword(),
                    config.getUrl(),
                    config.getUrl(),
                    Main.class.getResource("pumpkinbot.pem").toString()
            );
        } else {
            telegramBotsApi = new TelegramBotsApi();
        }

        if (injector.sender instanceof TelegramLongPollingBot) {
            telegramBotsApi.registerBot((LongPollingBot) injector.sender);
        } else {
            telegramBotsApi.registerBot((WebhookBot) injector.sender);
        }
    }
}