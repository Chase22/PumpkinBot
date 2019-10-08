package io.github.chase22.telegram.pumpkinbot.config;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class PumpkinConfig {
    private final String botUsername;
    private final String botToken;
    private final String redisUrl;

    public PumpkinConfig() {
        botToken = getFromEnvironment("BOT_TOKEN");
        botUsername = getFromEnvironment("BOT_USERNAME", "PumpkinBotBot");
        redisUrl = getFromEnvironment("REDIS_URL" , "https://localhost:6379");
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

    private String getFromEnvironment(@NotNull String key) {
        return getFromEnvironment(key, null);
    }

    private String getFromEnvironment(@NotNull String key, @Nullable String defaultValue) {
        return Optional.ofNullable(System.getenv(key))
                .or(() -> Optional.ofNullable(defaultValue))
                .orElseThrow(() -> new IllegalArgumentException("No value for key " + key));
    }
}
