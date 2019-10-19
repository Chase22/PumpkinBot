package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.sender.UpdateProvider;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PumpkinBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(PumpkinBot.class);

    private final AbsSender sender;
    private final LanguageHandler languageHandler;
    private final PumpkinStorage storage;
    private CommandHandler commandHandler;

    public PumpkinBot(final AbsSender sender, final LanguageHandler languageHandler, final PumpkinStorage storage) {
        this.sender = sender;
        this.languageHandler = languageHandler;
        this.storage = storage;

        if (sender instanceof UpdateProvider) {
            ((UpdateProvider) sender).setUpdateConsumer(this::onUpdateReceived);
        } else {
            throw new IllegalArgumentException("sender does not implement UpdateProvider");
        }
    }

    public void onUpdateReceived(final Update update) {
        try {

            if (update.hasMessage() && update.getMessage().hasText()) {
                Message message = update.getMessage();

                commandHandler.handle(message);
                String messageText = message.getText().toLowerCase().trim();

                if (languageHandler.containsPumpkin(messageText)) {
                    storage.increase(message.getChatId());
                }
            }
        } catch (TelegramApiException e) {
            LOGGER.error("Unable to execute telegram method", e);
        }
    }

    public void setCommandHandler(final CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
