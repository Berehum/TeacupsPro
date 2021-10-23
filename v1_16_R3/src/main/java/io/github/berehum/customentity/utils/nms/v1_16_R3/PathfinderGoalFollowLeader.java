package io.github.berehum.customentity.utils.nms.v1_16_R3;

import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import java.util.EnumSet;

//@todo make abstract pathfindergoal and entityinsentient to reduce code.
public class PathfinderGoalFollowLeader extends PathfinderGoal {
    private final EntityInsentient a; // wolf member
    private Entity b; // leader

    private final double f; // following speed

    private double c; // x
    private double d; // y
    private double e; // z


    public PathfinderGoalFollowLeader(EntityInsentient a, double speed) {
        this.a = a;
        this.f = speed;
        this.a(EnumSet.of(Type.MOVE));
    }

    @Override
    public boolean a() {
        // Will start event if this is true
        // runs every tick
        if (this.b == null) {
            Location loc = new Location(this.a.getWorld().getWorld(), this.a.locX(), this.a.locY(), this.a.locZ());
            for (Entity ent : loc.getWorld().getNearbyEntities(loc, 15, 15, 15)) {
                if (ent.getCustomName() != null)
                    if (ent.getCustomName().contains("Alpha Wolf"))
                        this.b = ent;
            }
            if (this.b == null) {
                this.a.killEntity();
                loc.getWorld().spawnParticle(Particle.CLOUD, loc.getX(), loc.getY(), loc.getZ(), 0);
            }
            return false;
        }
        else if (this.b.getLocation().distance(new Location(this.a.getWorld().getWorld(), this.a.locX(), this.a.locY(), this.a.locZ())) < 6) {
            return false;
        }
        else {

            this.c = this.b.getLocation().getX(); // x
            this.d = this.b.getLocation().getY(); // y
            this.e = this.b.getLocation().getZ(); // z
            return true;
            // call upon c()
        }
    }

    public void c() {
        // runner :)                x      y        z    speed
        this.a.getNavigation().a(this.c, this.d, this.e, this.f);
    }

    public boolean b() {
        // run every tick as long as its true (repeats c)
        return !this.a.getNavigation().m();
    }

    public void d() {
        // runs when done (b is false)
        this.b = null;
    }
}
