package io.github.berehum.customentity.utils.nms.v1_16_R3;

import io.github.berehum.customentity.utils.nms.NMSUtils;
import io.github.berehum.customentity.utils.nms.WolfAlpha;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfAlpha_1_16_R3 extends EntityWolf implements WolfAlpha {

    private final NMSUtils nmsUtils;

    public WolfAlpha_1_16_R3(NMSUtils nmsUtils, Location loc) {
        this(nmsUtils, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public WolfAlpha_1_16_R3(NMSUtils nmsUtils, World world) {
        this(EntityTypes.WOLF, world, nmsUtils);
    }

    public WolfAlpha_1_16_R3(EntityTypes<? extends EntityWolf> entityTypes, World world, NMSUtils nmsUtils) {
        super(entityTypes, world);
        this.nmsUtils = nmsUtils;

        this.setHealth(500);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&c&lAlpha Wolf")));
    }


    public void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<WolfAlpha_1_16_R3>(this, WolfAlpha_1_16_R3.class, false));
        this.goalSelector.a(1, new PathfinderGoalRestrictSun(this));
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
    }

    @Override
    public NMSUtils getNmsUtils() {
        return nmsUtils;
    }

    @Override
    public Entity getEntity() {
        return super.getBukkitEntity();
    }
}
