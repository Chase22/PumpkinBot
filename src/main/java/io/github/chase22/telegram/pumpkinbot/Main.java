package io.github.chase22.telegram.pumpkinbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

public class Main {
    public static void main(String... args) throws TelegramApiRequestException {
        ApiContextInitializer.init();

        Injector injector = new Injector().initialize();

        new TelegramBotsApi().registerBot((LongPollingBot) injector.sender);
    }
}
