package io.github.chase22.telegram.pumpkinbot.commands;

import org.slf4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public abstract class AbstractPumpkinCommand extends BotCommand {
    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    public AbstractPumpkinCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            execute(absSender, message, arguments);
        } catch (TelegramApiRequestException e) {
            getLogger().error("Error executing " + getCommandIdentifier() + " command Reason: " + e.getApiResponse(), e);
        } catch (TelegramApiException e) {
            getLogger().error("Error executing " + getCommandIdentifier() + " command", e);
        }
    }

    public abstract Logger getLogger();

    public abstract void execute(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException;

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
    }
}
