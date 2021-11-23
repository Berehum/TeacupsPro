package io.github.berehum.teacups.command.commands;

import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.TeacupManager;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ReloadCommand extends TeacupCommand {

    public ReloadCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("reload")
                        .argument(TeacupArgument.optional("teacup"))
                        .handler(this::spawnTeacup)
        );
    }

    private void spawnTeacup(final @NonNull CommandContext<CommandSender> context) {
        TeacupManager teacupManager = plugin.getTeacupManager();

        CommandSender sender = context.getSender();
        Optional<Teacup> optionalTeacup = context.getOptional("teacup");

        if (optionalTeacup.isPresent()) {
            Teacup teacup = optionalTeacup.get();
            teacupManager.loadTeacup(teacup.getCustomConfig());
            sender.sendMessage(ChatColor.GREEN + teacup.getId() + " has been reloaded.");
            return;
        }

        teacupManager.getTeacups().values().forEach(Teacup::disable);
        teacupManager.getTeacups().clear();
        teacupManager.loadTeacups(plugin.getDataFolder() + "/teacups");
        sender.sendMessage(ChatColor.GREEN + "All teacups have been reloaded.");
    }
}
