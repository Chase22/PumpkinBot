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
    private final String keystorePassword;
    private final String internalUrl;
    private final String externalUrl;
    private final String keystoreResource;
    private final String certificateResource;
    private final int port;

    public PumpkinConfig() {
        botToken = getFromEnvironment("BOT_TOKEN");
        keystorePassword = getFromEnvironment("BOT_KEYSTORE_PASS");

        botUsername = getFromEnvironment("BOT_USERNAME", "PumpkinBotBot");
        redisUrl = getFromEnvironment("REDIS_URL" , "https://localhost:6379");
        internalUrl = getFromEnvironment("BOT_INTERNAL_URL", "localhost");
        externalUrl = getFromEnvironment("BOT_URL", "");
        port = Integer.parseInt(getFromEnvironment("PORT"));

        keystoreResource = getFromEnvironment("BOT_KEYSTORE_RESSOURCE", "/pumpkinbot.jsk");
        certificateResource = getFromEnvironment("BOT_CERTIFICATE_RESSOURCE", "/pumpkinbot.pem");
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


    public String getExternalUrl() {
        return externalUrl;
    }

    public int getPort() {
        return port;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public boolean isWebhook() {
        return !externalUrl.isBlank();
    }

    public String getKeystoreResource() {
        return keystoreResource;
    }

    public String getCertificateResource() {
        return certificateResource;
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
