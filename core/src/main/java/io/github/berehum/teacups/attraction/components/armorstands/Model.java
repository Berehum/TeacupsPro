package io.github.berehum.teacups.attraction.components.armorstands;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.berehum.teacups.utils.wrappers.WrappedDataWatcherBuilder;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayServerEntityEquipment;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayServerEntityMetadata;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Model extends PacketArmorStand {

    private final ItemStack itemStack;

    public Model(Location location, ItemStack model) {
        this((int) (Math.random() * Integer.MAX_VALUE), UUID.randomUUID(),
                new WrappedDataWatcherBuilder().setInvisible(true).toWrappedDataWatcher(),
                location, model);
    }

    public Model(int entityId, UUID uuid, WrappedDataWatcher dataWatcher, Location location, ItemStack model) {
        super(entityId, uuid, dataWatcher, location);
        this.itemStack = model;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void sendMetaDataPacket(Player player, int entityId) {
        super.sendMetaDataPacket(player, entityId);
        WrapperPlayServerEntityEquipment packet2 = new WrapperPlayServerEntityEquipment();
        packet2.setEntityID(entityId);
        //model here
        packet2.setSlotStack(EnumWrappers.ItemSlot.HEAD, getItemStack());
        packet2.sendPacket(player);
    }
}
