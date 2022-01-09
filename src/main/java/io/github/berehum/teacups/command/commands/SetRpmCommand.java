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

    private static final String SET_RPM = "setrpm";
    private static final String RPM = "rpm";
    private static final String PERMISSION = "teacups.command.setrpm";
    private static final String ADD_TO_EXISTING = "add to existing";

    public SetRpmCommand(final @NonNull TeacupsMain plugin, final @NonNull CommandManager commandManager) {
        super(plugin, commandManager);
    }

    @Override
    public void register() {
        this.commandManager.registerSubcommand(builder ->
                builder.literal(SET_RPM, RPM)
                        .permission(PERMISSION)
                        .literal(Cart.NAME)
                        .argument(TeacupArgument.of(Teacup.NAME))
                        .argument(CartGroupArgument.of(CartGroup.NAME))
                        .argument(CartArgument.of(Cart.NAME))
                        .argument(IntegerArgument.of(RPM))
                        .argument(BooleanArgument.optional(ADD_TO_EXISTING))
                        .handler(this::setRpmCart)
        );
        this.commandManager.registerSubcommand(builder ->
                builder.literal(SET_RPM, RPM)
                        .permission(PERMISSION)
                        .literal(CartGroup.NAME, "group")
                        .argument(TeacupArgument.of(Teacup.NAME))
                        .argument(CartGroupArgument.of(CartGroup.NAME))
                        .argument(IntegerArgument.of(RPM))
                        .argument(BooleanArgument.optional(ADD_TO_EXISTING))
                        .handler(this::setRpmCartGroup)
        );
        this.commandManager.registerSubcommand(builder ->
                builder.literal(SET_RPM, RPM)
                        .permission(PERMISSION)
                        .literal(Teacup.NAME, "cup", "main")
                        .argument(TeacupArgument.of(Teacup.NAME))
                        .argument(IntegerArgument.of(RPM))
                        .argument(BooleanArgument.optional(ADD_TO_EXISTING))
                        .handler(this::setRpmTeacup)
        );
    }

    private void setRpmCart(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get(Teacup.NAME);
        final CartGroup cartgroup = context.get(CartGroup.NAME);
        final Cart cart = context.get(Cart.NAME);
        final int rpm = context.get(RPM);
        final Optional<Boolean> addToExisting = context.getOptional(ADD_TO_EXISTING);

        if (addToExisting.orElse(Boolean.FALSE)) {
            cart.setRpm(cart.getRpm() + rpm);
        } else {
            cart.setRpm(rpm);
        }

        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + cart.getId() + " in "
                + cartgroup.getId() + " in " + teacup.getId() + " to " + cart.getRpm());
    }

    private void setRpmCartGroup(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get(Teacup.NAME);
        final CartGroup cartgroup = context.get(CartGroup.NAME);
        final int rpm = context.get(RPM);
        final Optional<Boolean> addToExisting = context.getOptional(ADD_TO_EXISTING);

        if (addToExisting.orElse(Boolean.FALSE)) {
            cartgroup.setRpm(cartgroup.getRpm() + rpm);
        } else {
            cartgroup.setRpm(rpm);
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + cartgroup.getId() + " in " + teacup.getId() + " to " + cartgroup.getRpm());
    }

    private void setRpmTeacup(final @NonNull CommandContext<CommandSender> context) {
        final Teacup teacup = context.get(Teacup.NAME);
        final int rpm = context.get(RPM);
        final Optional<Boolean> addToExisting = context.getOptional(ADD_TO_EXISTING);

        if (addToExisting.orElse(Boolean.FALSE)) {
            teacup.setRpm(teacup.getRpm() + rpm);
        } else {
            teacup.setRpm(rpm);
        }
        context.getSender().sendMessage(ChatColor.GREEN + "You've set the rpm of " + teacup.getId() + " to " + teacup.getRpm());
    }

}