package io.github.berehum.customentity.utils.nms.v1_16_R2.entities;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.IWolfAlpha;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.List;

public class WolfAlpha extends EntityWolf implements IWolfAlpha {

    public WolfAlpha(Location loc) {
        this(((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public WolfAlpha(EntityTypes<? extends EntityWolf> entityTypes, World world) {
        this(world);
    }

    public WolfAlpha(World world) {
        super(EntityTypes.WOLF, world);

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
    public Entity getEntity() {
        return super.getBukkitEntity();
    }

    @Override
    public CustomEntityType getType() {
        return CustomEntityType.ALPHA_WOLF;
    }

    @Override
    public List<CustomEntity> createMembers() {
        Location loc = getEntity().getLocation();
        List<CustomEntity> pack = Arrays.asList(new WolfMember(loc, "&2Beta Wolf"), new WolfMember(loc, "&3Delta Wolf"),
                new WolfMember(loc, "&6Bere Wolf"), new WolfMember(loc, "&9Warrior Wolf"));
        return pack;
    }
}
