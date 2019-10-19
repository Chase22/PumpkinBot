package io.github.chase22.telegram.pumpkinbot.storage;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import redis.clients.jedis.Jedis;

import java.net.URI;

public class PumpkinStorage {
    private final Jedis client;

    public PumpkinStorage(final PumpkinConfig pumpkinConfig) {
        client = new Jedis(URI.create(pumpkinConfig.getRedisUrl()));
    }

    public int getForChat(long chatId) {
        return Integer.parseInt(client.get(Long.toString(chatId)));
    }

    public void setForChat(long chatId, int value) {
        client.set(Long.toString(chatId), Integer.toString(value));
    }

    public void removeChat(long chatId) {
        client.del(Long.toString(chatId));
    }

    public void increase(final Long chatId, final int amount) {
        setForChat(chatId, getForChat(chatId) + amount);
    }

    public boolean exists(final Long chatId) {
        return client.exists(Long.toString(chatId));
    }
}
