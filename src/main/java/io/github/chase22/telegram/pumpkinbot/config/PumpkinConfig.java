package io.github.chase22.telegram.pumpkinbot.config;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class PumpkinConfig {
    private final String botUsername;
    private final String botToken;
    private final String redisUrl;
    private final String keystorePassword;
    private final String url;
    private final String keystoreRessource;
    private final String certificateRessource;

    public PumpkinConfig() {
        botToken = getFromEnvironment("BOT_TOKEN");
        keystorePassword = getFromEnvironment("BOT_KEYSTORE_PASS");

        botUsername = getFromEnvironment("BOT_USERNAME", "PumpkinBotBot");
        redisUrl = getFromEnvironment("REDIS_URL" , "https://localhost:6379");
        url = getFromEnvironment("BOT_URL", "");

        keystoreRessource = getFromEnvironment("BOT_KEYSTORE_RESSOURCE", "/pumpkinbot.jsk");
        certificateRessource = getFromEnvironment("BOT_CERTIFICATE_RESSOURCE", "/pumpkinbot.pem");
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

    @NotNull
    public String getKeystorePassword() {
        return keystorePassword;
    }


    public String getUrl() {
        return url;
    }

    public boolean isWebhook() {
        return !url.isBlank();
    }

    public String getKeystoreRessource() {
        return keystoreRessource;
    }

    public String getCertificateRessource() {
        return certificateRessource;
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
