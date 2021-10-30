package io.github.berehum.teacups.attraction.components;


import io.github.berehum.teacups.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

// A cartgroup exists of multiple carts and is basically the plate where the carts are on
// https://stackoverflow.com/questions/24273990/calculating-evenly-spaced-points-on-the-perimeter-of-a-circle
public class CartGroup {

    private final String id;
    private final Map<String, Cart> carts;
    private final double radius;

    //Location of the center of the cartgroup
    private Location location;
    private float rpm = 0.0F;
    private double rotation = 0.0;

    public CartGroup(String id, Location location, double radius, Map<String, Cart> carts) {
        this.id = id;
        this.radius = radius;
        this.carts = carts;
        this.location = location;
    }

    public void init() {
        carts.values().forEach(Cart::init);
    }

    public void disable() {
        carts.values().forEach(Cart::disable);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation % (2 * Math.PI);
    }

    public void updateChildLocations() {
        List<Cart> carts = new ArrayList<>(this.carts.values());
        for (int i = 0; i < carts.size(); i++) {
            Cart cart = carts.get(i);
            cart.setLocation(MathUtils.drawPoint(location, radius, i, carts.size(), rotation));
            cart.updateChildLocations();
        }
    }

    public Set<Player> getPlayersInCartGroup() {
        Set<Player> players = new HashSet<>();
        carts.values().forEach(cart -> players.addAll(cart.getPlayersInCart()));
        return players;
    }

    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        carts.values().forEach(cart -> seats.addAll(cart.getSeats()));
        return seats;
    }

    public Map<String, Cart> getCarts() {
        return carts;
    }

    public float getRpm() {
        return rpm;
    }

    public void setRpm(float rpm) {
        this.rpm = rpm;
    }

    public String getId() {
        return id;
    }
}
