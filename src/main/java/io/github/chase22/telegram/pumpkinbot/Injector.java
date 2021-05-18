package io.github.chase22.telegram.pumpkinbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.chase22.telegram.pumpkinbot.commands.*;
import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import io.github.chase22.telegram.pumpkinbot.language.LanguageHandler;
import io.github.chase22.telegram.pumpkinbot.sender.LongPollingSender;
import io.github.chase22.telegram.pumpkinbot.sender.TelegramSender;
import io.github.chase22.telegram.pumpkinbot.sender.UpdateProvider;
import io.github.chase22.telegram.pumpkinbot.sender.WebhookUpdateProvider;
import io.github.chase22.telegram.pumpkinbot.storage.PumpkinStorage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.util.List;

public class Injector {
    public ObjectMapper objectMapper;
    public PumpkinBot pumpkinBot;
    public PumpkinConfig pumpkinConfig;
    public PumpkinStorage pumpkinStorage;
    public LanguageHandler languageHandler;
    public AbsSender sender;
    public UpdateProvider updateProvider;

    public Injector initialize() {
        languageHandler = new LanguageHandler();
        pumpkinConfig = new PumpkinConfig();
        objectMapper = new ObjectMapper();

        if (pumpkinConfig.isWebhook()) {
            sender = new TelegramSender(pumpkinConfig);
            updateProvider = new WebhookUpdateProvider(
                    pumpkinConfig.getExternalUrl(),
                    sender,
                    pumpkinConfig.getPort(),
                    objectMapper
            );
        } else {
            LongPollingSender longPollingSender = new LongPollingSender(pumpkinConfig);
            this.sender = longPollingSender;
            updateProvider = longPollingSender;
        }

        pumpkinStorage = new PumpkinStorage(pumpkinConfig);

        //Commands
        HelpCommand helpCommand = new HelpCommand();
        LanguageCommand languageCommand = new LanguageCommand(languageHandler);

        DumpCommand dumpCommand = new DumpCommand(pumpkinStorage);
        StopCommand stopCommand = new StopCommand(pumpkinStorage);
        CountCommand countCommand = new CountCommand(pumpkinStorage);

        StartCommand startCommand = new StartCommand(pumpkinStorage, languageHandler, countCommand);
        ResetCommand resetCommand = new ResetCommand(pumpkinStorage, countCommand);

        pumpkinBot = new PumpkinBot(sender, updateProvider, languageHandler, pumpkinStorage, pumpkinConfig, List.of(
                helpCommand, languageCommand, dumpCommand, stopCommand, countCommand, startCommand, resetCommand
        ));

        return this;
    }
}
