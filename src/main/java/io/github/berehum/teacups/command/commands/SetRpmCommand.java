package io.github.berehum.teacups.command.commands;

import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.context.CommandContext;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Cart;
import io.github.berehum.teacups.attraction.components.CartGroup;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.CommandManager;
import io.github.berehum.teacups.command.TeacupCommand;
import io.github.berehum.teacups.command.arguments.CartArgument;
import io.github.berehum.teacups.command.arguments.CartGroupArgument;
import io.github.berehum.teacups.command.arguments.TeacupArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class SetRpmCommand extends TeacupCommand {

    private static final String setrpmString = "setrpm";
    private static final String rpmString = "rpm";
    private static final String permission = "teacups.command.setrpm";
    private static final String addToExistingString = "add to existing";

    public SetRpmCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal(setrpmString, rpmString)
                        .permission(permission)
                        .literal(Cart.name)
                        .argument(TeacupArgument.of(Teacup.name))
                        .argument(CartGroupArgument.of(CartGroup.name))
                        .argument(CartArgument.of(Cart.name))
                        .argument(IntegerArgument.of(rpmString))
                        .argument(BooleanArgument.optional(addToExistingString))
                        .handler(this::setRpmCart)
        );
        this.commandManager.registerSubcommand(builder ->
                builder.literal(setrpmString, rpmString)
                        .permission(permission)
                        .literal(CartGroup.name, "group")
                        .argument(TeacupArgument.of(Teacup.name))
                        .argument(CartGroupArgument.of(CartGroup.name))
                        .argument(IntegerArgument.of(rpmString))
                        .argument(BooleanArgument.optional(addToExistingString))
                        .handler(this::setRpmCartGroup)
        );
        this.commandManager.registerSubcommand(builder ->
                builder.literal(setrpmString, rpmString)
                        .permission(permission)
                        .literal(Teacup.name, "cup", "main")
                        .argument(TeacupArgument.of(Teacup.name))
                        .argument(IntegerArgument.of(rpmString))
                        .argument(BooleanArgument.optional(addToExistingString))
                        .handler(this::setRpmTeacup)
        );
    }

    private void setRpmCart(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get(Teacup.name);
        final CartGroup cartgroup = context.get(CartGroup.name);
        final Cart cart = context.get(Cart.name);
        final int rpm = context.get(rpmString);
        final Optional<Boolean> addToExisting = context.getOptional(addToExistingString);

        if (addToExisting.orElse(Boolean.FALSE)) {
            cart.setRpm(cart.getRpm() + rpm);
        } else {
            cart.setRpm(rpm);
        }

        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + cart.getId() + " in "
                + cartgroup.getId() + " in " + teacup.getId() + " to " + cart.getRpm());
    }

    private void setRpmCartGroup(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get(Teacup.name);
        final CartGroup cartgroup = context.get(CartGroup.name);
        final int rpm = context.get(rpmString);
        final Optional<Boolean> addToExisting = context.getOptional(addToExistingString);

        if (addToExisting.orElse(Boolean.FALSE)) {
            cartgroup.setRpm(cartgroup.getRpm() + rpm);
        } else {
            cartgroup.setRpm(rpm);
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + cartgroup.getId() + " in " + teacup.getId() + " to " + cartgroup.getRpm());
    }

    private void setRpmTeacup(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get(Teacup.name);
        final int rpm = context.get(rpmString);
        final Optional<Boolean> addToExisting = context.getOptional(addToExistingString);

        if (addToExisting.orElse(Boolean.FALSE)) {
            teacup.setRpm(teacup.getRpm() + rpm);
        } else {
            teacup.setRpm(rpm);
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + teacup.getId() + " to " + teacup.getRpm());
    }

}