package io.github.chase22.telegram.pumpkinbot.commands;


import io.github.chase22.telegram.pumpkinbot.MessagePatterns;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.github.chase22.telegram.pumpkinbot.AdminUtils.checkAdmin;
import static io.github.chase22.telegram.pumpkinbot.MessagePatterns.WELCOME_PATTERN;
import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;
import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessageMarkdown;

public class StartCommand extends AbstractPumpkinCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    private final PumpkinStorage storage;
    private final LanguageHandler languageHandler;
    private final CountCommand countCommand;

    public StartCommand(PumpkinStorage storage, LanguageHandler languageHandler, CountCommand countCommand) {
        super("start", "starts the bot and begins the count");
        this.storage = storage;
        this.languageHandler = languageHandler;
        this.countCommand = countCommand;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
        if (!checkAdmin(absSender, message)) return;

        if (storage.exists(message.getChatId())) {
            sendMessage(absSender, message, MessagePatterns.ALREADY_STARTED_PATTERN);
        } else {
            sendMessageMarkdown(absSender, message, WELCOME_PATTERN, true, true, languageHandler.getLanguageCount());
            storage.setForChat(message.getChatId(), 0);
            countCommand.processMessage(absSender, message, arguments);
        }
    }
}
