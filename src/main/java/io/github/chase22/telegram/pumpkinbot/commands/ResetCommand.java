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

public class ResetCommand extends AbstractPumpkinCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetCommand.class);
    private final PumpkinStorage storage;
    private final CountCommand countCommand;

    public ResetCommand(PumpkinStorage storage, CountCommand countCommand) {
        super("reset", "Resets the counter to 0");
        this.storage = storage;
        this.countCommand = countCommand;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
        if (!checkAdmin(absSender, message)) return;

        if (!storage.exists(message.getChatId())) {
            sendMessage(absSender, message, MessagePatterns.NOT_STARTED_PATTERN);
        } else {
            sendMessage(absSender, message, MessagePatterns.RESET_PATTERN);
            storage.setForChat(message.getChatId(), 0);
            countCommand.processMessage(absSender, message, arguments);
        }
    }
}
