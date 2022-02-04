package io.github.berehum.teacupspro.show.reader.lines;

import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionType;

public class TickShowFileLine extends IShowFileLine {

    private final int tick;

    public TickShowFileLine(int tick, ShowActionType type, String filename, int line, String[] args) {
        super(type, filename, line, args);
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }

}
