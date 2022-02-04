package io.github.berehum.teacupspro.attraction;

import io.github.berehum.teacupspro.attraction.components.CartGroup;
import io.github.berehum.teacupspro.attraction.components.Component;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.attraction.components.armorstands.Model;
import io.github.berehum.teacupspro.attraction.components.armorstands.Seat;
import io.github.berehum.teacupspro.utils.config.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class TeacupManager {
    private final JavaPlugin plugin;
    private final Map<String, Teacup> teacupsAttractions = new HashMap<>();
    private BukkitTask updateTeacups;
    private BukkitTask packetRecipientUpdater;

    public TeacupManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        FileConfiguration config = plugin.getConfig();
        loadTeacups(plugin.getDataFolder().getAbsolutePath() + "/teacups");
        updateTeacups = updateTeacups(config.getInt("teacup.update-delay"));
        packetRecipientUpdater = updatePacketRecipients(config.getInt("packet-recipient-update-delay"));
    }

    //Recurring method
    public synchronized void loadTeacups(String path) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            File directory = new File(path);
            File[] foundDirectories = directory.listFiles(File::isDirectory);
            if (foundDirectories != null)
                Arrays.stream(foundDirectories).forEach(d -> loadTeacups(d.getAbsolutePath()));
            if (!directory.exists())
                directory.mkdir();

            File[] files = directory.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files == null) return;
            for (File currentFile : files) {
                loadTeacup(new CustomConfig(plugin, currentFile));
            }
        });
    }

    public void loadTeacup(CustomConfig config) {
        config.reloadConfig();
        String name = config.getFile().getName();
        String finalName = name.substring(0, name.length() - 4);

        Bukkit.getScheduler().runTask(plugin, () -> {
            Teacup oldTeacup = teacupsAttractions.get(finalName);
            if (oldTeacup != null) {
                oldTeacup.disable();
            }
            teacupsAttractions.remove(finalName);
            Teacup teacup = new Teacup(finalName, config);
            if (teacup.init()) teacupsAttractions.put(finalName, teacup);
        });
    }

    public void shutdown() {
        if (updateTeacups != null) updateTeacups.cancel();
        if (packetRecipientUpdater != null) packetRecipientUpdater.cancel();
        teacupsAttractions.values().forEach(Teacup::disable);
        teacupsAttractions.clear();
    }

    public void revealAll(Player player) {
        teacupsAttractions.values().forEach(teacup -> teacup.reveal(player));
    }

    public void hideAll(Player player) {
        teacupsAttractions.values().forEach(teacup -> teacup.hide(player));
    }

    public Optional<Teacup> getTeacup(String id) {
        return Optional.ofNullable(teacupsAttractions.get(id));
    }

    public Map<String, Teacup> getTeacups() {
        return teacupsAttractions;
    }

    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        teacupsAttractions.values().forEach(teacup -> seats.addAll(teacup.getSeats()));
        return seats;
    }

    public List<Model> getModels() {
        List<Model> models = new ArrayList<>();
        teacupsAttractions.values().forEach(teacup -> models.addAll(teacup.getModels()));
        return models;
    }

    //This method basically controls the teacup's movement
    public BukkitTask updateTeacups(int tickDelay) {
        return Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Teacup teacup : teacupsAttractions.values()) {
                int tcRpm = teacup.getRpm();
                if (tcRpm != 0) {
                    teacup.setCircleOffset(teacup.getCircleOffset() + getDeltaCircleOffset(tickDelay, tcRpm));
                }
                for (CartGroup cartGroup : teacup.getCartGroups().values()) {
                    int cgRpm = cartGroup.getRpm();
                    double cummulativeOffset = getDeltaCircleOffset(tickDelay, tcRpm) + getDeltaCircleOffset(tickDelay, cgRpm);
                    float cummulativeRotation = getDeltaRotation(tickDelay, tcRpm) + getDeltaRotation(tickDelay, cgRpm);

                    //setting cumulative offset and rotation since the components must stick together.
                    cartGroup.changeCircleOffset(cummulativeOffset);
                    cartGroup.changeRotation(cummulativeRotation);

                    for (Component cart : cartGroup.getSubComponents().values()) {
                        int cRpm = cart.getRpm();

                        //setting cumulative offset and rotation since the components must stick together.
                        cart.changeCircleOffset(cummulativeOffset + getDeltaCircleOffset(tickDelay, cRpm));
                        cart.changeRotation(cummulativeRotation + getDeltaRotation(tickDelay, cRpm));
                    }
                }
                teacup.updateChildLocations();
            }
        }, 0L, tickDelay);
    }

    public BukkitTask updatePacketRecipients(int tickDelay) {
        return Bukkit.getScheduler().runTaskTimer(plugin,
                () -> Bukkit.getOnlinePlayers().forEach(this::updatePacketRecipient), 0L, tickDelay);
    }

    public void updatePacketRecipient(Player player) {
        for (Teacup teacup : teacupsAttractions.values()) {
            if (player.getLocation().distance(teacup.getLocation()) < plugin.getConfig().getInt("teacup.visibility-distance")) {
                teacup.reveal(player);
                continue;
            }
            teacup.hide(player);
        }
    }

    private double getDeltaCircleOffset(int tickDelay, int rpm) {
        return (2 * Math.PI) * (rpm / (1200.0 / tickDelay));
    }

    private float getDeltaRotation(int tickDelay, int rpm) {
        return (float) ((360) * (rpm / (1200.0 / tickDelay)));
    }


}
