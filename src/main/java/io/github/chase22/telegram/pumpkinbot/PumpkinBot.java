package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.sender.UpdateProvider;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class PumpkinBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(PumpkinBot.class);

    private final AbsSender sender;
    private final LanguageHandler languageHandler;
    private final PumpkinStorage storage;
    private final CommandRegistry commandRegistry;

    public PumpkinBot(
            final AbsSender sender,
            final UpdateProvider updateProvider,
            final LanguageHandler languageHandler,
            final PumpkinStorage storage,
            final PumpkinConfig config,
            final List<IBotCommand> commands
    ) {
        this.sender = sender;
        this.languageHandler = languageHandler;
        this.storage = storage;

        commandRegistry = new CommandRegistry(true, config::getBotUsername);
        commands.forEach(commandRegistry::register);
        
        updateProvider.registerUpdateConsumer(this::onUpdateReceived);
    }

    public void onUpdateReceived(final Update update) {
        LOGGER.debug("Update received");
        if (update.hasMessage() && update.getMessage().hasText()) {
            LOGGER.debug("Message received");
            Message message = update.getMessage();

            String messageText = message.getText().toLowerCase().trim();

            if (storage.exists(message.getChatId())) {
                LOGGER.debug("Get pumpkin count " + messageText);
                storage.increase(message.getChatId(), languageHandler.countPumpkin(messageText));
            }
            commandRegistry.executeCommand(sender, message);
        }
    }
}
