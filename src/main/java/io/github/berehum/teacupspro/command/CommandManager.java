package io.github.berehum.teacupspro.command;

import cloud.commandframework.Command;
import cloud.commandframework.brigadier.CloudBrigadierManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.command.commands.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.UnaryOperator;

public final class CommandManager extends PaperCommandManager<CommandSender> {

    private final BukkitAudiences bukkitAudiences;

    public CommandManager(final @NonNull TeacupsMain plugin) throws Exception {
        super(plugin, CommandExecutionCoordinator.simpleCoordinator(), UnaryOperator.identity(), UnaryOperator.identity());

        this.bukkitAudiences = BukkitAudiences.create(plugin);

        if (this.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            this.registerBrigadier();
            final CloudBrigadierManager<?, ?> brigManager = this.brigadierManager();
            if (brigManager != null) {
                brigManager.setNativeNumberSuggestions(false);
            }
        }

        new TeacupCaptionRegistry<>();

        registerExceptions();

        if (this.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }

        ImmutableList.of(
                new HelpCommand(plugin, this),
                new SpawnCommand(plugin, this),
                new ReloadCommand(plugin, this),
                new LockCommand(plugin, this),
                new PlayerInputCommand(plugin, this),
                new KickCommand(plugin, this),
                new ActionCommand(plugin, this),
                new ExecuteCommand(plugin, this),
                new SetRpmCommand(plugin, this)
        ).forEach(TeacupCommand::register);

    }

    public void registerExceptions() {
        new MinecraftExceptionHandler<CommandSender>()
                .withArgumentParsingHandler()
                .withInvalidSenderHandler()
                .withInvalidSyntaxHandler()
                .withNoPermissionHandler()
                .withCommandExecutionHandler()
                .apply(this, this.bukkitAudiences::sender);


        captionRegistry(new TeacupCaptionRegistry<>());
    }

    public void registerSubcommand(UnaryOperator<Command.Builder<CommandSender>> builderModifier) {
        this.command(builderModifier.apply(this.rootBuilder()));
    }

    private Command.@NonNull Builder<CommandSender> rootBuilder() {
        return this.commandBuilder("teacup", "teacups")
                .meta(CommandMeta.DESCRIPTION, "Teacups command. '/teacup help'");
    }

    public BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }

}
