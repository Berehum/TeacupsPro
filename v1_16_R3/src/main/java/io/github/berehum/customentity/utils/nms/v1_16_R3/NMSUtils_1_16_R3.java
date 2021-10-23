package io.github.berehum.customentity.utils.nms.v1_16_R3;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import io.github.berehum.customentity.utils.nms.NMSUtils;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSUtils_1_16_R3 implements NMSUtils {

    private final EntityRegistry entityRegistry = new EntityRegistry();

    @Override
    public IEntityRegistry getEntityRegistry() {
        return entityRegistry;
    }

    @Override
    public CustomEntity createWolfAlpha(Location location) {
        return new WolfAlpha_1_16_R3(this, location);
    }

    @Override
    public CustomEntity createWolfMember(Location location, String name) {
        return new WolfMember_1_16_R3(this, location, name);
    }

    @Override
    public void spawnCustomEntity(CustomEntity customEntity) {
        WorldServer world = ((CraftWorld) customEntity.getEntity().getLocation().getWorld()).getHandle();
        world.addEntity((Entity) customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
}
