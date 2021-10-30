package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.Teacup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class LockCommand extends TeacupCommand {

    public LockCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("lock")
                        .argument(TeacupArgument.of("teacup"))
                        .argument(BooleanArgument.optional("set locked"))
                        .handler(this::setRpm)
        );
    }

    private void setRpm(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get("teacup");
        final Optional<Boolean> locked = context.getOptional("set locked");

        if (locked.isPresent()) {
            teacup.setLocked(locked.get());
        } else {
            teacup.setLocked(!teacup.isLocked());
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the lock of " + teacup.getId() + " to " + teacup.isLocked());
    }
}
