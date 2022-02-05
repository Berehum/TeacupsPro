package io.github.berehum.teacupspro.events;

import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionTypeRegistry;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegisterShowActionTypesEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final ShowActionTypeRegistry showActionTypeRegistry;

    public RegisterShowActionTypesEvent(ShowActionTypeRegistry showActionTypeRegistry) {
        this.showActionTypeRegistry = showActionTypeRegistry;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ShowActionTypeRegistry getShowActionTypeRegistry() {
        return showActionTypeRegistry;
    }
}
