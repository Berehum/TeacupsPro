package io.github.berehum.customentity.utils.nms.v1_16_R1;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import net.minecraft.server.v1_16_R1.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EntityRegistry implements IEntityRegistry {
    public static Map<CustomEntity.CustomEntityType, EntityTypes> ENTITIY_TYPES = new HashMap<>();

    public void injectCustomEntity(CustomEntity.CustomEntityType type, Class<?> clazz) {
        //Check if it is an instance of the right super classes
        if (!CustomEntity.class.isAssignableFrom(clazz) || !Entity.class.isAssignableFrom(clazz)) {
            return;
        }

        //Determining CreatureType
        EnumCreatureType creatureType = EnumCreatureType.a(type.getCreatureType());
        if (creatureType == null) return;

        //Gets the default constructor that every CustomEntity class MUST have.
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(EntityTypes.class, World.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }

        EntityTypes entityTypes = injectNewEntity(type.getName(), type.getExtendFrom(), EntityTypes.Builder.<Entity>a(((entitytypes, world) -> {
            try {
                return (Entity) constructor.newInstance(entitytypes, world);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }), creatureType));
        ENTITIY_TYPES.put(type, entityTypes);
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
            return null;
        }
    }
}
