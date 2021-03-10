package io.github.chase22.telegram.pumpkinbot.commands;

import io.github.chase22.telegram.pumpkinbot.MessagePatterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;

public class HelpCommand extends AbstractPumpkinCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);

    public HelpCommand() {
        super("help", "Shows this message");
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
        sendMessage(absSender, message, MessagePatterns.HELP_PATTERN);
    }
}
