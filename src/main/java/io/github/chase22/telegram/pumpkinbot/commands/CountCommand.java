package io.github.chase22.telegram.pumpkinbot.commands;


import io.github.chase22.telegram.pumpkinbot.MessagePatterns;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;

public class CountCommand extends AbstractPumpkinCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountCommand.class);

    private final PumpkinStorage storage;

    public CountCommand(PumpkinStorage pumpkinStorage) {
        super("count", "Shows the current count");
        this.storage = pumpkinStorage;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
            if (storage.exists(message.getChatId())) {
                final int count = storage.getForChat(message.getChatId());
                sendMessage(absSender, message, MessagePatterns.COUNT_PATTERN, count);
            } else {
                sendMessage(absSender, message, MessagePatterns.NOT_STARTED_PATTERN);
            }
    }
}
