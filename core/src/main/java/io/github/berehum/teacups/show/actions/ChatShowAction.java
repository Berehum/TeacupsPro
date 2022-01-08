package io.github.berehum.teacups.show.actions;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.show.actions.type.ShowActionType;
import io.github.berehum.teacups.show.reader.ShowFileReader;
import io.github.berehum.teacups.utils.config.ConfigProblem;
import io.github.berehum.teacups.utils.config.ConfigProblemDescriptions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatShowAction implements IShowAction {

    private static final ShowActionType<?> type = TeacupsMain.getInstance().getShowActionTypes().get("chat");

    private boolean loaded = false;

    private String message;

    @Override
    public boolean load(String filename, int line, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        message = builder.substring(0, builder.length()-1);

        if (message.isEmpty()) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription("Message is empty"), String.valueOf(line)));
            return loaded;
        }

        loaded = true;
        return loaded;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;
        TeacupsMain plugin = TeacupsMain.getInstance();
        for (Player player : teacup.getPlayers()) {
            player.sendMessage(plugin.format(player, message));
        }
    }

    @Override
    public @NotNull ShowActionType<?> getType() {
        return type;
    }
}
