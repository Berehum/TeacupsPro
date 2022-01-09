package io.github.berehum.teacups.command.arguments;


import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.CaptionVariable;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import io.github.berehum.teacups.attraction.components.CartGroup;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.TeacupCaptionKeys;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Argument that parses into a {@link io.github.berehum.teacups.attraction.components.CartGroup}
 *
 * @param <C> Command sender type
 */
public final class CartGroupArgument<C> extends CommandArgument<C, CartGroup> {

    private CartGroupArgument(
            final boolean required,
            final @NonNull String name,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<C>, @NonNull String,
                    @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription defaultDescription
    ) {
        super(required, name, new CartGroupParser<>(), defaultValue, CartGroup.class, suggestionsProvider, defaultDescription);
    }

    /**
     * Create a new builder
     *
     * @param name Name of the component
     * @param <C>  Command sender type
     * @return Created builder
     */
    public static <C> @NonNull Builder<C> newBuilder(final @NonNull String name) {
        return new Builder<>(name);
    }

    /**
     * Create a new required command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, CartGroup> of(final @NonNull String name) {
        return CartGroupArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, CartGroup> optional(final @NonNull String name) {
        return CartGroupArgument.<C>newBuilder(name).asOptional().build();
    }

    /**
     * Create a new required command component with a default value
     *
     * @param name             Component name
     * @param defaultCartGroup Default cartgroup
     * @param <C>              Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, CartGroup> optional(
            final @NonNull String name,
            final @NonNull String defaultCartGroup
    ) {
        return CartGroupArgument.<C>newBuilder(name).asOptionalWithDefault(defaultCartGroup).build();
    }


    public static final class Builder<C> extends CommandArgument.Builder<C, CartGroup> {

        private Builder(final @NonNull String name) {
            super(CartGroup.class, name);
        }

        /**
         * Builder a new boolean component
         *
         * @return Constructed component
         */
        @Override
        public @NonNull CartGroupArgument<C> build() {
            return new CartGroupArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }

    }


    public static final class CartGroupParser<C> implements ArgumentParser<C, CartGroup> {

        @Override
        public @NonNull ArgumentParseResult<CartGroup> parse(final @NonNull CommandContext<C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
            final String input = inputQueue.peek();
            final Teacup teacup = commandContext.get(Teacup.NAME);
            if (input == null || input.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(CartGroupParser.class, commandContext));
            }
            inputQueue.remove();

            Optional<CartGroup> cartGroup = Optional.ofNullable(teacup.getCartGroups().get(input));
            return cartGroup.map(ArgumentParseResult::success).orElseGet(() -> ArgumentParseResult.failure(new CartGroupParseException(input, commandContext)));
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
            Teacup teacup = commandContext.get("teacup");
            Map<String, CartGroup> cartGroups = teacup.getCartGroups();
            return (cartGroups == null) ? new ArrayList<>() : new ArrayList<>(cartGroups.keySet());
        }

    }

    public static final class CartGroupParseException extends ParserException {

        private static final long serialVersionUID = 3065238321298579780L;
        private final String input;

        /**
         * Construct a new Player parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public CartGroupParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    CartGroupParser.class,
                    context,
                    TeacupCaptionKeys.ARGUMENT_PARSE_FAILURE_CARTGROUP,
                    CaptionVariable.of("input", input),
                    CaptionVariable.of(Teacup.NAME, (((Teacup) context.get(Teacup.NAME)).getId()))
            );
            this.input = input;
        }

        /**
         * Get the supplied input
         *
         * @return String value
         */
        public @NonNull String getInput() {
            return this.input;
        }

    }

}
