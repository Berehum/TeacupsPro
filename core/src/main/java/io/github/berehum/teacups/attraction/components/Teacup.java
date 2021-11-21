package io.github.berehum.teacups.attraction.components;

import io.github.berehum.teacups.attraction.components.armorstands.Model;
import io.github.berehum.teacups.attraction.components.armorstands.Seat;
import io.github.berehum.teacups.utils.CustomConfig;
import io.github.berehum.teacups.utils.ItemBuilder;
import io.github.berehum.teacups.utils.MathUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Teacup {

    private final CustomConfig customConfig;

    private final String id;

    private final double radius;
    private final Location location;
    private final Map<String, CartGroup> cartGroups = new HashMap<>();

    private final int autoStartDelay;
    private final List<String> startCommands;
    private final List<String> stopCommands;

    private int rpm = 0;
    private double rotation = 0.0;
    private boolean locked = false;
    private boolean commenceStart = false;
    private boolean active = false;

    public Teacup(String id, CustomConfig customConfig) {
        this.id = id;
        this.customConfig = customConfig;
        FileConfiguration config = customConfig.getConfig();
        this.location = new Location(Bukkit.getWorld(config.getString("settings.location.world")),
                config.getDouble("settings.location.x"), config.getDouble("settings.location.y"), config.getDouble("settings.location.z"));

        this.radius = config.getDouble("settings.radius");
        this.autoStartDelay = config.getInt("settings.auto-start-delay");
        this.startCommands = config.getStringList("settings.commands.start");
        this.stopCommands = config.getStringList("settings.commands.stop");

        ConfigurationSection groupSection = config.getConfigurationSection("cartgroups");
        if (groupSection == null) return;
        for (String groupKey : groupSection.getKeys(false)) {

            ConfigurationSection cartsSection = groupSection.getConfigurationSection(groupKey + ".carts");
            if (cartsSection == null) continue;
            Map<String, Cart> carts = new HashMap<>();

            Model groupModel = null;
            if (groupSection.getString(groupKey+".model.type") != null) {
                Material groupModelMaterial = Material.matchMaterial(groupSection.getString(groupKey+".model.type"));
                int groupModelData = groupSection.getInt(groupKey+".model.modeldata");
                groupModel = new Model(location, new ItemBuilder(groupModelMaterial).setModelData(groupModelData).toItemStack());
            }

            for (String cartKeys : cartsSection.getKeys(false)) {
                List<Seat> seats = new ArrayList<>();

                Model cartModel = null;
                if (cartsSection.getString(cartKeys+".model.type") != null) {
                    Material cartModelMaterial = Material.matchMaterial(Objects.requireNonNull(cartsSection.getString(cartKeys+".model.type")));
                    int cartModelData = groupSection.getInt(cartKeys+".model.modeldata");
                    cartModel = new Model(location, new ItemBuilder(cartModelMaterial).setModelData(cartModelData).toItemStack());
                }

                for (int i = 0; i < cartsSection.getInt(cartKeys + ".seats"); i++) {
                    seats.add(new Seat(location));
                }

                carts.put(cartKeys, new Cart(cartKeys, location, cartsSection.getDouble(cartKeys + ".radius"), cartModel, seats));
            }

            cartGroups.put(groupKey, new CartGroup(groupKey, location, groupSection.getDouble(groupKey + ".radius"), groupModel, carts));

        }

    }

    public boolean init() {
        if (cartGroups.isEmpty() || location == null) return false;
        List<CartGroup> cartGroups = new ArrayList<>(this.cartGroups.values());
        for (int i = 0; i < cartGroups.size(); i++) {
            CartGroup group = cartGroups.get(i);
            group.setLocation(MathUtils.drawPoint(location, radius, i, cartGroups.size(), rotation, 0f));
            group.init();
        }
        updateChildLocations();
        return true;
    }

    public void disable() {
        cartGroups.values().forEach(CartGroup::disable);
    }

    public boolean start(boolean override) {
        if (!override && active) {
            return false;
        }
        active = true;
        final ConsoleCommandSender sender = Bukkit.getConsoleSender();
        startCommands.forEach(command -> Bukkit.dispatchCommand(sender, command));
        return true;
    }

    public void autoStart(JavaPlugin plugin) {
        if (commenceStart || active) return;
        commenceStart = true;
        new BukkitRunnable() {
            int index = autoStartDelay;
            @Override
            public void run() {
                Set<Player> players = getPlayersOnRide();
                if (players.size() == 0) {
                    commenceStart = false;
                    this.cancel();
                }
                if (index <= 0) {
                    start(false);
                    commenceStart = false;
                    this.cancel();
                }
                for (Player player : players) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Ride is starting in " + index + " second(s)!"));
                }
                index--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public boolean stop(boolean override) {
        if (!override && !active) {
            return false;
        }
        active = false;
        final ConsoleCommandSender sender = Bukkit.getConsoleSender();
        stopCommands.forEach(command -> Bukkit.dispatchCommand(sender, command));
        return true;
    }

    public void reveal(Player player) {
        getSeats().forEach(seat -> seat.spawn(player));
        getModels().forEach(model -> model.spawn(player));
    }

    public void hide(Player player) {
        getSeats().forEach(seat -> seat.remove(player));
        getModels().forEach(model -> model.remove(player));
    }

    public String getId() {
        return id;
    }


    public Set<Player> getPlayersOnRide() {
        Set<Player> players = new HashSet<>();
        cartGroups.values().forEach(cartGroup -> players.addAll(cartGroup.getPlayersInCartGroup()));
        return players;
    }

    public Map<String, CartGroup> getCartGroups() {
        return cartGroups;
    }

    public List<Model> getModels() {
        List<Model> models = new ArrayList<>();
        cartGroups.values().forEach(cartGroup -> models.addAll(cartGroup.getModels()));
        return models;
    }

    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        cartGroups.values().forEach(cartGroup -> seats.addAll(cartGroup.getSeats()));
        return seats;
    }

    public Location getLocation() {
        return location;
    }

    public void updateChildLocations() {
        List<CartGroup> cartGroups = new ArrayList<>(this.cartGroups.values());
        for (int i = 0; i < cartGroups.size(); i++) {
            CartGroup group = cartGroups.get(i);
            group.setLocation(MathUtils.drawPoint(location, radius, i, cartGroups.size(), rotation, 0f));
            group.updateChildLocations();
        }
    }

    public double getRadius() {
        return radius;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation % (2 * Math.PI);
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public CustomConfig getCustomConfig() {
        return customConfig;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        if (this.locked == locked) return;
        this.locked = locked;
        getSeats().forEach(seat -> seat.setLocked(locked));
    }

    public boolean isActive() {
        return active;
    }

    public int getAutoStartDelay() {
        return autoStartDelay;
    }
}
