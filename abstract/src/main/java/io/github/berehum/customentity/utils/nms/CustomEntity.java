package io.github.berehum.customentity.utils.nms;

import org.bukkit.entity.Entity;

public interface CustomEntity {
    Entity getEntity();
    CustomEntityType getType();

    enum CustomEntityType {
        ALPHA_WOLF, WOLF_MEMBER, ROCKET_CREEPER;
    }
}
