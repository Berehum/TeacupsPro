package io.github.berehum.teacups.utils.nms;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface INMSHandler {
    void teleportEntity(Entity entity, Location location);

    void moveEntity(Entity entity, Vector vector);
}
