package io.github.berehum.teacups.events;

import io.github.berehum.teacups.show.reader.lines.type.ShowActionTypes;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegisterShowActionTypesEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final ShowActionTypes showActionTypes;

    public RegisterShowActionTypesEvent(ShowActionTypes showActionTypes) {
        this.showActionTypes = showActionTypes;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ShowActionTypes getShowActionTypes() {
        return showActionTypes;
    }
}
