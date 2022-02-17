package io.github.berehum.teacupspro.dependencies;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.TeacupManager;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.attraction.components.armorstands.Seat;
import io.github.berehum.teacupspro.api.events.PlayerSeatEvent;
import io.github.berehum.teacupspro.utils.wrappers.WrapperPlayClientSteerVehicle;
import io.github.berehum.teacupspro.utils.wrappers.WrapperPlayClientUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketHandler {
    private final TeacupsMain plugin;
    private final ProtocolManager manager;

    public PacketHandler(TeacupsMain plugin) {
        this.plugin = plugin;
        manager = ProtocolLibrary.getProtocolManager();
    }

    public void addPacketListeners() {
        final TeacupManager teacupManager = plugin.getTeacupManager();

        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
                if (packet.getType() == EnumWrappers.EntityUseAction.ATTACK) return;
                Player player = event.getPlayer();
                if (player.isSneaking()) return;

                for (Teacup teacup : teacupManager.getTeacups().values()) {
                    for (Seat seat : teacup.getSeats()) {
                        if (seat == null || seat.getEntityId() != packet.getTargetID()) continue;
                        if (seat.isLocked() || seat.getPlayer() != null) return;

                        PlayerSeatEvent playerSeatEvent = new PlayerSeatEvent(PlayerSeatEvent.SeatAction.ENTER, seat, player);
                        playerSeatEvent.setCancelled(seat.isLocked() || seat.getPlayer() != null);
                        Bukkit.getPluginManager().callEvent(playerSeatEvent);
                        if (event.isCancelled()) return;

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            seat.mount(event.getPlayer());
                            teacup.autoStart(PacketHandler.this.plugin);
                        });
                        return;
                    }
                }

            }
        });

        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event.getPacket());
                if (!packet.isUnmount()) return;
                Player player = event.getPlayer();

                for (Seat seat : teacupManager.getSeats()) {
                    if (seat == null || seat.getPlayer() != player) continue;

                    PlayerSeatEvent playerSeatEvent = new PlayerSeatEvent(PlayerSeatEvent.SeatAction.LEAVE, seat, player);
                    playerSeatEvent.setCancelled(seat.isLocked());
                    Bukkit.getPluginManager().callEvent(playerSeatEvent);
                    if (event.isCancelled()) return;
                    Bukkit.getScheduler().runTask(plugin, seat::dismount);
                    return;
                }

            }
        });
    }

    public void removePacketListeners() {
        manager.removePacketListeners(plugin);
    }

}
