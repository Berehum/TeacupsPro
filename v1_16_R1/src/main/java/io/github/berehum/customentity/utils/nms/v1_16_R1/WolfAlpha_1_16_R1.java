package io.github.berehum.customentity.utils.nms.v1_16_R1;

import io.github.berehum.customentity.utils.nms.NMSUtils;
import io.github.berehum.customentity.utils.nms.WolfAlpha;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfAlpha_1_16_R1 extends EntityWolf implements WolfAlpha {

    private final NMSUtils nmsUtils;

    public WolfAlpha_1_16_R1(NMSUtils nmsUtils, Location loc) {
        super(EntityTypes.WOLF, ((CraftWorld)loc.getWorld()).getHandle());
        this.nmsUtils = nmsUtils;

        this.setPosition(loc.getX(), loc.getY(), loc.getZ());

        this.setHealth(500);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&c&lAlpha Wolf")));
    }

    public void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, WolfAlpha_1_16_R1.class, false));
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
