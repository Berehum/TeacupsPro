package io.github.berehum.teacupspro.show.actions.messageactions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.show.actions.type.ShowActionType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatShowAction extends MessageShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("chat");

    public ChatShowAction() {
        super(CommandSender::sendMessage);
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
