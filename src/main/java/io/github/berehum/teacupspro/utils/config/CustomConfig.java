package io.github.berehum.teacupspro.utils.config;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

public class CustomConfig {
    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration configuration;

    public CustomConfig(JavaPlugin plugin, String name) {
        this(plugin, new File(plugin.getDataFolder(), name));
    }

    public CustomConfig(JavaPlugin plugin, File file) {
        this.plugin = plugin;
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
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, String.format("Could not save %s", file.getName()), e);
        }
    }

    public void saveDefaultConfig() {
        saveDefaultConfig(file.getName());
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

        createNewFile(false);

        try (OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, String.format("Could not save %s to %s", resourcePath, file), e);
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
                //won't do anything
            }
        }
    }

    public boolean createNewFile(boolean replaceExisting) {
        boolean success = true;
        try {
            if (!file.getParentFile().exists() && !file.mkdirs()) {
                success = false;
            }
            if (file.exists() && replaceExisting) {
                Files.delete(file.toPath());
            }
            if (!file.createNewFile()) success = false;
        } catch (IOException exception) {
            success = false;
            exception.printStackTrace();
        }
        return success;
    }
}
