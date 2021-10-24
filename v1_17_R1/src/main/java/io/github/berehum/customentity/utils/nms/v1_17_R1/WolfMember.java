package io.github.berehum.customentity.utils.nms.v1_17_R1;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfMember extends EntityWolf implements CustomEntity {

    private final INMSUtils nmsUtils;

    public WolfMember(INMSUtils nmsUtils, Location loc, String name) {
        super(EntityTypes.bc, ((CraftWorld)loc.getWorld()).getHandle());
        this.nmsUtils = nmsUtils;
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());

        this.setHealth(300);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', name)));
    }

    @Override
    public void initPathfinder() {
        super.initPathfinder();
        //goalSelector
        this.bP.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.bP.a(1, new PathfinderGoalFollowLeader(this, 1.00));
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
