package io.github.berehum.customentity.utils.nms.v1_16_R3;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import net.minecraft.server.v1_16_R3.*;

import java.lang.reflect.Method;
import java.util.Map;

public class EntityRegistry implements IEntityRegistry {
    public static EntityTypes ALPHA_WOLF;

    private final INMSUtils nmsUtils;

    public EntityRegistry(INMSUtils nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    public void registerEntities() {
        ALPHA_WOLF =  injectNewEntity("alpha_wolf", "wolf", EntityTypes.Builder.<Entity>a(WolfAlpha::new, EnumCreatureType.CREATURE));
    }

    private <T extends Entity> EntityTypes injectNewEntity(String name, String extend_from, EntityTypes.b<T> entitytypes_b, EnumCreatureType type) {
        return injectNewEntity(name, extend_from, EntityTypes.Builder.<Entity>a(entitytypes_b, type));
    }

    private EntityTypes injectNewEntity(String name, String extend_from, EntityTypes.Builder<Entity> entitytypes_builder) {
        // get the server's datatypes (also referred to as "data fixers")
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY_TREE).types();
        // inject the new custom entity (this registers the
        // name/id with the server so you can use it in things
        // like the vanilla /summon command)
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        // create and return an EntityTypes for the custom entity
        // store this somewhere so you can reference it later (like for spawning)

        //using reflection because method is private
        try {
            Method a = EntityTypes.class.getDeclaredMethod("a", String.class, EntityTypes.Builder.class);
            a.setAccessible(true);
            return (EntityTypes) a.invoke(null, name, entitytypes_builder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
