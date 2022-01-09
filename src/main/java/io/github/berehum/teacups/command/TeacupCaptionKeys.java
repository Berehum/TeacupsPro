package io.github.berehum.teacups.command;

import cloud.commandframework.captions.Caption;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class TeacupCaptionKeys {

    private static final Collection<Caption> RECOGNIZED_CAPTIONS = new LinkedList<>();

    /**
     * Variables: {input}
     */
    public static final Caption ARGUMENT_PARSE_FAILURE_TEACUP = of("argument.parse.failure.teacup");
    /**
     * Variables: {input}, {teacup}
     */
    public static final Caption ARGUMENT_PARSE_FAILURE_CARTGROUP = of("argument.parse.failure.cartgroup");
    /**
     * Variables: {input}, {teacup}, {cartgroup}
     */
    public static final Caption ARGUMENT_PARSE_FAILURE_CART = of("argument.parse.failure.cart");
    /**
     * Variables: {input}
     */
    public static final Caption ARGUMENT_PARSE_FAILURE_SHOW = of("argument.parse.failure.show");

    private TeacupCaptionKeys() {
    }

    private static @NonNull Caption of(final @NonNull String key) {
        final Caption caption = Caption.of(key);
        RECOGNIZED_CAPTIONS.add(caption);
        return caption;
    }

    /**
     * Get an immutable collection containing all standard caption keys
     *
     * @return Immutable collection of keys
     */
    public static @NonNull Collection<@NonNull Caption> getTeacupCaptionKeys() {
        return Collections.unmodifiableCollection(RECOGNIZED_CAPTIONS);
    }
}
