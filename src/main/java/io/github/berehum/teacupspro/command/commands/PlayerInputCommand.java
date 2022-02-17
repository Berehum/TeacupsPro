package io.github.berehum.teacupspro.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.command.CommandManager;
import io.github.berehum.teacupspro.command.TeacupCommand;
import io.github.berehum.teacupspro.command.arguments.TeacupArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class PlayerInputCommand extends TeacupCommand {

    public PlayerInputCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("playerinput", "setplayerinput")
                        .permission("teacups.command.playerinput")
                        .argument(TeacupArgument.of("teacup"))
                        .argument(BooleanArgument.optional("set player input allowed"))
                        .handler(this::setRpm)
        );
    }

    private void setRpm(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get("teacup");
        final Optional<Boolean> playerInput = context.getOptional("set player input allowed");

        if (playerInput.isPresent()) {
            teacup.setAcceptPlayerInput(playerInput.get());
        } else {
            teacup.setAcceptPlayerInput(!teacup.acceptsPlayerInput());
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set player input of " + teacup.getId() + " to " + teacup.acceptsPlayerInput());
    }
}

