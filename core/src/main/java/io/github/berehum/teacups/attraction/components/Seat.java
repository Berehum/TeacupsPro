package io.github.berehum.teacups.attraction.components;

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

//packets :D
public class Seat {

    private final int entityId;
    private final Set<Player> recipients = new HashSet<>();

    private Location location;
    private Player mountedPlayer;
    private boolean locked = false;

    public Seat(Location location) {
        this((int)(Math.random() * Integer.MAX_VALUE), location);
    }

    public Seat(int entityId, Location location) {
        this.entityId = entityId;
        this.location = location;
    }

    public void spawn(Player player) {
        if (recipients.contains(player)) return;
        sendSpawnPacket(player, location, entityId);
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

    public void teleport(Location location) {
        if (this.location.equals(location)) return;

        if (this.location.distance(location) > 8) {
            this.location = location.clone();
            recipients.forEach(player -> sendTeleportPacket(player, location, entityId));
            return;
        }
        Vector deltaVector = location.toVector().subtract(this.location.toVector());
        this.location = location.clone();
        recipients.forEach(player -> sendMovePacket(player, deltaVector, entityId));
    }

    public Location getLocation() {
        return location;
    }

    public void mount(Player mountedPlayer) {
        if (this.mountedPlayer != null) {
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

    private void sendSpawnPacket(Player player, Location location, int entityId) {
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity();
        packet.setEntityID(entityId);
        packet.setType(EntityType.ARMOR_STAND);
        packet.setX(location.getX());
        packet.setY(location.getY());
        packet.setZ(location.getZ());
        packet.sendPacket(player);

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
        packet.sendPacket(player);
    }

    private void sendMovePacket(Player player, Vector delta, int entityId) {
        WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove();
        packet.setEntityID(entityId);
        packet.setVector(delta);
        packet.sendPacket(player);
    }

    private void sendMountPacket(Player player, int entityId, int passengerId) {
        WrapperPlayServerMount packet = new WrapperPlayServerMount();
        packet.setEntityID(entityId);
        packet.setPassengerIds(new int[]{passengerId});
        packet.sendPacket(player);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
