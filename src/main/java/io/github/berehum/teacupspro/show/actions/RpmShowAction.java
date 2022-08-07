package io.github.berehum.teacupspro.show.actions;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.attraction.components.Cart;
import io.github.berehum.teacupspro.attraction.components.CartGroup;
import io.github.berehum.teacupspro.attraction.components.Teacup;
import io.github.berehum.teacupspro.show.actions.type.ShowActionType;
import io.github.berehum.teacupspro.show.reader.ShowFileReader;
import io.github.berehum.teacupspro.utils.config.ConfigProblem;
import io.github.berehum.teacupspro.utils.config.ConfigProblemDescriptions;
import org.jetbrains.annotations.NotNull;

public class RpmShowAction implements IShowAction {

    private static final ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get("rpm");
    private boolean loaded = false;

    private String affect;
    private int rpm;
    private String[] data;


    //stop rpm teacup 0
    //stop rpm cartgroup cartgroup1 0
    //stop rpm cart cartgroup1 cart 1 0
    @Override
    public boolean load(String filename, int line, String[] args) {
        if (args.length < 2) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.MISSING_ARGUMENT.getDescription("MISSING ARGUMENTS"), String.valueOf(line)));
            return loaded;
        }

        affect = args[0].toLowerCase();

        switch (affect) {
            case "cup":
            case "teacup":
            case "group":
            case "cartgroup":
            case "groups":
            case "cartgroups":
            case "cart":
            case "carts":
                load2(filename, line, args);
                break;
            default:
                ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                        ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(affect), String.valueOf(line)));
        }
        return loaded;
    }

    private void load2(String filename, int line, String[] args) {
        int dataLength = args.length - 2;
        data = new String[dataLength];
        System.arraycopy(args, 1, data, 0, dataLength);
        try {
            rpm = Integer.parseInt(args[dataLength + 1]);
        } catch (Exception e) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(args[dataLength + 1]), String.valueOf(line)));
            return;
        }
        loaded = true;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;

        switch (affect) {
            case "teacup":
            case "cup":
                executeTeacup(teacup);
                return;
            case "cartgroup":
            case "group":
                executeCartGroup(teacup);
                return;
            case "cart":
                executeCart(teacup);
                return;
            case "cartgroups":
            case "groups":
                executeCartGroups(teacup);
                return;
            case "carts":
                executeCarts(teacup);
        }
    }

    private void executeTeacup(Teacup teacup) {
        teacup.setRpm(rpm);
    }

    private void executeCartGroup(Teacup teacup) {
        CartGroup cartGroup = teacup.getCartGroups().get(data[0]);
        if (cartGroup == null) return;
        cartGroup.setRpm(rpm);
    }

    private void executeCart(Teacup teacup) {
        CartGroup cartGroup = teacup.getCartGroups().get(data[0]);
        if (cartGroup == null) return;
        Cart cart = cartGroup.getCarts().get(data[1]);
        cart.setRpm(rpm);
    }

    private void executeCartGroups(Teacup teacup) {
        teacup.getCartGroups().values().forEach(group -> group.setRpm(rpm));
    }
    private void executeCarts(Teacup teacup) {
        teacup.getCartGroups().values().forEach(group -> group.getCarts().values().forEach(cart -> cart.setRpm(rpm)));
    }

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
