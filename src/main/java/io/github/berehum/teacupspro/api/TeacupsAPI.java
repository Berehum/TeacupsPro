package io.github.berehum.teacupspro.api;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Cart;
import io.github.berehum.teacupspro.attraction.components.CartGroup;
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

    public Optional<Teacup> getTeacup(Player player) {
        List<Teacup> teacups = getTeacups(player.getLocation(), 30);
        for (Teacup teacup : teacups) {
            if (teacup == null || !teacup.getPlayers().contains(player)) continue;
            return Optional.of(teacup);
        }
        return Optional.empty();
    }

    public Optional<CartGroup> getCartGroup(Player player) {
        Optional<Teacup> optTeacup = getTeacup(player);
        if (optTeacup.isPresent()) {
            Teacup teacup = optTeacup.get();

            for (CartGroup cartGroup : teacup.getCartGroups().values()) {
                if (cartGroup == null || !cartGroup.getPlayers().contains(player)) continue;
                return Optional.of(cartGroup);
            }

        }
        return Optional.empty();
    }

    public Optional<Cart> getCart(Player player) {
        Optional<CartGroup> optCartGroup = getCartGroup(player);
        if (optCartGroup.isPresent()) {
            CartGroup cartGroup = optCartGroup.get();

            for (Cart cart : cartGroup.getCarts().values()) {
                if (cart == null || !cart.getPlayers().contains(player)) continue;
                return Optional.of(cart);
            }

        }
        return Optional.empty();
    }

    public Optional<Seat> getSeat(Player player) {
        Optional<Cart> optCart = getCart(player);
        if (optCart.isPresent()) {
            Cart cart = optCart.get();

            for (Seat seat : cart.getSeats()) {
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
