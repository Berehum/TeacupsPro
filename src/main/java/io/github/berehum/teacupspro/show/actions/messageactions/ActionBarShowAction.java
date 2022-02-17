package io.github.berehum.teacupspro.show.actions.messageactions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.show.actions.type.ShowActionType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class ActionBarShowAction extends MessageShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("actionbar");

    public ActionBarShowAction() {
        super((player, s) -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(s)));
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
