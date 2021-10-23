package io.github.berehum.customentity.utils.nms;

import org.bukkit.Location;

public interface INMSUtils {
    IEntityRegistry getEntityRegistry();
    CustomEntity createWolfAlpha(Location location);
    CustomEntity createWolfMember(Location location, String name);
    void spawnCustomEntity(CustomEntity customEntity);
}
