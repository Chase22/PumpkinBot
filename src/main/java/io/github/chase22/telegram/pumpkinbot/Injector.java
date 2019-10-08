package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.sender.LongPollingSender;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class Injector {
    public PumpkinBot pumpkinBot;
    public PumpkinConfig pumpkinConfig;
    public PumpkinStorage pumpkinStorage;
    public LanguageHandler languageHandler;
    public CommandHandler commandHandler;
    public AbsSender sender;

    public Injector initialize() {
        languageHandler = new LanguageHandler();
        pumpkinConfig = new PumpkinConfig();

        sender = new LongPollingSender(pumpkinConfig);

        pumpkinStorage = new PumpkinStorage(pumpkinConfig);

        pumpkinBot = new PumpkinBot(sender, languageHandler, pumpkinStorage);

        commandHandler = new CommandHandler(sender, languageHandler, pumpkinStorage);

        pumpkinBot.setCommandHandler(commandHandler);

        return this;
    }
}
