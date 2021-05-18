package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.sender.WebhookUpdateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        try {
            Injector injector = new Injector().initialize();

            final PumpkinConfig config = injector.pumpkinConfig;

            TelegramBotsApi telegramBotsApi;

            if (injector.updateProvider instanceof TelegramLongPollingBot) {
                LOGGER.info("Configuring Longpolling");
                telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

                new Portbinder(config.getPort());
                LOGGER.info("Registering LongpollingBot");
                telegramBotsApi.registerBot((LongPollingBot) injector.sender);
            } else if (injector.updateProvider instanceof WebhookUpdateProvider) {
                LOGGER.info("Configuring webhook");
                while (((WebhookUpdateProvider) injector.updateProvider).isRunning()) {
                    Thread.sleep(100);
                }
            }


        } catch (TelegramApiRequestException e) {
            LOGGER.error("Telegram api returned" + e.getApiResponse(), e);
        } catch (TelegramApiException e) {
            LOGGER.error("Unexpected Api exception", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}