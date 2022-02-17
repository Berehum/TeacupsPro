package io.github.berehum.teacupspro.api;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.attraction.components.armorstands.Seat;
import io.github.berehum.teacupspro.exceptions.ClashingActionTypesException;
import io.github.berehum.teacupspro.show.Show;
import io.github.berehum.teacupspro.show.actions.type.ShowActionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class TeacupsAPI {
    private final TeacupsMain plugin;

    public TeacupsAPI(TeacupsMain plugin) {
        this.plugin = plugin;
    }

    public void registerShowAction(@NonNull ShowActionType showActionType) {
        try {
            plugin.getShowActionTypes().register(showActionType);
        } catch (ClashingActionTypesException e) {
            plugin.getLogger().warning(String.format("[API] Show Action Type '%s' already exists.", showActionType.getName()));
        }
    }

    public Set<ShowActionType> getShowActions() {
        return plugin.getShowActionTypes().getTypeSet();
    }

    public Map<String, Teacup> getTeacups() {
        return Collections.unmodifiableMap(plugin.getTeacupManager().getTeacups());
    }

    public List<Teacup> getTeacups(Location location, double radius) {
        return plugin.getTeacupManager().getTeacupsInProximity(location, radius);
    }

    public Optional<Seat> getSeat(Player player) {
        List<Teacup> teacups = getTeacups(player.getLocation(), 30);
        for (Teacup teacup : teacups) {
            for (Seat seat : teacup.getSeats()) {
                if (seat == null || seat.getPlayer() != player) continue;
                return Optional.of(seat);
            }
        }
        return Optional.empty();
    }

    public Map<String, Show> getShows() {
        return plugin.getShowManager().getShowMap();
    }

}
