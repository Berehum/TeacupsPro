package io.github.berehum.teacupspro.show.actions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.actions.type.ShowActionType;
import io.github.berehum.teacupspro.show.reader.ShowFileReader;
import io.github.berehum.teacupspro.utils.config.ConfigProblem;
import io.github.berehum.teacupspro.utils.config.ConfigProblemDescriptions;
import org.jetbrains.annotations.NotNull;

public class PlayerInputShowAction implements IShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("playerinput");

    private boolean loaded = false;

    private boolean playerinput;

    @Override
    public boolean load(String filename, int line, String[] args) {
        if (args.length < 1) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.MISSING_ARGUMENT.getDescription("<true/false>"), String.valueOf(line)));
            return loaded;
        }

        try {
            playerinput = Boolean.parseBoolean(args[0]);
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
        teacup.setAcceptPlayerInput(playerinput);
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}

