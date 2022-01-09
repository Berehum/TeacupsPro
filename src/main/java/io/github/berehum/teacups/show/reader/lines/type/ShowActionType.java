package io.github.berehum.teacups.show.reader.lines.type;

import io.github.berehum.teacups.show.actions.IShowAction;

import java.util.function.Supplier;

public class ShowActionType {
    private final String name;
    private final String[] aliases;
    private final Supplier<IShowAction> constructor;

    public ShowActionType(String name, String[] aliases, Supplier<IShowAction> constructor) {
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

    public Supplier<IShowAction> getConstructor() {
        return constructor;
    }
}
