package io.github.berehum.teacups.show.actions;

import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Cart;
import io.github.berehum.teacups.attraction.components.CartGroup;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.show.reader.ShowFileReader;
import io.github.berehum.teacups.show.reader.lines.type.ShowActionType;
import io.github.berehum.teacups.utils.config.ConfigProblem;
import io.github.berehum.teacups.utils.config.ConfigProblemDescriptions;
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
                loadTeacup(filename, line, args);
                break;
            case "group":
            case "cartgroup":
                loadCartGroup(filename, line, args);
                break;
            case "cart":
                loadCart(filename, line, args);
                break;
            default:
                ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                        ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(affect), String.valueOf(line)));
        }
        return loaded;
    }

    private void loadTeacup(String filename, int line, String[] args) {
        data = new String[0];
        try {
            rpm = Integer.parseInt(args[1]);
        } catch (Exception e) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(args[1]), String.valueOf(line)));
            return;
        }
        loaded = true;
    }

    private void loadCartGroup(String filename, int line, String[] args) {
        data = new String[1];
        data[0] = args[1];
        try {
            rpm = Integer.parseInt(args[2]);
        } catch (Exception e) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(args[2]), String.valueOf(line)));
            return;
        }
        loaded = true;
    }

    private void loadCart(String filename, int line, String[] args) {
        data = new String[2];
        data[0] = args[1];
        data[1] = args[2];
        try {
            rpm = Integer.parseInt(args[3]);
        } catch (Exception e) {
            ShowFileReader.addConfigProblem(filename, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ARGUMENT.getDescription(args[3]), String.valueOf(line)));
            return;
        }
        loaded = true;
    }

    @Override
    public void execute(Teacup teacup) {
        if (!loaded) return;

        switch (affect) {
            case "teacup":
                executeTeacup(teacup);
                return;
            case "cartgroup":
                executeCartGroup(teacup);
                return;
            case "cart":
                executeCart(teacup);
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

    @Override
    public @NotNull ShowActionType getType() {
        return type;
    }
}
