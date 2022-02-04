package io.github.berehum.teacupspro.show.actions.messageactions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.actions.IShowAction;
import io.github.berehum.teacupspro.show.reader.ShowFileReader;
import io.github.berehum.teacupspro.utils.config.ConfigProblem;
import io.github.berehum.teacupspro.utils.config.ConfigProblemDescriptions;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public abstract class MessageShowAction implements IShowAction {

    private final BiConsumer<Player, String> consumer;

    private boolean loaded = false;
    private String message;


    protected MessageShowAction(BiConsumer<Player, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean load(String filename, int line, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg).append(" ");
        }
        message = builder.substring(0, builder.length() - 1);

        if (message.isEmpty()) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription("Message is empty"), String.valueOf(line)));
            return loaded;
        }

        loaded = true;
        return true;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;
        TeacupsMain plugin = TeacupsMain.getInstance();
        for (Player player : teacup.getPlayers()) {
            consumer.accept(player, plugin.format(player, message));
        }
    }

}

