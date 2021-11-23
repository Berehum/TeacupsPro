package io.github.berehum.teacups.utils;

import io.github.berehum.teacups.attraction.components.armorstands.Seat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatLayout {
    private final String layout;
    private final Map<Character, ItemStack> referenceMap = new HashMap<>();

    public SeatLayout(String layout) {
        this.layout = layout;
    }

    public void setMaterial(char c, ItemStack itemStack) {
        referenceMap.put(c, itemStack);
    }

    public boolean isEmpty(int index) {
        char character = layout.toCharArray()[index];
        ItemStack itemStack = referenceMap.get(character);
        return itemStack == null;
    }

    public List<Seat> getSeats(Location location) {
        List<Seat> seats = new ArrayList<>();
        for (char character : layout.toCharArray()) {
            ItemStack itemStack = referenceMap.get(character);
            if (itemStack == null) continue;
            seats.add(new Seat(location, itemStack));
        }
        return seats;
    }

    public int size() {
        return layout.length();
    }

    public static SeatLayout getDefault() {
        SeatLayout seatLayout = new SeatLayout("aaaaaa");
        seatLayout.setMaterial('c', new ItemBuilder(Material.OAK_TRAPDOOR).toItemStack());
        return seatLayout;
    }

    public static SeatLayout readFromConfig(ConfigurationSection section) {
        if (section == null) return null;
        String layout = section.getString("seatlayout");
        if (layout == null || layout.isEmpty()) layout = "aaaa";
        SeatLayout seatLayout = new SeatLayout(layout);
        ConfigurationSection models = section.getConfigurationSection("models");
        if (models == null) {
            return seatLayout;
        }
        for (String character : models.getKeys(false)) {
            ItemBuilder itemBuilder = ItemBuilder.fromConfig(models.getConfigurationSection(character));
            if (itemBuilder == null) continue;
            seatLayout.setMaterial(character.toCharArray()[0], itemBuilder.toItemStack());
        }
        return seatLayout;
    }

}
