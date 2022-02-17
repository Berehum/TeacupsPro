package io.github.berehum.teacupspro.show.actions;

import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.actions.type.ShowActionType;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface IShowAction {
    //stop rpm teacup 0
    //stop rpm cartgroup cartgroup1 0
    //stop rpm cart cartgroup1 cart 1 0
    boolean load(String filename, int line, String[] args);

    void execute(Teacup teacup);

    @NonNull ShowActionType getType();
}
