package io.github.berehum.teacupspro.attraction.components;

import io.github.berehum.teacupspro.attraction.components.armorstands.Model;
import io.github.berehum.teacupspro.attraction.components.armorstands.Seat;
import io.github.berehum.teacupspro.utils.LocationUtils;
import io.github.berehum.teacupspro.utils.SeatLayout;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Cart extends Component {

    public static final String NAME = "cart";

    private final SeatLayout seatLayout;
    private final List<Seat> seats;

    public Cart(String id, Location location, double radius, Model model, SeatLayout seatLayout) {
        super(id, location, radius, model, Collections.emptyMap());
        this.seatLayout = seatLayout;
        this.seats = seatLayout.getSeats(location);
    }

    @Override
    public void init() {
        Model model = super.getModel();

        for (Player player : Bukkit.getOnlinePlayers()) {

            for (Seat seat : seats) {
                seat.spawn(player);
            }

            if (model == null) continue;
            model.spawn(player);
        }
    }

    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(player -> seats.forEach(seat -> seat.remove(player)));
        Model model = super.getModel();
        if (model == null) return;
        Bukkit.getOnlinePlayers().forEach(model::remove);
    }

    @Override
    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (Seat seat : seats) {
            Optional<Player> player = Optional.ofNullable(seat.getPlayer());
            if (!player.isPresent()) continue;
            players.add(player.get());
        }
        return players;
    }

    @Override
    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public void updateChildLocations() {

        Location location = super.getLocation();
        float rotation = super.getRotation();
        double radius = super.getRadius();
        double circleOffset = super.getCircleOffset();

        Iterator<Seat> iterator = seats.listIterator();
        for (int i = 0; i < seatLayout.size(); i++) {
            if (seatLayout.isEmpty(i)) continue;
            Seat seat = iterator.next();
            seat.teleport(LocationUtils.drawPoint(location, radius, i, seatLayout.size(), circleOffset));
        }

        if (hasModel()) {
            super.getModel().teleport(LocationUtils.setDirection(location, 0, rotation));
        }
    }

}
