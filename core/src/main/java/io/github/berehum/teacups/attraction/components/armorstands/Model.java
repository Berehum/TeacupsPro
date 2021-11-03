package io.github.berehum.teacups.attraction.components.armorstands;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayServerEntityEquipment;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayServerEntityMetadata;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Model extends PacketArmorStand {

    private final ItemStack itemStack;

    public Model(Location location, ItemStack model) {
        super(location);
        this.itemStack = model;
    }

    public Model(int entityId, UUID uuid, Location location, ItemStack model) {
        super(entityId, uuid, location);
        this.itemStack = model;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void sendMetaDataPacket(Player player, int entityId) {
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        //Set the entity to associate the packet with
        packet.setEntityID(entityId);
        //Create a ProtocolLib WrappedDataWatcher
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        WrappedDataWatcher.WrappedDataWatcherObject isInvisible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(isInvisible, (byte) 0x20);

        packet.setMetadata(dataWatcher.getWatchableObjects());
        packet.sendPacket(player);

        WrapperPlayServerEntityEquipment packet2 = new WrapperPlayServerEntityEquipment();
        packet2.setEntityID(entityId);
        packet2.setSlotStack(EnumWrappers.ItemSlot.HEAD, itemStack);
        packet2.sendPacket(player);
    }
}
