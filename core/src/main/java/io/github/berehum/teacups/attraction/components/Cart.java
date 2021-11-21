package io.github.berehum.teacups.attraction.components;

import io.github.berehum.teacups.attraction.components.armorstands.Model;
import io.github.berehum.teacups.attraction.components.armorstands.Seat;
import io.github.berehum.teacups.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.Player;

import java.util.*;

// Actual teacup with multiple chair slots.
public class Cart {

    private final String id;
    private final List<Seat> seats;
    private final double radius;
    private final @Nullable Model model;
    //Location of the middle of the cart.

    private Location location;
    private int rpm = 0;
    private double rotation = 0.0;

    public Cart(String id, Location location, double radius, Model model, List<Seat> seats) {
        this.id = id;
        this.location = location;
        this.radius = radius;
        this.model = model;
        this.seats = seats;
    }

    public void init() {
        Bukkit.getOnlinePlayers().forEach(player -> seats.forEach(seat -> seat.spawn(player)));
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::spawn);
    }

    public void disable() {
        Bukkit.getOnlinePlayers().forEach(player -> seats.forEach(seat -> seat.remove(player)));
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::remove);
    }

    public double getRadius() {
        return radius;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Player> getPlayersInCart() {
        Set<Player> players = new HashSet<>();
        for (Seat seat : seats) {
            Optional<Player> player = Optional.ofNullable(seat.getPlayer());
            if (!player.isPresent()) continue;
            players.add(player.get());
        }
        return players;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Model> getModels() {
        List<Model> models = new ArrayList<>();
        if (model != null && model.getItemStack() != null) {
            models.add(model);
        }
        return models;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public void updateChildLocations() {
        for (int i = 0; i < seats.size(); i++) {
            seats.get(i).teleport(MathUtils.drawPoint(location, radius, i + 1, seats.size() + 1, rotation, (float) rotation));
        }
        if (model != null && model.getItemStack() != null) model.teleport(location);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation % (2 * Math.PI);
    }

    public String getId() {
        return id;
    }
}