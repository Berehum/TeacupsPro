package io.github.berehum.teacups.attraction.components.armorstands;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.berehum.teacups.utils.wrappers.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PacketArmorStand {

    private final int entityId;
    private final UUID uuid;
    private final Set<Player> recipients = new HashSet<>();

    private Location location;
    private Player mountedPlayer;

    public PacketArmorStand(Location location) {
        this((int) (Math.random() * Integer.MAX_VALUE), UUID.randomUUID(), location);
    }

    public PacketArmorStand(int entityId, UUID uuid, Location location) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.location = location;
    }

    public void spawn(Player player) {
        if (recipients.contains(player)) return;
        sendSpawnPacket(player, location, entityId);
        sendMetaDataPacket(player, entityId);
        if (mountedPlayer != null) {
            sendMountPacket(player, entityId, mountedPlayer.getEntityId());
        }
        recipients.add(player);
    }

    public void remove(Player player) {
        if (!recipients.contains(player)) return;
        if (mountedPlayer == player) {
            dismount();
        }
        sendDestroyPacket(player, entityId);
        recipients.remove(player);
    }

    public Location getLocation() {
        return location;
    }

    public void teleport(Location location) {
        if (this.location.equals(location)) return;

        if (this.location.distance(location) > 8) {
            recipients.forEach(player -> sendTeleportPacket(player, location, entityId));
        } else {
            Vector deltaVector = location.toVector().subtract(this.location.toVector());
            recipients.forEach(player -> sendMovePacket(player, deltaVector, location.getPitch(), location.getYaw(), entityId));
        }
        if (mountedPlayer != null) {
            sendVehicleMovePacket(mountedPlayer, location);
        }
        this.location = location.clone();
    }

    public void mount(Player mountedPlayer) {
        if (this.mountedPlayer != null && this.mountedPlayer != mountedPlayer) {
            dismount();
        }
        this.mountedPlayer = mountedPlayer;
        mountedPlayer.setAllowFlight(true);
        mountedPlayer.setFlying(true);
        recipients.forEach(player -> {
            sendMountPacket(player, entityId, mountedPlayer.getEntityId());
        });
    }

    public void dismount() {
        if (mountedPlayer == null) return;
        mountedPlayer.setAllowFlight(mountedPlayer.getGameMode() == GameMode.CREATIVE || mountedPlayer.getGameMode() == GameMode.SPECTATOR);
        mountedPlayer.setFlying(false);
        Location playerLoc = mountedPlayer.getLocation();

        Location dismountLoc = location.clone();
        dismountLoc.setPitch(playerLoc.getPitch());
        dismountLoc.setYaw(playerLoc.getYaw());

        mountedPlayer.teleport(dismountLoc);
        mountedPlayer = null;
    }

    public Player getPlayer() {
        return mountedPlayer;
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUuid() {
        return uuid;
    }

    private void sendSpawnPacket(Player player, Location location, int entityId) {
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity();
        packet.setEntityID(entityId);
        packet.setType(EntityType.ARMOR_STAND);
        packet.setX(location.getX());
        packet.setY(location.getY());
        packet.setZ(location.getZ());
        packet.setYaw(location.getYaw());
        packet.setPitch(location.getPitch());
        packet.setUniqueId(uuid);
        packet.sendPacket(player);
    }

    public void sendMetaDataPacket(Player player, int entityId) {
        WrapperPlayServerEntityMetadata packet2 = new WrapperPlayServerEntityMetadata();
        //Set the entity to associate the packet with
        packet2.setEntityID(entityId);
        //Create a ProtocolLib WrappedDataWatcher
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        WrappedDataWatcher.WrappedDataWatcherObject isSmall = new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class));
        dataWatcher.setObject(isSmall, (byte) 0x01);

        packet2.setMetadata(dataWatcher.getWatchableObjects());
        packet2.sendPacket(player);
    }

    private void sendDestroyPacket(Player player, int entityId) {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntityIds(Collections.singletonList(entityId));
        packet.sendPacket(player);
    }

    private void sendTeleportPacket(Player player, Location location, int entityId) {
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
        packet.setEntityID(entityId);
        packet.setX(location.getX());
        packet.setY(location.getY());
        packet.setZ(location.getZ());
        packet.setPitch(location.getPitch());
        packet.setYaw(location.getYaw());
        packet.sendPacket(player);
    }

    private void sendMovePacket(Player player, Vector delta, float pitch, float yaw, int entityId) {
        WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove();
        packet.setEntityID(entityId);
        packet.setVector(delta);
        packet.setPitch(pitch);
        packet.setYaw(yaw);
        packet.sendPacket(player);
    }

    private void sendVehicleMovePacket(Player player, Location location) {
        WrapperPlayClientVehicleMove packet = new WrapperPlayClientVehicleMove();
        packet.setX(location.getX());
        packet.setY(location.getY());
        packet.setZ(location.getZ());
        packet.setPitch(location.getPitch());
        packet.setYaw(location.getYaw());
        packet.receivePacket(player);
    }

    private void sendMountPacket(Player player, int entityId, int passengerId) {
        WrapperPlayServerMount packet = new WrapperPlayServerMount();
        packet.setEntityID(entityId);
        packet.setPassengerIds(new int[]{passengerId});
        packet.sendPacket(player);
    }

}
