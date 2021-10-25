package io.github.berehum.customentity;

import io.github.berehum.customentity.commands.SpawnAlphaCommand;
import io.github.berehum.customentity.listeners.SpawnEntity;
import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEntityMain extends JavaPlugin {

    private INMSUtils nmsUtils;

    @Override
    public void onLoad() {
        if (!setupNMS(Version.Current)) {
            getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        registerEntities(nmsUtils.getEntityRegistry());
    }

    public void registerEntities(IEntityRegistry entityRegistry) {
        entityRegistry.injectCustomEntity(CustomEntity.CustomEntityType.ALPHA_WOLF, Version.Current.name());
        entityRegistry.injectCustomEntity(CustomEntity.CustomEntityType.WOLF_MEMBER, Version.Current.name());
        entityRegistry.injectCustomEntity(CustomEntity.CustomEntityType.ROCKET_CREEPER, Version.Current.name());
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SpawnEntity(nmsUtils), this);
        getCommand("spawnalpha").setExecutor(new SpawnAlphaCommand(nmsUtils));

    }

    public boolean setupNMS(Version version) {
        switch (version) {
            case v1_16_R1:
                nmsUtils = new io.github.berehum.customentity.utils.nms.v1_16_R1.NMSUtils();
                break;
            case v1_16_R2:
                nmsUtils = new io.github.berehum.customentity.utils.nms.v1_16_R2.NMSUtils();
                break;
            case v1_16_R3:
                nmsUtils = new io.github.berehum.customentity.utils.nms.v1_16_R3.NMSUtils();
                break;
            case v1_17_R1:
                nmsUtils = new io.github.berehum.customentity.utils.nms.v1_17_R1.NMSUtils();
                break;
        }

        return nmsUtils != null;
    }

    public INMSUtils getNmsUtils() {
        return nmsUtils;
    }

}
