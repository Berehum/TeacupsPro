package io.github.berehum.teacups.utils.wrappers;

import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import java.awt.image.renderable.ParameterBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WrapperPlayServerEntityEquipment extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_EQUIPMENT;

    public WrapperPlayServerEntityEquipment() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityEquipment(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Entity ID.
     * <p>
     * Notes: entity's ID
     *
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set Entity ID.
     *
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param event - the packet event.
     * @return The spawned entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    public void setSlotStack(ItemSlot itemSlot, ItemStack itemStack) {
        setSlotStackPairList(Collections.singletonList(new Pair<>(itemSlot, itemStack)));
    }

    public void setSlotStackPairList(List<Pair<ItemSlot, ItemStack>> listPair) {
        handle.getSlotStackPairLists().write(0, listPair);
    }

    public List<Pair<ItemSlot, ItemStack>> getSlotStackPairList() {
        return handle.getSlotStackPairLists().read(0);
    }
}
