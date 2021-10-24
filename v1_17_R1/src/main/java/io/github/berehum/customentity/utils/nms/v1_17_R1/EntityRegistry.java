package io.github.berehum.customentity.utils.nms.v1_17_R1;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import io.github.berehum.customentity.utils.nms.v1_17_R1.entities.RocketCreeper;
import io.github.berehum.customentity.utils.nms.v1_17_R1.entities.WolfAlpha;
import io.github.berehum.customentity.utils.nms.v1_17_R1.entities.WolfMember;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumCreatureType;
import org.bukkit.Location;

import java.lang.reflect.Method;
import java.util.Map;

public class EntityRegistry implements IEntityRegistry {
    public static EntityTypes ALPHA_WOLF;
    public static EntityTypes WOLF_MEMBER;
    public static EntityTypes SENSITIVE_CREEPER;

    public void registerEntities() {
        //EnumCreatureType.CREATURE
        ALPHA_WOLF = injectNewEntity("alpha_wolf", "wolf", EntityTypes.Builder.<Entity>a(WolfAlpha::new, EnumCreatureType.b));
        WOLF_MEMBER = injectNewEntity("wolf_member", "wolf", EntityTypes.Builder.<Entity>a(WolfMember::new, EnumCreatureType.b));
        //EnumCreatureType.MONSTER
        SENSITIVE_CREEPER = injectNewEntity("rocket_creeper", "creeper", EntityTypes.Builder.<Entity>a(RocketCreeper::new, EnumCreatureType.a));
    }

    //@todo make this so i can inject the entities in the core.
    public void injectCustomEntity(CustomEntity.CustomEntityType type) {
    }

    private EntityTypes injectNewEntity(String name, String extend_from, EntityTypes.Builder<Entity> entitytypes_builder) {
        // get the server's datatypes (also referred to as "data fixers")
        // DataConverterTypes.ENTITY_TREE
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion())).findChoiceType(DataConverterTypes.p).types();
        // inject the new custom entity (this registers the
        // name/id with the server so you can use it in things
        // like the vanilla /summon command)
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        // create and return an EntityTypes for the custom entity
        // store this somewhere so you can reference it later (like for spawning)

        // using reflection because method is private
        try {
            Method a = EntityTypes.class.getDeclaredMethod("a", String.class, EntityTypes.Builder.class);
            a.setAccessible(true);
            EntityTypes entityTypes = (EntityTypes) a.invoke(null, name, entitytypes_builder);
            return entityTypes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
