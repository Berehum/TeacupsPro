package io.github.berehum.teacups.listeners;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.TeacupManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final TeacupsMain plugin;

    public PlayerListener(TeacupsMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        final TeacupManager teacupManager = plugin.getTeacupManager();
        teacupManager.getSeats().forEach(seat -> seat.spawn(event.getPlayer()));

    }

    @EventHandler
    private void onLeave(PlayerQuitEvent event) {
        final TeacupManager teacupManager = plugin.getTeacupManager();
        teacupManager.getSeats().forEach(seat -> seat.remove(event.getPlayer()));
    }

}
