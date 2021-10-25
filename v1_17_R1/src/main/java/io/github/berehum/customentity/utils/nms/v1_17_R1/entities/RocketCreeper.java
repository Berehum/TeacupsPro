package io.github.berehum.customentity.utils.nms.v1_17_R1.entities;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

public class RocketCreeper extends EntityCreeper implements CustomEntity {

    public RocketCreeper(Location loc) {
        this(((CraftWorld) loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public RocketCreeper(EntityTypes<? extends EntityCreeper> entityTypes, World world) {
        this(world);
    }

    public RocketCreeper(World world) {
        //EntityTypes.CREEPER
        super(EntityTypes.o, world);

        this.setHealth(10);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&c&lRocket Creeper")));
    }

    @Override
    public void explode() {
        //this.level.isClientSide
        if (this.t.y) return;
        //Explosion.Effect.NONE
        Explosion.Effect explosion_effect = Explosion.Effect.a;
        float f = isPowered() ? 2.0F : 1.0F;
        //bV = explosionradius
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), this.bV * f, false);
        this.t.getCraftServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            //swell
            this.bT = 0;
            return;
        }
        this.t.createExplosion(this, locX(), locY(), locZ(), event.getRadius(), event.getFire(), explosion_effect);
        getEntity().setVelocity(new Vector(0, this.bV * 2, 0));
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
