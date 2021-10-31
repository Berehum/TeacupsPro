package io.github.berehum.teacups;

import io.github.berehum.teacups.attraction.TeacupManager;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class TeacupsMain extends JavaPlugin {

    private static TeacupsMain INSTANCE;

    private TeacupManager teacupManager;

    private PacketHandler packetHandler;

    public static TeacupsMain getInstance() {
        return INSTANCE;
    }

    public static void setInstance(TeacupsMain instance) {
        if (TeacupsMain.INSTANCE != null)
            throw new UnsupportedOperationException("Instance already exists");
        TeacupsMain.INSTANCE = instance;
    }

    @Override
    public void onEnable() {
        setInstance(this);
        teacupManager = new TeacupManager(this);
        teacupManager.init();
        packetHandler = new PacketHandler(this);
        packetHandler.addPacketListeners();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        try {
            new CommandManager(this);
        } catch (Exception e) {
            this.getLogger().log(Level.WARNING, "Failed to initialize command manager", e);
            this.setEnabled(false);
            return;
        }
    }

    @Override
    public void onDisable() {
        teacupManager.shutdown();
    }

    public TeacupManager getTeacupManager() {
        return teacupManager;
    }

}