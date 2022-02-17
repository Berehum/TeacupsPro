package io.github.berehum.teacupspro.api.events;

import io.github.berehum.teacupspro.show.actions.type.ShowActionTypeRegistry;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegisterShowActionTypesEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final ShowActionTypeRegistry showActionTypeRegistry;

    public RegisterShowActionTypesEvent(ShowActionTypeRegistry showActionTypeRegistry) {
        this.showActionTypeRegistry = showActionTypeRegistry;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public ShowActionTypeRegistry getShowActionTypeRegistry() {
        return showActionTypeRegistry;
    }
}
