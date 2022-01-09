package io.github.berehum.teacups.show.reader.lines;


import io.github.berehum.teacups.show.actions.IShowAction;
import io.github.berehum.teacups.show.reader.lines.type.ShowActionType;

public abstract class IShowFileLine {

    private final int line;
    private final String filename;
    private final ShowActionType type;
    private final String[] args;
    private IShowAction action;

    protected IShowFileLine(ShowActionType type, String filename, int line, String[] args) {
        this.type = type;
        this.line = line;
        this.filename = filename;
        this.args = args;
    }

    public ShowActionType getType() {
        return type;
    }

    public String[] getArgs() {
        return args;
    }

    public IShowAction toAction() {
        if (action != null) return action;
        IShowAction showAction = toNewAction();
        action = showAction;
        return showAction;
    }

    //will not cache nor return a cached action.
    public IShowAction toNewAction() {
        IShowAction showAction = type.getConstructor().get();
        if (showAction == null) return null;
        showAction.load(filename, line, args);
        return showAction;
    }

}
