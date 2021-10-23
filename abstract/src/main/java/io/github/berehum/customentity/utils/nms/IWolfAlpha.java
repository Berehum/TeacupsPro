package io.github.berehum.customentity.utils.nms;

import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

public interface IWolfAlpha extends CustomEntity {
    default void spawnMembers() {
        Location loc = getEntity().getLocation();
        INMSUtils nmsUtils = getNmsUtils();
        List<CustomEntity> pack = Arrays.asList(nmsUtils.createWolfMember(loc, "&2Beta Wolf"), nmsUtils.createWolfMember(loc, "&3Delta Wolf"),
                nmsUtils.createWolfMember(loc, "&6Bere Wolf"), nmsUtils.createWolfMember(loc, "&9Warrior Wolf"));
        pack.forEach(CustomEntity::spawn);
    };

    @Override
    default void spawn() {
        getNmsUtils().spawnCustomEntity(this);
        spawnMembers();
    }
}
