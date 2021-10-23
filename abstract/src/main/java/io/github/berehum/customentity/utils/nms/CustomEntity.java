package io.github.berehum.customentity.utils.nms;

import org.bukkit.entity.Entity;

public interface CustomEntity {
    NMSUtils getNmsUtils();
    default void spawn() {
        getNmsUtils().spawnCustomEntity(this);
    };
    Entity getEntity();
}
