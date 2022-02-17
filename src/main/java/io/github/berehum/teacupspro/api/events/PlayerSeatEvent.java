package io.github.berehum.teacupspro.api.events;

import io.github.berehum.teacupspro.attraction.components.armorstands.Seat;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerSeatEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final SeatAction seatAction;
    private final Seat seat;
    private final Player player;
    private float steerAmount;
    private boolean cancelled = false;

    public PlayerSeatEvent(SeatAction seatAction, Seat seat, Player player, float steerAmount) {
        this.seatAction = seatAction;
        this.seat = seat;
        this.player = player;
        this.steerAmount = steerAmount;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public SeatAction getSeatAction() {
        return seatAction;
    }

    public Seat getSeat() {
        return seat;
    }

    public Player getPlayer() {
        return player;
    }

    public float getSteerAmount() {
        return steerAmount;
    }

    public void setSteerAmount(float steerAmount) {
        this.steerAmount = steerAmount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public enum SeatAction {
        ENTER, LEAVE, STEER
    }

}
