package io.github.berehum.customentity.utils.nms;

import org.bukkit.entity.Entity;

//This class must have the following constructor:
// ClassName(EntityTypes entityTypes, World world) {}
public interface CustomEntity {
    Entity getEntity();

    CustomEntityType getType();

    enum CustomEntityType {
        ALPHA_WOLF("alpha_wolf", "wolf", "creature", "WolfAlpha"),
        WOLF_MEMBER("wolf_member", "wolf", "creature", "WolfMember"),
        ROCKET_CREEPER("rocket_creeper", "creeper", "monster", "RocketCreeper");

        private final String name;
        private final String extend_from;
        private final String creatureType;
        private final String className;

        CustomEntityType(String name, String extend_from, String creatureType, String className) {
            this.name = name;
            this.extend_from = extend_from;
            this.creatureType = creatureType;
            this.className = className;
        }

        public String getName() {
            return name;
        }

        public String getExtendFrom() {
            return extend_from;
        }

        public String getCreatureType() {
            return creatureType;
        }

        public String getClassName() {
            return className;
        }
    }
}
