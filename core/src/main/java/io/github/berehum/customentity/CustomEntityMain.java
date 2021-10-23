package io.github.berehum.customentity;

import io.github.berehum.customentity.commands.SpawnAlphaCommand;
import io.github.berehum.customentity.listeners.SpawnEntity;
import io.github.berehum.customentity.utils.nms.NMSUtils;
import io.github.berehum.customentity.utils.nms.v1_16_R1.NMSUtils_1_16_R1;
import io.github.berehum.customentity.utils.nms.v1_16_R2.NMSUtils_1_16_R2;
import io.github.berehum.customentity.utils.nms.v1_16_R3.NMSUtils_1_16_R3;
import io.github.berehum.customentity.utils.nms.v1_17_r1.NMSUtils_1_17_R1;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEntityMain extends JavaPlugin {

    private NMSUtils nmsUtils;

    @Override
    public void onLoad() {
        if (!setupNMS(Version.Current)) {
            getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SpawnEntity(nmsUtils), this);
        getCommand("spawnalpha").setExecutor(new SpawnAlphaCommand(nmsUtils));

    }

    public boolean setupNMS(Version version) {
        switch (version) {
            case v1_16_R1:
                nmsUtils = new NMSUtils_1_16_R1();
                break;
            case v1_16_R2:
                nmsUtils = new NMSUtils_1_16_R2();
                break;
            case v1_16_R3:
                nmsUtils = new NMSUtils_1_16_R3();
                break;
            case v1_17_R1:
                nmsUtils = new NMSUtils_1_17_R1();
                break;
        }

        return nmsUtils != null;
    }

    @Override
    public void onDisable() {
    }

    public NMSUtils getNmsUtils() {
        return nmsUtils;
    }

}
