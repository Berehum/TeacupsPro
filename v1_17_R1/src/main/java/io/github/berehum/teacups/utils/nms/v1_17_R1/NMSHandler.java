package io.github.berehum.teacups.utils.nms.v1_17_R1;

import io.github.berehum.teacups.utils.nms.INMSHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.util.Vector;

public class NMSHandler implements INMSHandler {
    @Override
    public void teleportEntity(org.bukkit.entity.Entity entity, Location location) {
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        nmsEntity.setPosition(location.getX(), location.getY(), location.getZ());
    }

    public void moveEntity(org.bukkit.entity.Entity entity, Vector vector) {
        Vec3D locationVector = new Vec3D(vector.getX(), vector.getY(), vector.getZ());
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        //Method is MOVE not TELEPORT!!
        nmsEntity.move(EnumMoveType.a, locationVector);
    }
}
