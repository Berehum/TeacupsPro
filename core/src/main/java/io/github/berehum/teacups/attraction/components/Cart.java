package io.github.berehum.teacups.attraction.components;

import io.github.berehum.teacups.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Actual teacup with multiple chair slots.
public class Cart {

    private final String id;
    private final List<Seat> seats;
    private final double radius;
    //Location of the middle of the cart.

    private Location location;
    private int rpm = 0;
    private double rotation = 0.0;

    public Cart(String id, Location location, double radius, List<Seat> seats) {
        this.id = id;
        this.location = location;
        this.radius = radius;
        this.seats = seats;
    }

    public void init() {
        Bukkit.getOnlinePlayers().forEach(player -> seats.forEach(seat -> seat.spawn(player)));
    }

    public void disable() {
        Bukkit.getOnlinePlayers().forEach(player -> seats.forEach(seat -> seat.remove(player)));
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
        seats.forEach(seat -> players.add(seat.getPlayer()));
        return players;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public void updateChildLocations() {
        for (int i = 0; i < seats.size(); i++) {
            seats.get(i).teleport(MathUtils.drawPoint(location, radius, i + 1, seats.size() + 1, rotation));
        }
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
