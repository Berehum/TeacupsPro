package io.github.berehum.teacups.attraction.components;


import io.github.berehum.teacups.attraction.components.armorstands.Model;
import io.github.berehum.teacups.attraction.components.armorstands.Seat;
import io.github.berehum.teacups.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.Player;

import java.util.*;

public class CartGroup {

    private final String id;
    private final Map<String, Cart> carts;
    private final double radius;
    private final @Nullable Model model;

    //Location of the center of the cartgroup
    private Location location;
    private int rpm = 0;
    private double rotation = 0.0;

    public CartGroup(String id, Location location, double radius, Model model, Map<String, Cart> carts) {
        this.id = id;
        this.location = location;
        this.radius = radius;
        this.model = model;
        this.carts = carts;
    }

    public void init() {
        carts.values().forEach(Cart::init);
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::spawn);
    }

    public void disable() {
        carts.values().forEach(Cart::disable);
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::remove);
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
            cart.setLocation(MathUtils.drawPoint(location, radius, i, carts.size(), rotation, (float) rotation));
            cart.updateChildLocations();
        }
        if (model != null && model.getItemStack() != null) model.teleport(location);
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

    public List<Model> getModels() {
        List<Model> models = new ArrayList<>();
        if (model != null && model.getItemStack() != null) {
            models.add(model);
        }
        carts.values().forEach(cart -> models.addAll(cart.getModels()));
        return models;
    }

    public Map<String, Cart> getCarts() {
        return carts;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public String getId() {
        return id;
    }
}
