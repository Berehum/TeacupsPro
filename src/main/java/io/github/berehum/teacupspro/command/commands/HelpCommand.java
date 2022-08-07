package io.github.berehum.teacupspro.command.commands;

import cloud.commandframework.CommandHelpHandler;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.command.CommandManager;
import io.github.berehum.teacupspro.command.TeacupCommand;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Collectors;

public class HelpCommand extends TeacupCommand {

    private final MinecraftHelp<CommandSender> minecraftHelp;

    public HelpCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
        this.minecraftHelp = new MinecraftHelp<>(
                "/teacup help",
                commandManager.getBukkitAudiences()::sender,
                commandManager
        );
        this.minecraftHelp.setHelpColors(MinecraftHelp.HelpColors.of(
                TextColor.color(0x5B00FF),
                NamedTextColor.WHITE,
                TextColor.color(0xC028FF),
                NamedTextColor.GRAY,
                NamedTextColor.DARK_GRAY
        ));
        this.minecraftHelp.setMessage(MinecraftHelp.MESSAGE_HELP_TITLE, "Teacups Help");
    }

    @Override
    public void register() {
        //used to suggest commands as arguments
        final CommandHelpHandler<CommandSender> commandHelpHandler = this.commandManager.createCommandHelpHandler();
        final CommandArgument<CommandSender, String> helpQueryArgument = StringArgument.<CommandSender>newBuilder("query")
                .greedy()
                .asOptional()
                .withSuggestionsProvider((context, input) -> {
                    final CommandHelpHandler.IndexHelpTopic<CommandSender> indexHelpTopic = (CommandHelpHandler.IndexHelpTopic<CommandSender>) commandHelpHandler.queryHelp(context.getSender(), "");
                    return indexHelpTopic.getEntries()
                            .stream()
                            .map(CommandHelpHandler.VerboseHelpEntry::getSyntaxString)
                            .collect(Collectors.toList());
                })
                .build();

        this.commandManager.registerSubcommand(builder ->
                builder.literal("help")
                        .argument(helpQueryArgument)
                        .permission("teacups.command.help")
                        .handler(this::executeHelp));
    }

    private void executeHelp(final @NonNull CommandContext<CommandSender> context) {
        this.minecraftHelp.queryCommands(
                context.<String>getOptional("query").orElse(""),
                context.getSender()
        );
    }
}
