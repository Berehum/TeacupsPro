package io.github.berehum.customentity.utils.nms.v1_16_R2.entities;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;

public class RocketCreeper extends EntityCreeper implements CustomEntity {

    public RocketCreeper(Location loc) {
        this(((CraftWorld) loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public RocketCreeper(EntityTypes<? extends EntityCreeper> entityTypes, World world) {
        this(world);
    }

    public RocketCreeper(World world) {
        super(EntityTypes.CREEPER, world);

        this.setHealth(10);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&c&lRocket Creeper")));
    }

    @Override
    public void explode() {
        //this.level.isClientSide
        if (this.world.isClientSide) return;
        //Explosion.Effect.NONE
        Explosion.Effect explosion_effect = Explosion.Effect.NONE;
        float f = isPowered() ? 2.0F : 1.0F;
        //bV = explosionradius
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), this.explosionRadius * f, false);
        this.world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            //swell
            try {
                Field field = this.getClass().getDeclaredField("fuseTicks");
                field.setAccessible(true);
                field.set(this, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        this.world.createExplosion(this, locX(), locY(), locZ(), event.getRadius(), event.getFire(), explosion_effect);
        getEntity().setVelocity(new Vector(0, f * 2, 0));
    }

    @Override
    public Entity getEntity() {
        return super.getBukkitEntity();
    }

    @Override
    public CustomEntityType getType() {
        return CustomEntityType.ROCKET_CREEPER;
    }
}
