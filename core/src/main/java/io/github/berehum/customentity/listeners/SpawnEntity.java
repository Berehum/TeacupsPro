package io.github.berehum.customentity.listeners;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import io.github.berehum.customentity.utils.nms.IWolfAlpha;
import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnEntity implements Listener {

    private final INMSUtils nmsUtils;

    public SpawnEntity(INMSUtils nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Animals) || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM)
            return;
        if (event.getLocation().getBlock().isLiquid()) return;
        if ((int) (Math.random() * 10) == 1) {
            event.setCancelled(true);
            IWolfAlpha wolfAlpha = (IWolfAlpha) nmsUtils.createCustomEntity(CustomEntity.CustomEntityType.ALPHA_WOLF, event.getLocation());
            nmsUtils.spawnCustomEntity(wolfAlpha);
            wolfAlpha.createMembers().forEach(nmsUtils::spawnCustomEntity);
            Bukkit.broadcastMessage("pack spawned at " + event.getLocation());
        }
    }

}
