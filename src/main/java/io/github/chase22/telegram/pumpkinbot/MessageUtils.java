package io.github.chase22.telegram.pumpkinbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;

public class MessageUtils {
    public static void sendMessageMarkdown(
            final AbsSender sender,
            final Message message,
            final String pattern,
            final boolean hidePreview,
            final boolean markdown,
            Object... vars
    ) throws TelegramApiException {
        final SendMessage.SendMessageBuilder sendMessageBuilder = SendMessage.builder()
                .text(String.format(pattern, vars))
                .chatId(message.getChatId().toString())
                .replyToMessageId(message.getMessageId())
                .disableWebPagePreview(hidePreview);

        if (markdown) sendMessageBuilder.parseMode(MARKDOWN);

        sender.execute(sendMessageBuilder.build());
    }

    public static void sendMessage(
            final AbsSender sender,
            final Message message,
            final String pattern,
            Object... vars
    ) throws TelegramApiException {
        sendMessageMarkdown(sender, message, pattern, false, false, vars);
    }
}
