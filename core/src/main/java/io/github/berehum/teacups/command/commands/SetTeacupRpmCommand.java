package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.FloatArgument;
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

public class SetTeacupRpmCommand extends TeacupCommand {

    public SetTeacupRpmCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("setrpm").literal("teacup")
                        .argument(TeacupArgument.of("teacup"))
                        .argument(FloatArgument.of("rpm"))
                        .argument(BooleanArgument.optional("add to existing"))
                        .handler(this::setRpm)
        );
    }

    private void setRpm(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get("teacup");
        final float rpm = context.get("rpm");
        final Optional<Boolean> addToExisting = context.getOptional("add to existing");

        if (addToExisting.isPresent() && addToExisting.get()) {
            teacup.setRpm(teacup.getRpm()+rpm);
        } else {
            teacup.setRpm(rpm);
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + teacup.getId() + " to " + teacup.getRpm());
    }
}
