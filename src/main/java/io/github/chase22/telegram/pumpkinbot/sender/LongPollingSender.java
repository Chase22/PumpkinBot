package io.github.chase22.telegram.pumpkinbot.sender;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

public class LongPollingSender extends TelegramLongPollingBot implements UpdateProvider {
    private final PumpkinConfig config;
    private Consumer<Update> updateConsumer;

    public LongPollingSender(final PumpkinConfig config) {
        this.config = config;
    }

    public void setUpdateConsumer(final Consumer<Update> updateConsumer) {
        this.updateConsumer = updateConsumer;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        updateConsumer.accept(update);
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }
}
