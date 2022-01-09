package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.ShowArgument;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import io.github.berehum.teacups.show.Show;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ActionCommand extends TeacupCommand {

    private static final String OVERRIDE = "override";

    public ActionCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("action")
                        .permission("teacups.command.action")
                        .literal("start")
                        .argument(TeacupArgument.of(Teacup.NAME))
                        .argument(BooleanArgument.optional(OVERRIDE))
                        .argument(ShowArgument.optional("show"))
                        .handler(this::start)
        );
        this.commandManager.registerSubcommand(builder ->
                builder.literal("action")
                        .permission("teacups.command.action")
                        .literal("stop")
                        .argument(TeacupArgument.of(Teacup.NAME))
                        .argument(BooleanArgument.optional(OVERRIDE))
                        .handler(this::stop)
        );
    }

    private void start(final @NonNull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        final Teacup teacup = context.get(Teacup.NAME);
        final Optional<Boolean> override = context.getOptional(OVERRIDE);
        final Optional<Show> show = context.getOptional("show");

        if (!show.isPresent()) {
            if (!teacup.start(plugin, override.orElse(Boolean.FALSE))) {
                sender.sendMessage(ChatColor.RED + String.format("Sorry, but couldn't start teacup '%s'.", teacup.getId()));
                return;
            }
        } else {
            if (!teacup.start(plugin, show.get(), override.orElse(Boolean.FALSE))) {
                sender.sendMessage(ChatColor.RED + String.format("Sorry, but couldn't start teacup '%s'.", teacup.getId()));
                return;
            }
        }
        sender.sendMessage(ChatColor.GREEN + String.format("Successfully started teacup '%s'.", teacup.getId()));
    }

    private void stop(final @NonNull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        final Teacup teacup = context.get(Teacup.NAME);
        final Optional<Boolean> override = context.getOptional(OVERRIDE);

        if (!teacup.stop(override.orElse(Boolean.FALSE))) {
            sender.sendMessage(ChatColor.RED + String.format("Sorry, but couldn't stop teacup '%s'.", teacup.getId()));
            return;
        }
        sender.sendMessage(ChatColor.GREEN + String.format("Successfully stopped teacup '%s'.", teacup.getId()));
    }

}
