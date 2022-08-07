package io.github.berehum.teacupspro.command;

import cloud.commandframework.bukkit.BukkitCaptionRegistry;

public class TeacupCaptionRegistry<C> extends BukkitCaptionRegistry<C> {

    /**
     * Default caption for {@link TeacupCaptionKeys#ARGUMENT_PARSE_FAILURE_TEACUP}
     */
    public static final String ARGUMENT_PARSE_FAILURE_TEACUP = "No teacup attraction found for '{input}'";
    /**
     * Default caption for {@link TeacupCaptionKeys#ARGUMENT_PARSE_FAILURE_CARTGROUP}
     */
    public static final String ARGUMENT_PARSE_FAILURE_CARTGROUP = "No cartgroup found for '{input}' in teacup '{teacup}'";
    /**
     * Default caption for {@link TeacupCaptionKeys#ARGUMENT_PARSE_FAILURE_CART}
     */
    public static final String ARGUMENT_PARSE_FAILURE_CART = "No cart found for '{input}' in cartgroup '{cartgroup}' in  teacup '{teacup}'";
    /**
     * Default caption for {@link TeacupCaptionKeys#ARGUMENT_PARSE_FAILURE_SHOW}
     */
    public static final String ARGUMENT_PARSE_FAILURE_SHOW = "No show found for '{input}'";

    protected TeacupCaptionRegistry() {
        super();
        this.registerMessageFactory(
                TeacupCaptionKeys.ARGUMENT_PARSE_FAILURE_TEACUP,
                (caption, sender) -> ARGUMENT_PARSE_FAILURE_TEACUP
        );
        this.registerMessageFactory(
                TeacupCaptionKeys.ARGUMENT_PARSE_FAILURE_CARTGROUP,
                (caption, sender) -> ARGUMENT_PARSE_FAILURE_CARTGROUP
        );
        this.registerMessageFactory(
                TeacupCaptionKeys.ARGUMENT_PARSE_FAILURE_CART,
                (caption, sender) -> ARGUMENT_PARSE_FAILURE_CART
        );
        this.registerMessageFactory(
                TeacupCaptionKeys.ARGUMENT_PARSE_FAILURE_SHOW,
                (caption, sender) -> ARGUMENT_PARSE_FAILURE_SHOW
        );
    }

}
