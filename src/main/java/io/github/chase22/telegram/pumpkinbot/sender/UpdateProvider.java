package io.github.chase22.telegram.pumpkinbot.sender;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

public interface UpdateProvider {
    void setUpdateConsumer(final Consumer<Update> updateConsumer);
}
