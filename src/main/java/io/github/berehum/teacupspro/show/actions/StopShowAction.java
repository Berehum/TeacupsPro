package io.github.berehum.teacupspro.show.actions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionType;
import org.jetbrains.annotations.NotNull;

public class StopShowAction implements IShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("stop");

    @Override
    public boolean load(String filename, int line, String[] args) {
        return true;
    }

    @Override
    public void execute(Teacup teacup) {
        teacup.stop(false);
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
