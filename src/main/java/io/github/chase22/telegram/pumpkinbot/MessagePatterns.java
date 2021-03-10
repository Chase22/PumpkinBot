package io.github.chase22.telegram.pumpkinbot;

import java.util.StringJoiner;

public class MessagePatterns {
    public static final String WELCOME_PATTERN = "This bot will now count your pumpkins in %d languages. %n%n" +
            "powered by: [Translatr](https://translatr.varunmalhotra.xyz";
    public static final String COUNT_PATTERN = "Current pumpkin count is: %d";
    public static final String STOP_PATTERN = "Shutting down";
    public static final String NOT_STARTED_PATTERN = "Bot is not started. Use /start";
    public static final String LANGUAGES_PATTERN = "This bot supports the following languages: %n %s";
    public static final String RESET_PATTERN = "Resetting";
    public static final String HELP_PATTERN = new StringJoiner("\n")
                    .add("/help - displays this message")
                    .add("/start - starts the bot")
                    .add("/stop - stops the bot")
                    .add("/count - displays the current count")
                    .add("/reset - resets the counter to 0")
                    .add("/languages - shows all supported languages").toString();
    public static final String ALREADY_STARTED_PATTERN = "Bot is already running";
}
