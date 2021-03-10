package io.github.chase22.telegram.pumpkinbot.commands;

import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Collectors;

import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;

public class DumpCommand extends AbstractPumpkinCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(DumpCommand.class);
    private final PumpkinStorage storage;

    public DumpCommand(PumpkinStorage storage) {
        super("dump", "Dumps some data for a backup");
        this.storage = storage;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException {
        if (message.getFrom().getId() == 188215327) {
            String messageText = storage.getAll().entrySet().stream()
                    .map(entry -> getChatName(absSender, entry.getKey()) + "(" + entry.getKey() + "): " + entry.getValue())
                    .collect(Collectors.joining("\n"));
            sendMessage(absSender, message, messageText);
        }
    }
    private String getChatName(AbsSender sender, String id) {
        try {
            return sender.execute(new GetChat(id)).getTitle();
        } catch (TelegramApiException e) {
            return null;
        }
    }

}
