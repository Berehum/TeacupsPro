package io.github.berehum.customentity.utils.nms;

import org.bukkit.Location;

public interface INMSUtils {
    IEntityRegistry getEntityRegistry();

    CustomEntity createCustomEntity(CustomEntity.CustomEntityType type, Location location);

    void spawnCustomEntity(CustomEntity customEntity);

    default CustomEntity spawnCustomEntity(CustomEntity.CustomEntityType type, Location location) {
        CustomEntity customEntity = createCustomEntity(type, location);
        spawnCustomEntity(customEntity);
        return customEntity;
    }
}
