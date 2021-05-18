package io.github.chase22.telegram.pumpkinbot.sender;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

public class TelegramSender extends DefaultAbsSender {
    private final PumpkinConfig pumpkinConfig;

    public TelegramSender(PumpkinConfig pumpkinConfig) {
        super(new DefaultBotOptions());
        this.pumpkinConfig = pumpkinConfig;
    }

    @Override
    public String getBotToken() {
        return pumpkinConfig.getBotToken();
    }
}
