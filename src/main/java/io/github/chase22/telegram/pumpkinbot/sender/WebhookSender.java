package io.github.chase22.telegram.pumpkinbot.sender;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

public class WebhookSender extends TelegramWebhookBot implements UpdateProvider {
    private PumpkinConfig pumpkinConfig;
    private Consumer<Update> updateConsumer;

    public WebhookSender(final PumpkinConfig pumpkinConfig) {
        this.pumpkinConfig = pumpkinConfig;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(final Update update) {
        updateConsumer.accept(update);
        return null;
    }

    @Override
    public String getBotUsername() {
        return pumpkinConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return pumpkinConfig.getBotToken();
    }

    @Override
    public String getBotPath() {
        return getBotUsername();
    }

    @Override
    public void setUpdateConsumer(final Consumer<Update> updateConsumer) {
        this.updateConsumer = updateConsumer;
    }
}
