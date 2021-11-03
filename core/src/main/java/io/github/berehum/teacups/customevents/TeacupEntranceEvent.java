package io.github.berehum.teacups.customevents;

import io.github.berehum.teacups.attraction.components.Teacup;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeacupEntranceEvent extends Event {
    public static HandlerList HANDLERS = new HandlerList();

    private final Teacup teacup;
    private final Player player;

    public TeacupEntranceEvent(Teacup teacup, Player player) {
        this.teacup = teacup;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Teacup getTeacup() {
        return teacup;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
