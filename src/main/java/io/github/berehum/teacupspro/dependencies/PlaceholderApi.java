package io.github.berehum.teacupspro.dependencies;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderApi {

    private PlaceholderApi() {}

    public static String setPlaceholders(Player player, String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
