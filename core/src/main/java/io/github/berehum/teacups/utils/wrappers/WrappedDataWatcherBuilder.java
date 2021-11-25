package io.github.berehum.teacups.utils.wrappers;

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

    public WrappedDataWatcherBuilder setInvisible(boolean invisible) {
        adjustBitmask(0, invisible, (byte) 0x20);
        return this;
    }

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
