package io.github.berehum.teacups;

import cloud.commandframework.paper.PaperCommandManager;
import io.github.berehum.teacups.attraction.TeacupManager;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.utils.Version;
import io.github.berehum.teacups.utils.nms.INMSHandler;
import io.github.berehum.teacups.utils.nms.v1_17_R1.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class TeacupsMain extends JavaPlugin {

    private static TeacupsMain INSTANCE;

    private INMSHandler nmsHandler;
    private TeacupManager teacupManager;

    public static TeacupsMain getInstance() {
        return INSTANCE;
    }

    public static void setInstance(TeacupsMain instance) {
        if (TeacupsMain.INSTANCE != null) throw new UnsupportedOperationException("Instance already exists");
        TeacupsMain.INSTANCE = instance;
    }

    @Override
    public void onLoad() {
        setInstance(this);
        if (!setupNMS(Version.Current)) {
            getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        teacupManager = new TeacupManager(this);
        teacupManager.init();

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

    public boolean setupNMS(Version version) {
        switch (version) {
            case v1_17_R1:
                nmsHandler = new NMSHandler();
                break;
        }

        return nmsHandler != null;
    }

    public INMSHandler getNmsHandler() {
        return nmsHandler;
    }

    public TeacupManager getTeacupManager() {
        return teacupManager;
    }

}
