package io.github.chase22.telegram.pumpkinbot.storage;

import io.github.chase22.telegram.pumpkinbot.config.PumpkinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;

public class PumpkinStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumpkinStorage.class);
    private static JedisPool pool;

    public PumpkinStorage(final PumpkinConfig pumpkinConfig) {
        URI redisURI = URI.create(pumpkinConfig.getRedisUrl());
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        pool = new JedisPool(poolConfig, redisURI);
    }

    public int getForChat(long chatId) {
        LOGGER.info("Get for chat " + chatId);
        return Integer.parseInt(pool.getResource().get(Long.toString(chatId)));
    }

    public void setForChat(long chatId, int value) {
        LOGGER.info("Set value " + value + " for chatid " + chatId);
        pool.getResource().set(Long.toString(chatId), Integer.toString(value));
    }

    public void removeChat(long chatId) {
        pool.getResource().del(Long.toString(chatId));
    }

    public void increase(final Long chatId, final int amount) {
        if (amount == 0) return;
        setForChat(chatId, getForChat(chatId) + amount);
    }

    public boolean exists(final Long chatId) {
        return pool.getResource().exists(Long.toString(chatId));
    }

}
