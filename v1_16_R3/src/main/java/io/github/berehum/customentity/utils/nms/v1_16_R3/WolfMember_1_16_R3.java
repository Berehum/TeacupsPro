package io.github.berehum.customentity.utils.nms.v1_16_R3;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.NMSUtils;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfMember_1_16_R3 extends EntityWolf implements CustomEntity {

    private final NMSUtils nmsUtils;

    public WolfMember_1_16_R3(NMSUtils nmsUtils, Location loc, String name) {
        super(EntityTypes.WOLF, ((CraftWorld)loc.getWorld()).getHandle());
        this.nmsUtils = nmsUtils;
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());

        this.setHealth(300);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public void initPathfinder() {
        super.initPathfinder();

        this.goalSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.goalSelector.a(1, new PathfinderGoalFollowLeader(this, 1.00));
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
