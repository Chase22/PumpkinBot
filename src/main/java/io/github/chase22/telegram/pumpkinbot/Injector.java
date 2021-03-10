package io.github.chase22.telegram.pumpkinbot;

import io.github.chase22.telegram.pumpkinbot.commands.*;
import io.github.chase22.telegram.pumpkinbot.config.FilesConfig;
import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.sender.LongPollingSender;
import io.github.chase22.telegram.pumpkinbot.sender.WebhookSender;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Injector {
    public PumpkinBot pumpkinBot;
    public PumpkinConfig pumpkinConfig;
    public FilesConfig filesConfig;
    public PumpkinStorage pumpkinStorage;
    public LanguageHandler languageHandler;
    public AbsSender sender;

    public Injector initialize() throws IOException {
        languageHandler = new LanguageHandler();
        pumpkinConfig = new PumpkinConfig();

        if (pumpkinConfig.isWebhook()) {
            sender = new WebhookSender(pumpkinConfig);
        } else {
            sender = new LongPollingSender(pumpkinConfig);
        }

        filesConfig = new FilesConfig(pumpkinConfig);
        pumpkinStorage = new PumpkinStorage(pumpkinConfig);

        //Commands
        HelpCommand helpCommand = new HelpCommand();
        LanguageCommand languageCommand = new LanguageCommand(languageHandler);

        DumpCommand dumpCommand = new DumpCommand(pumpkinStorage);
        StopCommand stopCommand = new StopCommand(pumpkinStorage);
        CountCommand countCommand = new CountCommand(pumpkinStorage);

        StartCommand startCommand = new StartCommand(pumpkinStorage, languageHandler, countCommand);
        ResetCommand resetCommand = new ResetCommand(pumpkinStorage, countCommand);

        pumpkinBot = new PumpkinBot(sender, languageHandler, pumpkinStorage, pumpkinConfig, List.of(
                helpCommand, languageCommand, dumpCommand, stopCommand, countCommand, startCommand, resetCommand
        ));

        return this;
    }
}
