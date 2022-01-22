package io.github.berehum.teacups.attraction.components;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.armorstands.Model;
import io.github.berehum.teacups.attraction.components.armorstands.PacketArmorStand;
import io.github.berehum.teacups.attraction.components.armorstands.Seat;
import io.github.berehum.teacups.show.Show;
import io.github.berehum.teacups.utils.ItemBuilder;
import io.github.berehum.teacups.utils.LocationUtils;
import io.github.berehum.teacups.utils.SeatLayout;
import io.github.berehum.teacups.utils.config.CustomConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Teacup {

    public static final String NAME = "teacup";

    private final CustomConfig customConfig;

    private final String id;

    private final double radius;
    private final Location location;
    private final Map<String, CartGroup> cartGroups = new HashMap<>();

    private final int autoStartDelay;
    private final String defaultShow;

    private Show activeShow;

    private int rpm = 0;
    private double circleOffset = 0.0;
    private boolean locked = false;
    private boolean commenceStart = false;
    private boolean active = false;

    public Teacup(String id, CustomConfig customConfig) {
        this.id = id;
        this.customConfig = customConfig;
        FileConfiguration config = customConfig.getConfig();
        this.location = new Location(
                Bukkit.getWorld(config.getString("settings.location.world")),
                config.getDouble("settings.location.x"), config.getDouble("settings.location.y"),
                config.getDouble("settings.location.z"));

        this.radius = config.getDouble("settings.radius");
        this.autoStartDelay = config.getInt("settings.show.auto-start-delay");
        this.defaultShow = config.getString("settings.show.default-show");

        ConfigurationSection groupSection = config.getConfigurationSection("cartgroups");
        if (groupSection == null) return;
        for (String groupKey : groupSection.getKeys(false)) {

            ConfigurationSection cartsSection = groupSection.getConfigurationSection(groupKey + ".carts");
            if (cartsSection == null) continue;
            Map<String, Cart> carts = new HashMap<>();

            Model groupModel = null;
            ItemBuilder groupModelItem = ItemBuilder.fromConfig(groupSection.getConfigurationSection(groupKey + ".model"));
            if (groupModelItem != null) {
                groupModel = new Model(location, groupModelItem.toItemStack());
            }

            for (String cartKeys : cartsSection.getKeys(false)) {

                ItemBuilder cartModelItem = ItemBuilder.fromConfig(cartsSection.getConfigurationSection(cartKeys + ".model"));
                Model cartModel = (cartModelItem != null) ? new Model(location, cartModelItem.toItemStack()) : null;

                SeatLayout layout = SeatLayout.readFromConfig(cartsSection.getConfigurationSection(cartKeys + ".seats"));
                if (layout == null) layout = SeatLayout.getDefault();

                carts.put(cartKeys, new Cart(cartKeys, location, cartsSection.getDouble(cartKeys + ".radius"), cartModel, layout));
            }
            cartGroups.put(groupKey, new CartGroup(groupKey, location, groupSection.getDouble(groupKey + ".radius"), groupModel, carts));

        }

    }

    public boolean init() {
        if (cartGroups.isEmpty() || location == null) return false;
        List<CartGroup> cartGroupsList = new ArrayList<>(this.cartGroups.values());
        double deltaRot = 360.0 / cartGroupsList.size();
        for (int i = 0; i < cartGroupsList.size(); i++) {
            CartGroup group = cartGroupsList.get(i);
            //rotates the to face the middle of the circle
            group.setRotation((float) (deltaRot*i) + 90);
            //space the groups out over the circle of the teacup.
            group.setLocation(LocationUtils.drawPoint(location, radius, i, cartGroupsList.size(), circleOffset));
            group.init();
        }
        updateChildLocations();
        return true;
    }

    public void disable() {
        cartGroups.values().forEach(CartGroup::disable);
    }

    public boolean start(TeacupsMain plugin, boolean override) {
        Show show = plugin.getShowManager().getShowMap().get(defaultShow);
        return start(plugin, show, override);
    }

    public boolean start(JavaPlugin plugin, Show show, boolean override) {
        if (show == null) {
            plugin.getLogger().warning(String.format("Started (default)show for teacup '%s' does not exist.", id));
            return false;
        }

        if (!override && active) {
            return false;
        }
        active = true;
        show.startShow(plugin, this);
        return true;
    }

    public void autoStart(TeacupsMain plugin) {
        if (commenceStart || active || autoStartDelay < 0) return;
        commenceStart = true;
        new BukkitRunnable() {
            int index = autoStartDelay;

            @Override
            public void run() {
                Set<Player> players = getPlayers();
                if (players.isEmpty() || isActive()) {
                    commenceStart = false;
                    this.cancel();
                }
                if (index <= 0) {
                    start(plugin, false);
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
        activeShow.stopShow(this);
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

    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        cartGroups.values().forEach(cartGroup -> players.addAll(cartGroup.getPlayers()));
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
        List<CartGroup> cartGroupsList = new ArrayList<>(this.cartGroups.values());
        for (int i = 0; i < cartGroupsList.size(); i++) {
            CartGroup group = cartGroupsList.get(i);
            group.setLocation(LocationUtils.drawPoint(location, radius, i, cartGroupsList.size(), circleOffset));
            group.updateChildLocations();
        }
    }

    public double getRadius() {
        return radius;
    }

    public double getCircleOffset() {
        return circleOffset;
    }

    public void setCircleOffset(double circleOffset) {
        this.circleOffset = circleOffset % (2 * Math.PI);
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

    public void kickAll() {
        getSeats().forEach(PacketArmorStand::dismount);
    }

    public boolean isActive() {
        return active;
    }

    public int getAutoStartDelay() {
        return autoStartDelay;
    }

    public Show getActiveShow() {
        return activeShow;
    }

    public void setActiveShow(Show activeShow) {
        this.activeShow = activeShow;
    }

}
