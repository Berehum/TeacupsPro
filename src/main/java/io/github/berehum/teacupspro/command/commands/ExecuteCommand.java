package io.github.berehum.teacupspro.command.commands;

import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArrayArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.command.CommandManager;
import io.github.berehum.teacupspro.command.TeacupCommand;
import io.github.berehum.teacupspro.command.arguments.TeacupArgument;
import io.github.berehum.teacupspro.dependencies.PlaceholderApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class ExecuteCommand extends TeacupCommand {

    private static final String COMMAND = "command";

    public ExecuteCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal("execute", COMMAND, "cmd")
                        .permission("teacups.command.execute")
                        .argument(EnumArgument.of(ExecuteType.class, "execute type"))
                        .argument(TeacupArgument.of(Teacup.NAME))
                        .argument(StringArrayArgument.of(COMMAND,
                                (commandSenderCommandContext, s) -> Arrays.asList("Command (without /)", "%player% for the player's name", "Example: eco give %player% 20")))
                        .handler(this::execute)
        );
    }

    private void execute(final @NonNull CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();
        final Teacup teacup = context.get(Teacup.NAME);
        final ExecuteType executeType = context.get("execute type");
        final String[] commandArray = context.get(COMMAND);

        StringBuilder builder = new StringBuilder();
        for (String arg : commandArray) {
            builder.append(arg).append(" ");
        }
        final String command = builder.substring(0, builder.length() - 1);

        final CommandSender commandExecutor = Bukkit.getConsoleSender();

        if (executeType == ExecuteType.CONSOLE) {
            Bukkit.dispatchCommand(commandExecutor, command);
            return;
        } else {
            for (Player player : teacup.getPlayers()) {
                if (player == null) continue;
                String personalCommand = (TeacupsMain.getInstance().isPlaceholderApiEnabled())
                        ? PlaceholderApi.setPlaceholders(player, command) : command;
                if (executeType == ExecuteType.CONSOLE_FOR_EVERY_PLAYER) {
                    Bukkit.dispatchCommand(commandExecutor, personalCommand);
                    continue;
                }
                Bukkit.dispatchCommand(player, personalCommand);
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Execute " + executeType.name().toLowerCase() + " command on " + teacup.getId());
    }

    private enum ExecuteType {
        PLAYER, CONSOLE, CONSOLE_FOR_EVERY_PLAYER
    }
}
