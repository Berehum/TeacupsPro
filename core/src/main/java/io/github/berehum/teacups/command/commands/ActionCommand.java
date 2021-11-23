package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ActionCommand extends TeacupCommand {

    public ActionCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("action")
                        .argument(EnumArgument.of(ActionType.class, "action"))
                        .argument(TeacupArgument.of("teacup"))
                        .argument(BooleanArgument.optional("override"))
                        .handler(this::setRpm)
        );
    }

    private void setRpm(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get("teacup");
        final ActionType actionType = context.get("action");
        final Optional<Boolean> override = context.getOptional("override");

        boolean succeeded = false;

        if (actionType == ActionType.START) {
            succeeded = teacup.start(override.orElse(Boolean.FALSE));
        } else if (actionType == ActionType.STOP) {
            succeeded = teacup.stop(override.orElse(Boolean.FALSE));
        }

        CommandSender sender = context.getSender();
        if (!succeeded) {
            sender.sendMessage(ChatColor.RED + "Sorry, but the " + actionType.name() + " action couldn't be executed on " + teacup.getId());
            return;
        }
        sender.sendMessage(ChatColor.GREEN + actionType.name() + " action was successfully executed on " + teacup.getId());
    }

    private enum ActionType {
        START, STOP;
    }
}
