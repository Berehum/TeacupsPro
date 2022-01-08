package io.github.berehum.teacups.utils.wrappers;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WrappedDataWatcherBuilder {
    private final WrappedDataWatcher dataWatcher;

    public WrappedDataWatcherBuilder() {
        this(new WrappedDataWatcher());
    }

    public WrappedDataWatcherBuilder(WrappedDataWatcher dataWatcher) {
        this.dataWatcher = dataWatcher;
    }

    private void adjustBitmask(int index, boolean add, byte bitmask) {
        WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject(index, WrappedDataWatcher.Registry.get(Byte.class));
        Object object1 = dataWatcher.getObject(object.getIndex());
        byte totalBitmask = (object1 != null) ? (byte) object1 : 0;
        if (add) {
            totalBitmask = (byte) (totalBitmask | bitmask);
        } else {
            totalBitmask = (byte) (totalBitmask & ~(bitmask));
        }
        dataWatcher.setObject(object, totalBitmask);
    }

    public WrappedDataWatcherBuilder setBitmask(int index, byte @NotNull [] bitmasks) {
        byte bitmask = 0;
        for (byte mask : bitmasks) {
            bitmask = (byte) (bitmask | mask);
        }
        setBitmask(index, bitmask);
        return this;
    }

    public WrappedDataWatcherBuilder setBitmask(int index, byte bitmask) {
        WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject(index, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(object, bitmask);
        return this;
    }

    public WrappedDataWatcherBuilder setInt(int index, int integer) {
        WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject(index, WrappedDataWatcher.Registry.get(int.class));
        dataWatcher.setObject(object, integer);
        return this;
    }

    public WrappedDataWatcherBuilder setBoolean(int index, boolean bool) {
        WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject(index, WrappedDataWatcher.Registry.get(boolean.class));
        dataWatcher.setObject(object, bool);
        return this;
    }

    public WrappedDataWatcherBuilder setPose(int index, EnumWrappers.EntityPose pose) {
        WrappedDataWatcher.WrappedDataWatcherObject object = new WrappedDataWatcher.WrappedDataWatcherObject(index, WrappedDataWatcher.Registry.get(EnumWrappers.EntityPose.class));
        dataWatcher.setObject(object, pose);
        return this;
    }

    //Entity DataWatcher

    public WrappedDataWatcherBuilder setOnFire(boolean onFire) {
        adjustBitmask(0, onFire, (byte) 0x01);
        return this;
    }

    public WrappedDataWatcherBuilder setCrouching(boolean crouching) {
        adjustBitmask(0, crouching, (byte) 0x02);
        return this;
    }

    public WrappedDataWatcherBuilder setSprinting(boolean sprinting) {
        adjustBitmask(0, sprinting, (byte) 0x08);
        return this;
    }

    public WrappedDataWatcherBuilder setSwimming(boolean swimming) {
        adjustBitmask(0, swimming, (byte) 0x10);
        return this;
    }

    public WrappedDataWatcherBuilder setInvisible(boolean invisible) {
        adjustBitmask(0, invisible, (byte) 0x20);
        return this;
    }

    public WrappedDataWatcherBuilder setGlowing(boolean glowing) {
        adjustBitmask(0, glowing, (byte) 0x40);
        return this;
    }

    public WrappedDataWatcherBuilder isFlyingWithElytra(boolean flying) {
        adjustBitmask(0, flying, (byte) 0x80);
        return this;
    }

    public WrappedDataWatcherBuilder setAirTicks(int airTicks) {
        setInt(1, airTicks);
        return this;
    }

    /*
    public WrappedDataWatcherBuilder setCustomName(String customName) {
        setInt(2, airTicks);
        return this;
    }
     */

    public WrappedDataWatcherBuilder setCustomNameVisible(boolean visible) {
        setBoolean(3, visible);
        return this;
    }

    public WrappedDataWatcherBuilder setSilent(boolean silent) {
        setBoolean(4, silent);
        return this;
    }

    public WrappedDataWatcherBuilder setNoGravity(boolean noGravity) {
        setBoolean(5, noGravity);
        return this;
    }

    public WrappedDataWatcherBuilder setPose(EnumWrappers.EntityPose pose) {
        setPose(6, pose);
        return this;
    }

    public WrappedDataWatcherBuilder setTicksFrozenInPowderedSnow(int frozenTicks) {
        setInt(7, frozenTicks);
        return this;
    }

    //ArmorStand DataWatcher

    public WrappedDataWatcherBuilder setSmall(boolean small) {
        adjustBitmask(15, small, (byte) 0x01);
        return this;
    }

    public WrappedDataWatcher toWrappedDataWatcher() {
        return dataWatcher;
    }

    public List<WrappedWatchableObject> toWatchableObjects() {
        return dataWatcher.getWatchableObjects();
    }

}
