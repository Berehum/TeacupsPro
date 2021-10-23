package io.github.berehum.customentity.utils.nms.v1_16_R2;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import io.github.berehum.customentity.utils.nms.IEntityRegistry;
import net.minecraft.server.v1_16_R2.*;

import java.util.Map;
import java.util.function.Function;

public class EntityRegistry implements IEntityRegistry {
    public static EntityTypes ALPHA_WOLF;

    public void registerEntities() {
        ALPHA_WOLF = injectNewEntity("alpha_wolf", "wolf", WolfAlpha_1_16_R2.class, new WolfAlpha_1_16_R2(new NMSUtils_1_16_R2(), ));
    }

    private EntityTypes injectNewEntity(String name, String extend_from, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        // get the server's datatypes (also referred to as "data fixers")
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.a().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY_TREE).types();
        // inject the new custom entity (this registers the
        // name/id with the server so you can use it in things
        // like the vanilla /summon command)
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        // create and return an EntityTypes for the custom entity
        // store this somewhere so you can reference it later (like for spawning)
        return EntityTypes.a(name, EntityTypes.a.a(clazz, function));
    }

}
