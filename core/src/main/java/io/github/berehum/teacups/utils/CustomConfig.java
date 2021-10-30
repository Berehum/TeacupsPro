package io.github.berehum.teacups.utils;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

public class CustomConfig {
    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    private FileConfiguration configuration;

    public CustomConfig(JavaPlugin plugin, String name) {
        this(plugin, new File(plugin.getDataFolder(), name));
    }

    public CustomConfig(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.name = file.getName();
        this.file = file;
    }

    public void reloadConfig() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        if (configuration == null) {
            reloadConfig();
        }
        return configuration;
    }

    public File getFile() {
        return file;
    }

    public void saveConfig() {
        if (configuration == null || file == null) return;
        try {
            getConfig().save(file);
        } catch (IOException exception) {
            // Error
        }
    }

    public void saveDefaultConfig() {
        saveDefaultConfig(name);
    }

    public void saveDefaultConfig(String resourcePath) {
        if (file.exists()) {
            saveConfig();
            return;
        }
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + this.file);
        }
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        try {
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (IOException var10) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + resourcePath + " to " + file, var10);
        }

    }

    public void createNewFile(boolean replaceExisting) {
        try {
            if (file.exists() && replaceExisting) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
