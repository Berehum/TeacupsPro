package io.github.berehum.teacups.show.actions;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.show.actions.type.ShowActionType;
import io.github.berehum.teacups.show.reader.ShowFileReader;
import io.github.berehum.teacups.utils.config.ConfigProblem;
import io.github.berehum.teacups.utils.config.ConfigProblemDescriptions;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;


public class ConsoleShowAction implements IShowAction {

    private static final ShowActionType<?> type = TeacupsMain.getInstance().getShowActionTypes().get("console");
    private boolean loaded = false;

    private ConsoleCommandSender sender;
    private String command;

    @Override
    public boolean load(String filename, int line, String[] args) {
        sender = Bukkit.getConsoleSender();

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        command = builder.substring(0, builder.length()-1);

        if (command.isEmpty()) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription("No command given."), String.valueOf(line)));
            return loaded;
        }

        loaded = true;
        return true;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;
        Bukkit.dispatchCommand(sender, command);
    }

    @Override
    public @NotNull ShowActionType<?> getType() {
        return type;
    }
}

