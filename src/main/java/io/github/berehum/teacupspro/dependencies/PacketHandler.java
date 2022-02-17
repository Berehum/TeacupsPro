package io.github.berehum.teacupspro.dependencies;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.api.TeacupsAPI;
import io.github.berehum.teacupspro.attraction.TeacupManager;
import io.github.berehum.teacupspro.attraction.components.Cart;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.attraction.components.armorstands.Seat;
import io.github.berehum.teacupspro.api.events.PlayerSeatEvent;
import io.github.berehum.teacupspro.utils.wrappers.WrapperPlayClientSteerVehicle;
import io.github.berehum.teacupspro.utils.wrappers.WrapperPlayClientUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PacketHandler {
    private final TeacupsMain plugin;
    private final ProtocolManager manager;
    private final TeacupsAPI teacupsAPI;

    public PacketHandler(TeacupsMain plugin) {
        this.plugin = plugin;
        manager = ProtocolLibrary.getProtocolManager();
        teacupsAPI = plugin.getTeacupsAPI();
    }

    public void addPacketListeners() {
        final TeacupManager teacupManager = plugin.getTeacupManager();

        //Player click seat listener
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

                        PlayerSeatEvent playerSeatEvent = new PlayerSeatEvent(PlayerSeatEvent.SeatAction.ENTER, seat, player, 0);
                        playerSeatEvent.setCancelled(seat.isLocked() || seat.getPlayer() != null);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.getPluginManager().callEvent(playerSeatEvent);
                            if (event.isCancelled()) return;
                            seat.mount(event.getPlayer());
                            teacup.autoStart(PacketHandler.this.plugin);
                        });
                        return;
                    }
                }

            }
        });

        // Player leave seat and steer seat (cart) listener
        // @todo: optimizations (caching seats 'n stuff)
        manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event.getPacket());
                if (!packet.isUnmount() && packet.getSideways() == 0) return;
                Player player = event.getPlayer();
                Optional<Seat> optSeat = teacupsAPI.getSeat(player);
                if (!optSeat.isPresent()) return;
                Seat seat = optSeat.get();

                if (packet.isUnmount()) {
                    PlayerSeatEvent playerSeatEvent = new PlayerSeatEvent(PlayerSeatEvent.SeatAction.LEAVE, seat, player, 0);
                    playerSeatEvent.setCancelled(seat.isLocked());

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getPluginManager().callEvent(playerSeatEvent);
                        if (playerSeatEvent.isCancelled()) return;
                        seat.dismount();
                    });
                }


                if (packet.getSideways() != 0) {
                    Teacup teacup = teacupsAPI.getTeacup(player).get();
                    Cart cart = teacupsAPI.getCart(player).get();
                    PlayerSeatEvent playerSeatEvent = new PlayerSeatEvent(PlayerSeatEvent.SeatAction.STEER, seat, player, packet.getSideways());
                    playerSeatEvent.setCancelled(!teacup.acceptsPlayerInput());

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getPluginManager().callEvent(playerSeatEvent);
                        if (playerSeatEvent.isCancelled()) return;

                        if (playerSeatEvent.getSteerAmount() > 0) {
                            cart.addPlayerInput(teacup.getPlayerInputSensitivity());
                        } else {
                            cart.addPlayerInput(teacup.getPlayerInputSensitivity() * -1);
                        }
                    });
                }


            }
        });

    }

    public void removePacketListeners() {
        manager.removePacketListeners(plugin);
    }

}
