package io.github.berehum.teacupspro.show.actions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionType;
import org.jetbrains.annotations.NotNull;

public class KickShowAction implements IShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("kick");

    @Override
    public boolean load(String filename, int line, String[] args) {
        return true;
    }

    @Override
    public void execute(Teacup teacup) {
        teacup.kickAll();
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
