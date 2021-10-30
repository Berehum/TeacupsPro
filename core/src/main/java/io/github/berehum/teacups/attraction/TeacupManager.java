package io.github.berehum.teacups.attraction;

import io.github.berehum.teacups.attraction.components.Cart;
import io.github.berehum.teacups.attraction.components.CartGroup;
import io.github.berehum.teacups.attraction.components.Seat;
import io.github.berehum.teacups.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class TeacupManager {
    private final JavaPlugin plugin;
    private final Map<String, Teacup> teacupsAttractions = new HashMap<>();

    public TeacupManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        loadTeacups(plugin.getDataFolder().getAbsolutePath() + "/teacups");
        Bukkit.getScheduler().runTaskTimer(plugin, () -> updateTeacups(1), 0L, 1L);
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
            Iterator<File> iterator = FileUtils.iterateFiles(directory, new String[]{"yml"}, false);
            while (iterator.hasNext()) {
                File currentFile = iterator.next();
                loadTeacup(new CustomConfig(plugin, currentFile));
            }
        });
    }

    public void loadTeacup(CustomConfig config) {

        String name = config.getFile().getName();
        String finalName = name.substring(0, name.length()-4);

        Bukkit.getScheduler().runTask(plugin, () -> {
            Teacup oldTeacup = teacupsAttractions.get(finalName);
            teacupsAttractions.remove(finalName);
            if (oldTeacup != null) {
                oldTeacup.disable();
            }
            Teacup teacup = new Teacup(finalName, config);
            if (teacup.init()) teacupsAttractions.put(finalName, teacup);
        });
    }

    public void shutdown() {
        teacupsAttractions.values().forEach(Teacup::disable);
        teacupsAttractions.clear();
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

    //O(n^3)
    public void updateTeacups(int tickDelay) {
        for (Teacup teacup : teacupsAttractions.values()) {
            float tcRpm = teacup.getRpm();
            if (tcRpm != 0) {
                teacup.setRotation(teacup.getRotation() + getDeltaRotation(tickDelay, tcRpm));
            }
            for (CartGroup cartGroup : teacup.getCartGroups().values()) {
                float cgRpm = cartGroup.getRpm();
                if (cgRpm != 0) {
                    cartGroup.setRotation(cartGroup.getRotation() + getDeltaRotation(tickDelay, cgRpm));
                }

                for (Cart cart : cartGroup.getCarts().values()) {
                    float cRpm = cart.getRpm();
                    if (cRpm != 0) {
                        cart.setRotation(cart.getRotation() + getDeltaRotation(tickDelay, cRpm));
                    }
                }
            }
            teacup.updateChildLocations();
        }
    }

    private double getDeltaRotation(int tickdelay, float rpm) {
        return rpm / (1200.0 / tickdelay);
    }


}
