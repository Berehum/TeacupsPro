package io.github.berehum.teacups.customevents;

import io.github.berehum.teacups.attraction.Teacup;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeacupStartEvent extends Event {
    public static HandlerList HANDLERS = new HandlerList();

    private final Teacup teacup;

    public TeacupStartEvent(Teacup teacup) {
        this.teacup = teacup;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Teacup getTeacup() {
        return teacup;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
