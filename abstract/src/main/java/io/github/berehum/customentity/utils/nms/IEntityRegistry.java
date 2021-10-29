package io.github.berehum.customentity.utils.nms;

public interface IEntityRegistry {
    default void injectCustomEntity(CustomEntity.CustomEntityType type, String versionPackage) {
        try {
            injectCustomEntity(type, getEntityClass(type, versionPackage));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void injectCustomEntity(CustomEntity.CustomEntityType type, Class<?> clazz);

    default Class<?> getEntityClass(CustomEntity.CustomEntityType type, String version) throws ClassNotFoundException {
        return Class.forName("io.github.berehum.customentity.utils.nms." + version + ".entities." + type.getClassName());
    }
}
