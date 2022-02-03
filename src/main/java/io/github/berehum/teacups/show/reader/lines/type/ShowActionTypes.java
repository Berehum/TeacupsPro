package io.github.berehum.teacups.show.reader.lines.type;

import io.github.berehum.teacups.events.RegisterShowActionTypesEvent;
import io.github.berehum.teacups.exceptions.ClashingActionTypesException;
import io.github.berehum.teacups.show.actions.*;
import io.github.berehum.teacups.show.actions.messageactions.ActionBarShowAction;
import io.github.berehum.teacups.show.actions.messageactions.ChatShowAction;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public class ShowActionTypes {
    private final Set<ShowActionType> typeSet = new HashSet<>();

    public void registerTypes() {
        Bukkit.getPluginManager().callEvent(new RegisterShowActionTypesEvent(this));
        try {
            registerType(new ShowActionType("rpm", new String[]{"setrpm"}, RpmShowAction::new));
            registerType(new ShowActionType("kick", new String[]{"kickall"}, KickShowAction::new));
            registerType(new ShowActionType("lock", new String[0], LockShowAction::new));
            registerType(new ShowActionType("console", new String[]{"cmd", "command"}, ConsoleShowAction::new));
            registerType(new ShowActionType("player", new String[]{"playercmd", "playercommand"}, PlayerShowAction::new));
            registerType(new ShowActionType("actionbar", new String[]{"bar"}, ActionBarShowAction::new));
            registerType(new ShowActionType("chat", new String[]{"message, msg"}, ChatShowAction::new));
            registerType(new ShowActionType("stop", new String[]{"end", "finish"}, StopShowAction::new));
        } catch (Exception ignored) {
        }
    }

    public void registerType(ShowActionType type) throws ClashingActionTypesException {
        String name = type.getName();
        String[] aliases = type.getAliases();

        if (get(name) != null) {
            throw new ClashingActionTypesException(get(name), type);
        }
        for (String alias : aliases) {
            if (get(alias) != null) throw new ClashingActionTypesException(get(name), type);
        }

        typeSet.add(type);
    }

    public ShowActionType get(String string) {
        for (ShowActionType type : typeSet) {
            if (type.getName().equalsIgnoreCase(string)) return type;
            for (String alias : type.getAliases()) {
                if (alias.equalsIgnoreCase(string)) return type;
            }
        }
        return null;
    }


}
