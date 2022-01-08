package io.github.berehum.teacups.command.commands;

import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.TeacupManager;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.ShowArgument;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import io.github.berehum.teacups.show.Show;
import io.github.berehum.teacups.show.ShowManager;
import io.github.berehum.teacups.show.reader.ShowFileReader;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.Optional;

public class ReloadCommand extends TeacupCommand {

    public ReloadCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("reload")
                        .permission("teacups.command.reload")
                        .literal("teacups", "teacup")
                        .argument(TeacupArgument.optional("teacup"))
                        .handler(this::reloadTeacup)
        );
        this.commandManager.registerSubcommand(builder ->
                builder.literal("reload")
                        .permission("teacups.command.reload")
                        .literal("shows", "show")
                        .argument(ShowArgument.optional("show"))
                        .handler(this::reloadShow)
        );
    }

    private void reloadTeacup(final @NonNull CommandContext<CommandSender> context) {
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

    private void reloadShow(final @NonNull CommandContext<CommandSender> context) {
        ShowManager showManager = plugin.getShowManager();

        CommandSender sender = context.getSender();
        Optional<Show> optionalShow = context.getOptional("show");

        if (optionalShow.isPresent()) {
            Show show = optionalShow.get();
            File file = show.getFile();
            String fileName = file.getName();
            showManager.loadShow(file);
            sender.sendMessage(ChatColor.GREEN + file.getName() + " has been reloaded.");
            ShowFileReader.showProblems(sender, fileName, ShowFileReader.getConfigProblems().get(fileName));
            return;
        }

        showManager.shutdown();
        showManager.init(false);
        sender.sendMessage(ChatColor.GREEN + "All shows have been reloaded.");
        ShowFileReader.showProblems(sender, ShowFileReader.getConfigProblems());
    }
}
