package io.github.berehum.teacups.show.actions.type;

import io.github.berehum.teacups.show.actions.IShowAction;

import java.util.function.Supplier;

public class ShowActionType<T extends IShowAction> {
    private final String name;
    private final String[] aliases;
    private final Supplier<T> constructor;

    public ShowActionType(String name, String[] aliases, Supplier<T> constructor) {
        this.name = name;
        this.aliases = aliases;
        this.constructor = constructor;
    }


    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Supplier<T> getConstructor() {
        return constructor;
    }
}
