package io.github.chase22.telegram.pumpkinbot;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.github.chase22.telegram.pumpkinbot.MessageUtils.sendMessage;
import static java.util.Arrays.asList;
import static org.telegram.telegrambots.meta.api.objects.MemberStatus.ADMINISTRATOR;
import static org.telegram.telegrambots.meta.api.objects.MemberStatus.CREATOR;

public class AdminUtils {
    private static final String NO_ADMIN_PATTERN = "Only admins are allowed to execute this command";

    public static boolean checkAdmin(AbsSender sender, Message message) throws TelegramApiException {
        if (message.isUserMessage()) {
            return true;
        } else if (isAdmin(sender, message)) {
            return true;
        } else {
            sendMessage(sender, message, NO_ADMIN_PATTERN);
            return false;
        }
    }

    public static boolean isAdmin(AbsSender sender, Message message) throws TelegramApiException {
        final GetChatMember getChatMember = new GetChatMember(
                message.getChatId().toString(),
                message.getFrom().getId()
        );

        final ChatMember chatMember = sender.execute(getChatMember);

        return asList(ADMINISTRATOR, CREATOR).contains(chatMember.getStatus());
    }
}
