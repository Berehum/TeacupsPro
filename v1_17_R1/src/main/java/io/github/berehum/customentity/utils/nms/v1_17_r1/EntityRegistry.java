package io.github.berehum.customentity.utils.nms.v1_17_r1;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.entity.animal.EntityWolf;

import java.util.Map;

public class EntityRegistry implements IEntityRegistry {
    public static EntityTypes ALPHA_WOLF;

    private final INMSUtils nmsUtils;

    public EntityRegistry(INMSUtils nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    public void registerEntities() {
        ALPHA_WOLF = injectNewEntity("alpha_wolf", "wolf", (entityTypes, world) -> new WolfAlpha((EntityTypes<? extends EntityWolf>) entityTypes, world, nmsUtils), EnumCreatureType.b);
    }

    private EntityTypes injectNewEntity(String name, String extend_from, EntityTypes.b<Entity> entityb, EnumCreatureType type) {
        // get the server's datatypes (also referred to as "data fixers")
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion())).findChoiceType(DataConverterTypes.p).types();
        // inject the new custom entity (this registers the
        // name/id with the server so you can use it in things
        // like the vanilla /summon command)
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        // create and return an EntityTypes for the custom entity
        // store this somewhere so you can reference it later (like for spawning)
        EntityTypes.Builder<Entity> entityBuilder = EntityTypes.Builder.a(entityb, type);
        return EntityTypes.<Entity>a(name, entityBuilder);
    }

}
