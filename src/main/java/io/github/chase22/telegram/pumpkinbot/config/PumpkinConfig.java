package io.github.chase22.telegram.pumpkinbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class PumpkinConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumpkinConfig.class);

    private final String botUsername;
    private final String botToken;
    private final String redisUrl;
    private final int port;
    private final String externalUrl;

    public PumpkinConfig() {
        botToken = getFromEnvironment("BOT_TOKEN");

        botUsername = getFromEnvironment("BOT_USERNAME", "PumpkinBotBot");
        redisUrl = getFromEnvironment("REDIS_URL" , "https://localhost:6379");
        port = Integer.parseInt(getFromEnvironment("PORT"));

        externalUrl = System.getenv("BOT_EXTERNAL_URL");
    }

    @NotNull
    public String getBotUsername() {
        return botUsername;
    }

    @NotNull
    public String getBotToken() {
        return botToken;
    }

    @NotNull
    public String getRedisUrl() {
        return redisUrl;
    }

    public int getPort() {
        return port;
    }

    public boolean isWebhook() {
        return externalUrl != null;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    private String getFromEnvironment(@NotNull String key) {
        return getFromEnvironment(key, null);
    }

    private String getFromEnvironment(@NotNull String key, @Nullable String defaultValue) {
        return Optional.ofNullable(System.getenv(key))
                .flatMap(s -> {
                    LOGGER.info("Read " + key + " = " + s + " from environment");
                    return Optional.of(s);
                })
                .or(() -> {
                    LOGGER.info(key + " not found in enviroment. Returning default: " + defaultValue);
                    return Optional.ofNullable(defaultValue);
                })
                .orElseThrow(() -> new IllegalArgumentException("No value for key " + key));
    }
}
