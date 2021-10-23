package io.github.berehum.customentity.utils.nms.v1_16_R2;

import io.github.berehum.customentity.utils.nms.INMSUtils;
import io.github.berehum.customentity.utils.nms.IWolfAlpha;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfAlpha extends EntityWolf implements IWolfAlpha {

    private final INMSUtils nmsUtils;

    public WolfAlpha(INMSUtils nmsUtils, Location loc) {
        this(nmsUtils, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public WolfAlpha(INMSUtils nmsUtils, World world) {
        this(EntityTypes.WOLF, world, nmsUtils);
    }

    public WolfAlpha(EntityTypes<? extends EntityWolf> entityTypes, World world, INMSUtils nmsUtils) {
        super(entityTypes, world);
        this.nmsUtils = nmsUtils;

        this.setHealth(500);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&c&lAlpha Wolf")));
    }


    public void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, WolfAlpha.class, false));
        this.goalSelector.a(1, new PathfinderGoalRestrictSun(this));
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    }

    @Override
    public INMSUtils getNmsUtils() {
        return nmsUtils;
    }

    @Override
    public Entity getEntity() {
        return super.getBukkitEntity();
    }
}
