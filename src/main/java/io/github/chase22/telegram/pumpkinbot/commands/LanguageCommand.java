package io.github.chase22.telegram.pumpkinbot.commands;

import io.github.chase22.telegram.pumpkinbot.MessagePatterns;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;

public class LanguageCommand extends AbstractPumpkinCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageCommand.class);
    private final LanguageHandler languageHandler;

    public LanguageCommand(LanguageHandler languageHandler) {
        super("languages", "shows all supported languages");
        this.languageHandler = languageHandler;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
        sendMessage(absSender, message, MessagePatterns.LANGUAGES_PATTERN, languageHandler.getLanguages());
    }
}
