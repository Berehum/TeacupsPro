package io.github.berehum.customentity.utils.nms.v1_17_R1.entities;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.v1_17_R1.PathfinderGoalFollowLeader;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfMember extends EntityWolf implements CustomEntity {

    public WolfMember(Location location) {
        this(location, "Wolf Member");
    }

    public WolfMember(Location loc, String name) {
        this(((CraftWorld) loc.getWorld()).getHandle(), name);
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public WolfMember(EntityTypes<? extends EntityWolf> entityTypes, World world) {
        this(world, "Wolf Member");
    }

    public WolfMember(World world, String name) {
        //EntityTypes.WOLF
        super(EntityTypes.bc, world);

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
    public Entity getEntity() {
        return super.getBukkitEntity();
    }

    @Override
    public CustomEntityType getType() {
        return CustomEntityType.WOLF_MEMBER;
    }
}
