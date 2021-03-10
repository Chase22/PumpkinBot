package io.github.chase22.telegram.pumpkinbot.commands;

import io.github.chase22.telegram.pumpkinbot.MessagePatterns;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.github.chase22.telegram.pumpkinbot.AdminUtils.checkAdmin;
import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;

public class StopCommand extends AbstractPumpkinCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopCommand.class);
    private final PumpkinStorage storage;

    public StopCommand(PumpkinStorage storage) {
        super("stop", "Stops the bot and deletes all data");
        this.storage = storage;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
        if (!checkAdmin(absSender, message)) return;

        if (storage.exists(message.getChatId())) {
            sendMessage(absSender, message, MessagePatterns.STOP_PATTERN);
            storage.removeChat(message.getChatId());
        } else {
            sendMessage(absSender, message, MessagePatterns.NOT_STARTED_PATTERN);
        }
    }
}
