package io.github.berehum.teacups.utils;

import org.bukkit.Bukkit;

import java.util.List;

public enum Version {
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1;

    public static final Version Current;

    static {
        String[] split = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        String version = split[split.length - 1];
        Current = Version.valueOf(version);
    }

    public static boolean onlyAllow(List<Version> versions) {
        return versions.contains(Current);
    }

    public boolean isLower(Version version) {
        return (ordinal() < version.ordinal());
    }

    public boolean isHigher(Version version) {
        return (ordinal() > version.ordinal());
    }

    public boolean isCurrent() {
        return (this == Current);
    }


}
