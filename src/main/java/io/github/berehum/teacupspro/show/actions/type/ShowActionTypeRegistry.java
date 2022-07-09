package io.github.berehum.teacupspro.show.actions.type;

import io.github.berehum.teacupspro.api.events.RegisterShowActionTypesEvent;
import io.github.berehum.teacupspro.exceptions.ClashingActionTypesException;
import io.github.berehum.teacupspro.show.actions.*;
import io.github.berehum.teacupspro.show.actions.messageactions.ActionBarShowAction;
import io.github.berehum.teacupspro.show.actions.messageactions.ChatShowAction;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowActionTypeRegistry {

    private final Logger logger;
    private final Set<ShowActionType> typeSet = new HashSet<>();

    public ShowActionTypeRegistry(Logger logger) {
        this.logger = logger;
    }

    public void registerTypes() {
        //Lol would this even work??? The other plugins depending on this one should load AFTER this one has been loaded.
        //Meaning that they won't be able to listen for the event....

        //just keeping this till version 2.0 to keep stuff from breaking (which is very unlikely if stuff is handled properly)
        Bukkit.getPluginManager().callEvent(new RegisterShowActionTypesEvent(this));

        try {
            register(new ShowActionType("rpm", new String[]{"setrpm"}, RpmShowAction::new));
            register(new ShowActionType("kick", new String[]{"kickall"}, KickShowAction::new));
            register(new ShowActionType("lock", new String[]{"setlock", "locked"}, LockShowAction::new));
            register(new ShowActionType("playerinput", new String[]{"steering", "input", "setplayerinput"},
                    PlayerInputShowAction::new));
            register(new ShowActionType("console", new String[]{"cmd", "command"}, ConsoleShowAction::new));
            register(new ShowActionType("player", new String[]{"playercmd", "playercommand"}, PlayerShowAction::new));
            register(new ShowActionType("actionbar", new String[]{"bar"}, ActionBarShowAction::new));
            register(new ShowActionType("chat", new String[]{"message, msg"}, ChatShowAction::new));
            register(new ShowActionType("stop", new String[]{"end", "finish"}, StopShowAction::new));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error whilst registering show action types.", e);
        }
    }

    public void register(ShowActionType type) throws ClashingActionTypesException {
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

    public Set<ShowActionType> getTypeSet() {
        return Collections.unmodifiableSet(typeSet);
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
