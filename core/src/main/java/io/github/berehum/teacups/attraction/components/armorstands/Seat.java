package io.github.berehum.teacups.attraction.components.armorstands;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayServerEntityEquipment;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayServerEntityMetadata;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Seat extends Model {

    private boolean locked = false;

    public Seat(Location location, ItemStack itemStack) {
        super(location, itemStack);
    }

    public Seat(int entityId, UUID uuid, Location location, ItemStack itemStack) {
        super(entityId, uuid, location, itemStack);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }


    @Override
    public void sendMetaDataPacket(Player player, int entityId) {
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        //Set the entity to associate the packet with
        packet.setEntityID(entityId);
        //Create a ProtocolLib WrappedDataWatcher
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        WrappedDataWatcher.WrappedDataWatcherObject isSmall = new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(isSmall, (byte) 0x01);
        WrappedDataWatcher.WrappedDataWatcherObject isInvisible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(isInvisible, (byte) 0x20);

        packet.setMetadata(dataWatcher.getWatchableObjects());
        packet.sendPacket(player);

        WrapperPlayServerEntityEquipment packet2 = new WrapperPlayServerEntityEquipment();
        packet2.setEntityID(entityId);
        //model here
        packet2.setSlotStack(EnumWrappers.ItemSlot.HEAD, getItemStack());
        packet2.sendPacket(player);
    }

}
