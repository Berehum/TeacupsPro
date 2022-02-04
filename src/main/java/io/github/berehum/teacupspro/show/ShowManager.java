package io.github.berehum.teacupspro.show;

import io.github.berehum.teacupspro.show.reader.ShowFileReader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShowManager {
    private final JavaPlugin plugin;
    private final Map<String, Show> showMap = new HashMap<>();

    public ShowManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init(boolean sendProblemsToConsole) {
        loadShows(plugin.getDataFolder().getAbsolutePath() + "/shows", sendProblemsToConsole);
    }

    public void shutdown() {
        showMap.values().forEach(Show::disable);
        showMap.clear();
    }

    //Recurring method
    public synchronized void loadShows(String path, boolean sendProblemsToConsole) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            File directory = new File(path);
            File[] foundDirectories = directory.listFiles(File::isDirectory);
            if (foundDirectories != null)
                Arrays.stream(foundDirectories).forEach(d -> loadShows(d.getAbsolutePath(), sendProblemsToConsole));
            if (!directory.exists())
                directory.mkdir();

            File[] files = directory.listFiles((dir, name) -> name.endsWith(".yml"));
            if (files == null) return;
            for (File currentFile : files) {
                loadShow(currentFile);
            }

            if (sendProblemsToConsole) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        ShowFileReader.showProblems(Bukkit.getConsoleSender(), ShowFileReader.getConfigProblems())
                );
            }
        });
    }

    public void loadShow(File file) {
        String name = file.getName();
        String finalName = name.substring(0, name.length() - 4);

        Show show = (showMap.containsKey(finalName) ? showMap.get(finalName) : new Show());
        if (!show.load(file)) return;
        showMap.put(finalName, show);
    }

    public Optional<Show> getShow(String name) {
        return Optional.ofNullable(showMap.get(name));
    }

    public Map<String, Show> getShowMap() {
        return showMap;
    }

}
