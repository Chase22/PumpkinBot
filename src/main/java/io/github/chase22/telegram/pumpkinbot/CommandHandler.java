package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.StringJoiner;

import static java.util.Arrays.asList;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;
import static org.telegram.telegrambots.meta.api.objects.MemberStatus.ADMINISTRATOR;
import static org.telegram.telegrambots.meta.api.objects.MemberStatus.CREATOR;

public class CommandHandler {
    private static final String WELCOME_PATTERN = "This bot will now count your pumpkins in %d languages. %n%n" +
            "powered by: [Translatr](https://translatr.varunmalhotra.xyz";
    private static final String COUNT_PATTERN = "Current pumpkin count is: %d";
    private static final String STOP_PATTERN = "Shutting down";
    private static final String NOT_STARTED_PATTERN = "Bot is not started. Use /start";
    private static final String ALREADY_STARTED_PATTERN = "Bot is already running";
    private static final String LANGUAGES_PATTERN = "This bot supports the following languages: %n %s";
    private static final String RESET_PATTERN = "Resetting";
    private static final String NO_ADMIN_PATTERN = "Only admins are allowed to execute this command";

    private static final String HELP_PATTERN = new StringJoiner("\n")
            .add("/help - displays this message")
            .add("/start - starts the bot")
            .add("/stop - stops the bot")
            .add("/count - displays the current count")
            .add("/reset - resets the counter to 0")
            .add("/languages - shows all supported languaged").toString();

    private final AbsSender sender;
    private final LanguageHandler languageHandler;
    private final PumpkinStorage storage;

    public CommandHandler(final AbsSender sender, final LanguageHandler languageHandler, final PumpkinStorage storage) {
        this.sender = sender;
        this.languageHandler = languageHandler;
        this.storage = storage;
    }

    public void handle(final Message message) throws TelegramApiException {
        if (!message.isCommand()) return;

        final Optional<String> command = message.getEntities().stream()
                .filter(messageEntity -> messageEntity.getType().equals(EntityType.BOTCOMMAND))
                .map(MessageEntity::getText).findFirst();

        if (command.isPresent()) {
            handleCommand(command.get().toLowerCase().trim(), message);
        }
    }

    private void handleCommand(final String text, final Message message) throws TelegramApiException {
        switch (text) {
            case "/start":
                start(message);
                break;
            case "/stop":
                stop(message);
                break;
            case "/count":
                count(message);
                break;
            case "/help":
                help(message);
                break;
            case "/reset":
                reset(message);
                break;
            case "/languages":
                languages(message);
                break;
        }
    }

    private void help(final Message message) throws TelegramApiException {
        sendMessage(message, HELP_PATTERN);
    }

    private void count(final Message message) throws TelegramApiException {
        final int count = storage.getForChat(message.getChatId());
        sendMessage(message, COUNT_PATTERN, count);
    }

    private void stop(final Message message) throws TelegramApiException {
        if (!checkAdmin(message)) return;

        if (storage.exists(message.getChatId())) {
            sendMessage(message, STOP_PATTERN);
            storage.removeChat(message.getChatId());
        } else {
            sendMessage(message, NOT_STARTED_PATTERN);
        }
    }

    private void start(final Message message) throws TelegramApiException {
        if (!checkAdmin(message)) return;

        if (storage.exists(message.getChatId())) {
            sendMessage(message, ALREADY_STARTED_PATTERN);
        } else {
            sendMessageMarkdown(message, WELCOME_PATTERN, true, true, languageHandler.getLanguageCount());
            storage.setForChat(message.getChatId(), 0);
            count(message);
        }
    }

    private void reset(final Message message) throws TelegramApiException {
        if (!checkAdmin(message)) return;

        sendMessage(message, RESET_PATTERN);
        storage.setForChat(message.getChatId(), 0);
        count(message);
    }

    private void languages(final Message message) throws TelegramApiException {
        sendMessage(message, LANGUAGES_PATTERN, languageHandler.getLanguages());
    }

    private void sendMessageMarkdown(final Message message,
                                     final String pattern,
                                     final boolean hidePreview,
                                     final boolean markdown,
                                     Object... vars) throws TelegramApiException {
        final SendMessage sendMessage = new SendMessage(message.getChatId(), String.format(pattern, vars))
                .setReplyToMessageId(message.getMessageId());

        if (hidePreview) sendMessage.disableWebPagePreview();
        if (markdown) sendMessage.setParseMode(MARKDOWN);

        sender.execute(sendMessage);
    }

    private void sendMessage(final Message message, final String pattern, Object... vars) throws TelegramApiException {
        sendMessageMarkdown(message, pattern, false, false, vars);
    }

    private boolean checkAdmin(Message message) throws TelegramApiException {
        if (message.isUserMessage()) {
            return true;
        } else if (isAdmin(message)) {
            return true;
        } else {
            sendMessage(message, NO_ADMIN_PATTERN);
            return false;
        }
    }

    private boolean isAdmin(Message message) throws TelegramApiException {
        final GetChatMember getChatMember = new GetChatMember()
                .setChatId(message.getChatId())
                .setUserId(message.getFrom().getId());

        final ChatMember chatMember = sender.execute(getChatMember);

        return asList(ADMINISTRATOR, CREATOR).contains(chatMember.getStatus());
    }
}