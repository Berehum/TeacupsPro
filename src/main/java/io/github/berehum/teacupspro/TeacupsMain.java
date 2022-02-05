package io.github.berehum.teacupspro;

import io.github.berehum.teacupspro.attraction.TeacupManager;
import io.github.berehum.teacupspro.command.CommandManager;
import io.github.berehum.teacupspro.dependencies.PacketHandler;
import io.github.berehum.teacupspro.dependencies.PlaceholderApi;
import io.github.berehum.teacupspro.listeners.PlayerListener;
import io.github.berehum.teacupspro.show.ShowManager;
import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionTypeRegistry;
import io.github.berehum.teacupspro.utils.Version;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TeacupsMain extends JavaPlugin {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    private static TeacupsMain instance;
    private ShowActionTypeRegistry showActionTypeRegistry;

    private ShowManager showManager;
    private TeacupManager teacupManager;
    private PacketHandler packetHandler;

    private boolean placeholderApiEnabled = false;

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
        showActionTypeRegistry = new ShowActionTypeRegistry(getLogger());
        showActionTypeRegistry.registerTypes();
    }

    @Override
    public void onEnable() {
        setInstance(this);
        loadConfig();

        calculateDependencies();

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

    private void calculateDependencies() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!pluginManager.isPluginEnabled("ProtocolLib")) {
            this.getLogger().log(Level.WARNING, "Disabling plugin, ProtocolLib isn't enabled.");
            this.setEnabled(false);
        }

        if (getConfig().getBoolean("check-protocollib-version")) {
            //Checks if protocollib version is above 4.7.0 because I depend on features added in that version.
            //Will probably add a utility for this in the future or something.
            String protocolLibVersion = pluginManager.getPlugin("ProtocolLib").getDescription().getVersion();

            String[] splitted = protocolLibVersion.split("\\.");

            boolean valid = true;

            int major = 0;
            int minor = 0;
            try {
                major = Integer.parseInt(splitted[0]);
                minor = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException e) {
                valid = false;
            }

            if (major < 4 || (major == 4 && minor < 7)) {
                valid = false;
            }

            if (!valid) {
                this.getLogger().log(Level.WARNING, String.format("Disabling plugin, invalid ProtocolLib version detected: %s.", protocolLibVersion));
                this.setEnabled(false);
            }
        }

        placeholderApiEnabled = pluginManager.isPluginEnabled("PlaceholderApi");
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
        return format(player, true, input);
    }

    public String format(Player player, boolean color, String input) {
        if (placeholderApiEnabled) {
            input = PlaceholderApi.setPlaceholders(player, input);
        }
        return color(input);
    }

    public String color(String input) {
        if (Version.v1_16_R1.isLower(Version.Current)) {
            Matcher match = pattern.matcher(input);
            while (match.find()) {
                String color = input.substring(match.start(), match.end());
                input = input.replace(color, ChatColor.of(color).toString());
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

    public ShowActionTypeRegistry getShowActionTypes() {
        return showActionTypeRegistry;
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiEnabled;
    }
}