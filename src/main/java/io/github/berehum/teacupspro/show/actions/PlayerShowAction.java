package io.github.berehum.teacupspro.show.actions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.reader.ShowFileReader;
import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionType;
import io.github.berehum.teacupspro.utils.config.ConfigProblem;
import io.github.berehum.teacupspro.utils.config.ConfigProblemDescriptions;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class PlayerShowAction implements IShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("player");

    private boolean loaded = false;

    private String command;

    @Override
    public boolean load(String filename, int line, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        command = builder.substring(0, builder.length() - 1);

        if (command.isEmpty()) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription("No command given."), String.valueOf(line)));
            return loaded;
        }

        loaded = true;
        return loaded;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;
        teacup.getPlayers().forEach(player -> Bukkit.dispatchCommand(player, command));
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
