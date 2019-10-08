package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;

public class Injector {
    public PumpkinBot pumpkinBot;
    public PumpkinConfig pumpkinConfig;
    public PumpkinStorage pumpkinStorage;
    public LanguageHandler languageHandler;
    public CommandHandler commandHandler;

    public Injector initialize() {
        languageHandler = new LanguageHandler();
        pumpkinConfig = new PumpkinConfig();

        pumpkinStorage = new PumpkinStorage(pumpkinConfig);

        pumpkinBot = new PumpkinBot(pumpkinConfig, languageHandler, pumpkinStorage);

        commandHandler = new CommandHandler(pumpkinBot, languageHandler, pumpkinStorage);

        pumpkinBot.setCommandHandler(commandHandler);

        return this;
    }
}
