package io.github.berehum.teacups.show.reader.lines;

import io.github.berehum.teacups.show.actions.type.ShowActionType;

public class StateShowFileLine extends IShowFileLine {

    private final State state;

    public StateShowFileLine(State state, ShowActionType<?> type, String filename, int line, String[] args) {
        super(type, filename, line, args);
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public enum State {
        START, STOP;

        private final String[] aliases;

        State(String... aliases) {
            this.aliases = aliases;
        }

        public static State get(String string) {
            for (State state : values()) {
                if (state.name().equalsIgnoreCase(string)) return state;
                for (String alias : state.getAliases()) {
                    if (alias.equalsIgnoreCase(string)) return state;
                }
            }
            return null;
        }

        public String[] getAliases() {
            return aliases;
        }
    }

}
