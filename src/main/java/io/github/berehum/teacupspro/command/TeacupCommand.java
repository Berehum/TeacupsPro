package io.github.berehum.teacupspro.command;

import io.github.berehum.teacupspro.TeacupsMain;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class TeacupCommand {
    protected final TeacupsMain plugin;
    protected final CommandManager commandManager;

    protected TeacupCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    public abstract void register();
}