package io.github.berehum.teacups.command.arguments;


import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.Caption;
import cloud.commandframework.captions.CaptionVariable;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import io.github.berehum.teacups.attraction.components.Cart;
import io.github.berehum.teacups.attraction.components.CartGroup;
import io.github.berehum.teacups.attraction.components.Teacup;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Argument that parses into a {@link Cart}
 *
 * @param <C> Command sender type
 */
public final class CartArgument<C> extends CommandArgument<C, Cart> {

    private CartArgument(
            final boolean required,
            final @NonNull String name,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<C>, @NonNull String,
                    @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription defaultDescription
    ) {
        super(required, name, new CartParser<>(), defaultValue, Cart.class, suggestionsProvider, defaultDescription);
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
    public static <C> @NonNull CommandArgument<C, Cart> of(final @NonNull String name) {
        return CartArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Cart> optional(final @NonNull String name) {
        return CartArgument.<C>newBuilder(name).asOptional().build();
    }

    /**
     * Create a new required command component with a default value
     *
     * @param name        Component name
     * @param defaultCart Default cart
     * @param <C>         Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Cart> optional(
            final @NonNull String name,
            final @NonNull String defaultCart
    ) {
        return CartArgument.<C>newBuilder(name).asOptionalWithDefault(defaultCart).build();
    }


    public static final class Builder<C> extends CommandArgument.Builder<C, Cart> {

        private Builder(final @NonNull String name) {
            super(Cart.class, name);
        }

        /**
         * Builder a new boolean component
         *
         * @return Constructed component
         */
        @Override
        public @NonNull CartArgument<C> build() {
            return new CartArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }

    }


    public static final class CartParser<C> implements ArgumentParser<C, Cart> {

        @Override
        public @NonNull ArgumentParseResult<Cart> parse(final @NonNull CommandContext<C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
            final String input = inputQueue.peek();
            final CartGroup cartgroup = commandContext.get("cartgroup");
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(CartParser.class, commandContext));
            }
            inputQueue.remove();

            Optional<Cart> cart = Optional.ofNullable(cartgroup.getCarts().get(input));
            return cart.map(ArgumentParseResult::success).orElseGet(() -> ArgumentParseResult.failure(new CartParseException(input, commandContext)));
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
            CartGroup cartgroup = commandContext.get("cartgroup");
            Map<String, Cart> carts = cartgroup.getCarts();
            return (carts == null) ? new ArrayList<>() : new ArrayList<>(carts.keySet());
        }

    }

    public static final class CartParseException extends ParserException {

        private static final long serialVersionUID = 927476591631527552L;
        private final String input;

        /**
         * Construct a new Player parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public CartParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    CartParser.class,
                    context,
                    Caption.of("No cart found for 'input' in 'cartgroup' in 'teacup'"),
                    CaptionVariable.of("input", input),
                    CaptionVariable.of("cartgroup", ((CartGroup) context.get("cartgroup")).getId()),
                    CaptionVariable.of("teacup", (((Teacup) context.get("teacup")).getId()))
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
