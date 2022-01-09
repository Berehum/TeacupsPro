package io.github.berehum.teacups;

import io.github.berehum.teacups.attraction.TeacupManager;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.listeners.PlayerListener;
import io.github.berehum.teacups.show.ShowManager;
import io.github.berehum.teacups.show.reader.lines.type.ShowActionTypes;
import io.github.berehum.teacups.utils.Version;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TeacupsMain extends JavaPlugin {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    private static TeacupsMain instance;
    private ShowActionTypes showActionTypes;

    private ShowManager showManager;
    private TeacupManager teacupManager;
    private PacketHandler packetHandler;

    public static TeacupsMain getInstance() {
        return instance;
    }

    public static void setInstance(TeacupsMain instance) {
        if (TeacupsMain.instance != null)
            throw new UnsupportedOperationException("Instance already exists");
        TeacupsMain.instance = instance;
    }

    @Override
    public void onLoad() {
        showActionTypes = new ShowActionTypes();
        showActionTypes.registerTypes();
    }

    @Override
    public void onEnable() {
        setInstance(this);
        loadConfig();

        showManager = new ShowManager(this);
        showManager.init(true);

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
        }
    }

    @Override
    public void onDisable() {
        teacupManager.shutdown();
        showManager.shutdown();
        packetHandler.removePacketListeners();
        loadConfig();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

    }

    public String format(Player player, String input) {
        //do placeholder stuff here
        return color(input);
    }

    public String color(String input) {
        if (Version.V1_16_R1.isLower(Version.Current)) {
            Matcher match = pattern.matcher(input);
            while (match.find()) {
                String color = input.substring(match.start(), match.end());
                input = input.replace(color, ChatColor.of(color) + "");
                match = pattern.matcher(input);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public TeacupManager getTeacupManager() {
        return teacupManager;
    }

    public ShowManager getShowManager() {
        return showManager;
    }

    public ShowActionTypes getShowActionTypes() {
        return showActionTypes;
    }

}