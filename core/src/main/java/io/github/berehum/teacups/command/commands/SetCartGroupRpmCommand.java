package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.attraction.components.CartGroup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.CartGroupArgument;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class SetCartGroupRpmCommand extends TeacupCommand {

    public SetCartGroupRpmCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("setrpm").literal("cartgroup")
                        .argument(TeacupArgument.of("teacup"))
                        .argument(CartGroupArgument.of("cartgroup"))
                        .argument(IntegerArgument.of("rpm"))
                        .argument(BooleanArgument.optional("add to existing"))
                        .handler(this::setRpm)
        );
    }

    private void setRpm(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get("teacup");
        final CartGroup cartgroup = context.get("cartgroup");
        final int rpm = context.get("rpm");
        final Optional<Boolean> addToExisting = context.getOptional("add to existing");

        if (addToExisting.isPresent() && addToExisting.get()) {
            cartgroup.setRpm(cartgroup.getRpm() + rpm);
        } else {
            cartgroup.setRpm(rpm);
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + cartgroup.getId() + " in " + teacup.getId() + " to " + cartgroup.getRpm());
    }
}
