package io.github.berehum.teacupspro.utils.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

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

    /**
     * @param itemSlot  slot to put the item
     * @param itemStack item to put in the slot
     * @apiNote Support for SlotStackPair was added in ProtocolLib version 4.7.0
     */
    public void setSlotStack(ItemSlot itemSlot, ItemStack itemStack) {
        setSlotStackPairList(Collections.singletonList(new Pair<>(itemSlot, itemStack)));
    }

    public List<Pair<ItemSlot, ItemStack>> getSlotStackPairList() {
        return handle.getSlotStackPairLists().read(0);
    }

    public void setSlotStackPairList(List<Pair<ItemSlot, ItemStack>> listPair) {
        //method was added in a development build of ProtocolLib.
        handle.getSlotStackPairLists().write(0, listPair);
    }
}
