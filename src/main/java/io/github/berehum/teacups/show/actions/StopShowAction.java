package io.github.berehum.teacups.show.actions;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.show.reader.lines.type.ShowActionType;
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
