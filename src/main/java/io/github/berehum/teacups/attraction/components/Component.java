package io.github.berehum.teacups.attraction.components;

import io.github.berehum.teacups.attraction.components.armorstands.Model;
import io.github.berehum.teacups.attraction.components.armorstands.Seat;
import io.github.berehum.teacups.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.Player;

import java.util.*;

//@todo perhaps use a parameter here for better support?
public abstract class Component {

    private final String id;
    private final Map<String, Component> subComponents;
    private final double radius;
    private final @Nullable
    Model model;

    //Location of the center of the component
    private Location location;
    private int rpm = 0;
    private double circleOffset = 0.0;
    private float rotation = 0.0F;

    protected Component(String id, Location location, double radius, Model model, Map<String, Component> subComponents) {
        this.id = id;
        this.location = location;
        this.radius = radius;
        this.model = model;
        this.subComponents = subComponents;
    }

    public void init() {
        subComponents.values().forEach(Component::init);
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::spawn);
    }

    public void disable() {
        subComponents.values().forEach(Component::disable);
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::remove);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }

    public void updateChildLocations() {
        location = LocationUtils.setDirection(location, 0, rotation);
        List<Component> components = new ArrayList<>(this.subComponents.values());
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            component.setLocation(LocationUtils.drawPoint(location, radius, i, components.size(), circleOffset));
            component.updateChildLocations();
        }
        if (model != null && model.getItemStack() != null) {
            model.teleport(location);
        }
    }

    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        subComponents.values().forEach(component -> players.addAll(component.getPlayers()));
        return players;
    }

    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        subComponents.values().forEach(component -> seats.addAll(component.getSeats()));
        return seats;
    }

    public Model getModel() {
        return model;
    }

    public List<Model> getModels() {
        List<Model> models = new ArrayList<>();
        if (model != null && model.getItemStack() != null) {
            models.add(model);
        }
        subComponents.values().forEach(component -> models.addAll(component.getModels()));
        return models;
    }

    public Map<String, Component> getSubComponents() {
        return subComponents;
    }

    public double getRadius() {
        return radius;
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

    public double getCircleOffset() {
        return circleOffset;
    }

    public void setCircleOffset(double circleOffset) {
        this.circleOffset = circleOffset % (2 * Math.PI);
    }

    public void changeCircleOffset(double circleOffset) {
        setCircleOffset(this.circleOffset + circleOffset);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation % 360;
    }

    public void changeRotation(float rotation) {
        setRotation(this.rotation + rotation);
    }
}
