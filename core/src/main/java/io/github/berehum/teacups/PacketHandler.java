package io.github.berehum.teacups;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.berehum.teacups.attraction.TeacupManager;
import io.github.berehum.teacups.attraction.components.Seat;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayClientSteerVehicle;
import io.github.berehum.teacups.utils.wrappers.WrapperPlayClientUseEntity;
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

                for (Seat seat : teacupManager.getSeats()) {
                    if (seat == null || seat.getEntityId() != packet.getTargetID()) continue;
                    if (seat.isLocked() || seat.getPlayer() != null) return;
                    Bukkit.getScheduler().runTask(plugin, () -> seat.mount(player));
                    return;
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
                    if (seat.isLocked()) return;
                    Bukkit.getScheduler().runTask(plugin, seat::dismount);
                    return;
                }

            }
        });
    }

}
