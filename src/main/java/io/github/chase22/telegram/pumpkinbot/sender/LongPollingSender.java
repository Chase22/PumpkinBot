package io.github.chase22.telegram.pumpkinbot.sender;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LongPollingSender extends TelegramLongPollingBot implements UpdateProvider {
    private final PumpkinConfig config;
    private final List<Consumer<Update>> updateConsumer = new ArrayList<>();
    public LongPollingSender(final PumpkinConfig config) {
        this.config = config;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        updateConsumer.parallelStream().forEach(consumer -> consumer.accept(update));
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void registerUpdateConsumer(Consumer<Update> updateConsumer) {
        this.updateConsumer.add(updateConsumer);
    }
}
