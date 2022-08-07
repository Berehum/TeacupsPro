package io.github.berehum.teacupspro;

import io.github.berehum.teacupspro.api.TeacupsAPI;
import io.github.berehum.teacupspro.attraction.TeacupManager;
import io.github.berehum.teacupspro.command.CommandManager;
import io.github.berehum.teacupspro.dependencies.PacketHandler;
import io.github.berehum.teacupspro.dependencies.PlaceholderApi;
import io.github.berehum.teacupspro.listeners.PlayerListener;
import io.github.berehum.teacupspro.show.ShowManager;
import io.github.berehum.teacupspro.show.actions.type.ShowActionTypeRegistry;
import io.github.berehum.teacupspro.utils.Version;
import io.github.berehum.teacupspro.utils.config.CustomConfig;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TeacupsMain extends JavaPlugin {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    //beh stop static abuse -> it's fine in this instance because the instance is 'immutable'
    private static TeacupsMain instance;
    private ShowActionTypeRegistry showActionTypeRegistry;

    private ShowManager showManager;
    private TeacupManager teacupManager;
    private PacketHandler packetHandler;
    private TeacupsAPI teacupsAPI;

    private CommandManager commandManager;

    private boolean placeholderApiEnabled = false;

    public static TeacupsMain getInstance() {
        return instance;
    }

    public static void setInstance(TeacupsMain instance) {
        //Set new instance if there isn't an instance active.
        if (TeacupsMain.instance != null)
            throw new UnsupportedOperationException("Instance already exists");
        TeacupsMain.instance = instance;
    }

    @Override
    public void onLoad() {
        //Load the showactiontypes so that they can be used in the other classes
        showActionTypeRegistry = new ShowActionTypeRegistry(getLogger());
        showActionTypeRegistry.registerTypes();
    }

    @Override
    public void onEnable() {

        setInstance(this);

        loadConfig();

        calculateDependencies();

        try {
            commandManager = new CommandManager(this);
        } catch (Exception e) {
            this.getLogger().log(Level.WARNING, "Error whilst initializing command manager", e);
            this.stop("failed to initialize command manager");
            return;
        }

        showManager = new ShowManager(this);
        showManager.init(true);

        teacupManager = new TeacupManager(this);
        teacupManager.init();

        teacupsAPI = new TeacupsAPI(this);

        packetHandler = new PacketHandler(this);
        packetHandler.addPacketListeners();

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        try {
            getServer().getServicesManager().register(TeacupsAPI.class, teacupsAPI, this, ServicePriority.Normal);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void calculateDependencies() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!pluginManager.isPluginEnabled("ProtocolLib")) {
            this.stop("ProtocolLib isn't enabled.");
            return;
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
                this.stop(String.format("invalid ProtocolLib version detected: %s.", protocolLibVersion));
                return;
            }
        }

        placeholderApiEnabled = pluginManager.isPluginEnabled("PlaceholderApi");
    }

    @Override
    public void onDisable() {
        if (teacupManager != null) teacupManager.shutdown();
        if (showManager != null) showManager.shutdown();
        if (packetHandler != null) packetHandler.removePacketListeners();
        if (commandManager != null) {
            BukkitAudiences audiences = commandManager.getBukkitAudiences();
            if (audiences != null) {
                audiences.close();
            }
        }
        loadConfig();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        CustomConfig config = new CustomConfig(this, new File(getDataFolder().getAbsolutePath() + "/shows", "defaultshow.yml"));
        config.saveDefaultConfig("defaultshow.yml");
    }

    public void stop(String reason) {
        this.getLogger().log(Level.WARNING, "Disabling plugin; " + reason);
        this.setEnabled(false);
    }

    public String format(Player player, String input) {
        return format(player, true, input);
    }

    public String format(Player player, boolean color, String input) {
        if (placeholderApiEnabled) {
            input = PlaceholderApi.setPlaceholders(player, input);
        }
        if (color) {
            return color(input);
        }
        return input;
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

    public TeacupsAPI getTeacupsAPI() {
        return teacupsAPI;
    }

    public boolean isPlaceholderApiEnabled() {
        return placeholderApiEnabled;
    }
}