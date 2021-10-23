package io.github.berehum.customentity.utils.nms;

import org.bukkit.entity.Entity;

public interface CustomEntity {
    INMSUtils getNmsUtils();
    default void spawn() {
        getNmsUtils().spawnCustomEntity(this);
    };
    Entity getEntity();
}
