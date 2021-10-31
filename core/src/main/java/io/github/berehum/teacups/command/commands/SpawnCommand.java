package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.location.LocationArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.utils.CustomConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public class SpawnCommand extends TeacupCommand {

    public SpawnCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("spawn")
                        .argument(StringArgument.of("name"))
                        .argument(LocationArgument.of("location"))
                        .handler(this::spawnTeacup)
        );
    }

    private void spawnTeacup(final @NonNull CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        String name = context.get("name");
        Location location = context.get("location");

        if (plugin.getTeacupManager().getTeacup(name).isPresent()) {
            sender.sendMessage(ChatColor.RED + "A teacups attraction with this name already exists.");
            return;
        }

        CustomConfig config = new CustomConfig(plugin, new File(plugin.getDataFolder().getAbsolutePath() + "/teacups", name + ".yml"));
        config.saveDefaultConfig("teacuptemplate.yml");
        ConfigurationSection section = config.getConfig().getConfigurationSection("settings.location");
        if (section == null) {
            sender.sendMessage(ChatColor.RED + "Something went wrong whilst creating a new config file.");
            return;
        }
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        config.saveConfig();

        plugin.getTeacupManager().loadTeacup(config);

        sender.sendMessage(ChatColor.GREEN + "A new teacups attraction has been created, edit the configuration file to your needing.");
    }
}
