package io.github.berehum.teacups.attraction.components;

import io.github.berehum.teacups.attraction.components.armorstands.Model;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CartGroup extends Component {

    public static final String NAME = "cartgroup";

    public CartGroup(String id, Location location, double radius, Model model, Map<String, Cart> carts) {
        super(id, location, radius, model, new HashMap<>(carts));
    }

    //immutable
    public Map<String, Cart> getCarts() {
        HashMap<String, Cart> cartHashMap = new HashMap<>();
        for (Map.Entry<String, Component> entry : super.getSubComponents().entrySet()) {
            Component component = entry.getValue();
            if (!(component instanceof Cart)) continue;
            cartHashMap.put(entry.getKey(), (Cart) component);
        }
        return Collections.unmodifiableMap(cartHashMap);
    }

}
