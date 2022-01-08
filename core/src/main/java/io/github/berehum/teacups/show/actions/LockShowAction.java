package io.github.berehum.teacups.show.actions;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.show.actions.type.ShowActionType;
import io.github.berehum.teacups.show.reader.ShowFileReader;
import io.github.berehum.teacups.utils.config.ConfigProblem;
import io.github.berehum.teacups.utils.config.ConfigProblemDescriptions;
import org.jetbrains.annotations.NotNull;

public class LockShowAction implements IShowAction {

    private static final ShowActionType<?> type = TeacupsMain.getInstance().getShowActionTypes().get("lock");

    private boolean loaded = false;

    private boolean lock;

    @Override
    public boolean load(String filename, int line, String[] args) {
        if (args.length < 1) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.MISSING_ARGUMENT.getDescription("<true/false>"), String.valueOf(line)));
            return loaded;
        }

        try {
            lock = Boolean.parseBoolean(args[0]);
            loaded = true;
        } catch (Exception e) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(args[0]), String.valueOf(line)));
        }

        return loaded;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;
        teacup.setLocked(lock);
    }

    @Override
    public @NotNull ShowActionType<?> getType() {
        return type;
    }
}
