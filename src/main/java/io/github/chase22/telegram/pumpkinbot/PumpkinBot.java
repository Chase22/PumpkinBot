package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PumpkinBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(PumpkinBot.class);

    private final PumpkinConfig config;
    private final LanguageHandler languageHandler;
    private final PumpkinStorage storage;
    private CommandHandler commandHandler;

    public PumpkinBot(final PumpkinConfig config, final LanguageHandler languageHandler, final PumpkinStorage storage) {
        this.config = config;
        this.languageHandler = languageHandler;
        this.storage = storage;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        try {

            if (update.hasMessage() && update.getMessage().hasText()) {
                Message message = update.getMessage();

                commandHandler.handle(message);
                String messageText = message.getText().toLowerCase().trim();

                if (languageHandler.containsPumpkin(messageText)) {
                    execute(new SendMessage(message.getChatId(), "Pumpkin found"));
                    storage.increase(message.getChatId());
                }
            }
        } catch (TelegramApiException e) {
            LOGGER.error("Unable to execute telegram method", e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public void setCommandHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
