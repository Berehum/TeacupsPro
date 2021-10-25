package io.github.berehum.customentity.utils.nms.v1_16_R1;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import io.github.berehum.customentity.utils.nms.v1_16_R1.entities.RocketCreeper;
import io.github.berehum.customentity.utils.nms.v1_16_R1.entities.WolfAlpha;
import io.github.berehum.customentity.utils.nms.v1_16_R1.entities.WolfMember;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSUtils implements INMSUtils {

    private final EntityRegistry entityRegistry = new EntityRegistry();

    @Override
    public IEntityRegistry getEntityRegistry() {
        return entityRegistry;
    }

    @Override
    public CustomEntity createCustomEntity(CustomEntity.CustomEntityType type, Location location) {
        switch (type) {
            case ALPHA_WOLF:
                return new WolfAlpha(location);
            case WOLF_MEMBER:
                return new WolfMember(location);
            case ROCKET_CREEPER:
                return new RocketCreeper(location);
        }
        return null;
    }

    public void spawnCustomEntity(CustomEntity customEntity) {
        WorldServer world = ((CraftWorld) customEntity.getEntity().getLocation().getWorld()).getHandle();
        world.addEntity((Entity) customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
}
