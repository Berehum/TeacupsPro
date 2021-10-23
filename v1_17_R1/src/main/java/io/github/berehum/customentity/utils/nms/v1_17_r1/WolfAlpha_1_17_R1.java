package io.github.berehum.customentity.utils.nms.v1_17_r1;

import io.github.berehum.customentity.utils.nms.NMSUtils;
import io.github.berehum.customentity.utils.nms.WolfAlpha;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRestrictSun;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Entity;

public class WolfAlpha_1_17_R1 extends EntityWolf implements WolfAlpha {

    private final NMSUtils nmsUtils;

    public WolfAlpha_1_17_R1(NMSUtils nmsUtils, Location loc) {
        //EntityTypes.WOLF
        super(EntityTypes.bc, ((CraftWorld)loc.getWorld()).getHandle());
        this.nmsUtils = nmsUtils;
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());

        this.setHealth(500);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', "&c&lAlpha Wolf")));
    }

    @Override
    public void initPathfinder() {
        super.initPathfinder();
        this.bP.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.bP.a(1, new PathfinderGoalNearestAttackableTarget<>(this, WolfAlpha_1_17_R1.class, false));
        this.bP.a(1, new PathfinderGoalRestrictSun(this));
        this.bP.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
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
