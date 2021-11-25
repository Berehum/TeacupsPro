package io.github.berehum.teacups.attraction.components.armorstands;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.berehum.teacups.utils.wrappers.WrappedDataWatcherBuilder;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Seat extends Model {

    private boolean locked = false;

    public Seat(Location location, ItemStack itemStack) {
        this((int) (Math.random() * Integer.MAX_VALUE), UUID.randomUUID(),
                new WrappedDataWatcherBuilder().setInvisible(true).setSmall(true).toWrappedDataWatcher(),
                location, itemStack);
    }

    public Seat(int entityId, UUID uuid, WrappedDataWatcher dataWatcher, Location location, ItemStack itemStack) {
        super(entityId, uuid, dataWatcher, location, itemStack);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
