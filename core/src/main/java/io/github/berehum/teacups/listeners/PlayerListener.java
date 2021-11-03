package io.github.berehum.teacups.listeners;

import io.github.berehum.teacups.TeacupsMain;
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
        plugin.getTeacupManager().revealAll(event.getPlayer());
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent event) {
        plugin.getTeacupManager().hideAll(event.getPlayer());
    }

}
