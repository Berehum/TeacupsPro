package io.github.berehum.teacups.command.arguments;


import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.CaptionVariable;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.command.TeacupCaptionKeys;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiFunction;

/**
 * Argument that parses into a {@link Teacup}
 *
 * @param <C> Command sender type
 */
public final class TeacupArgument<C> extends CommandArgument<C, Teacup> {

    private TeacupArgument(
            final boolean required,
            final @NonNull String name,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<C>, @NonNull String,
                    @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription defaultDescription
    ) {
        super(required, name, new TeacupParser<>(), defaultValue, Teacup.class, suggestionsProvider, defaultDescription);
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
    public static <C> @NonNull CommandArgument<C, Teacup> of(final @NonNull String name) {
        return TeacupArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Teacup> optional(final @NonNull String name) {
        return TeacupArgument.<C>newBuilder(name).asOptional().build();
    }

    /**
     * Create a new required command component with a default value
     *
     * @param name          Component name
     * @param defaultTeacup Default teacup
     * @param <C>           Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Teacup> optional(
            final @NonNull String name,
            final @NonNull String defaultTeacup
    ) {
        return TeacupArgument.<C>newBuilder(name).asOptionalWithDefault(defaultTeacup).build();
    }


    public static final class Builder<C> extends CommandArgument.Builder<C, Teacup> {

        private Builder(final @NonNull String name) {
            super(Teacup.class, name);
        }

        /**
         * Builder a new boolean component
         *
         * @return Constructed component
         */
        @Override
        public @NonNull TeacupArgument<C> build() {
            return new TeacupArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }

    }


    public static final class TeacupParser<C> implements ArgumentParser<C, Teacup> {

        @Override
        public @NonNull ArgumentParseResult<Teacup> parse(final @NonNull CommandContext<C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
            final String input = inputQueue.peek();
            if (input == null || input.isEmpty()) {
                return ArgumentParseResult.failure(new NoInputProvidedException(TeacupParser.class, commandContext));
            }
            inputQueue.remove();
            Optional<Teacup> teacup = TeacupsMain.getInstance().getTeacupManager().getTeacup(input);
            return teacup.map(ArgumentParseResult::success).orElseGet(() -> ArgumentParseResult.failure(new TeacupParseException(input, commandContext)));
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
            return new ArrayList<>(TeacupsMain.getInstance().getTeacupManager().getTeacups().keySet());
        }

    }


    /**
     * Teacup parse exception
     */
    public static final class TeacupParseException extends ParserException {

        private static final long serialVersionUID = 2448491509768905719L;
        private final String input;

        /**
         * Construct a new Teacup parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public TeacupParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    TeacupParser.class,
                    context,
                    TeacupCaptionKeys.ARGUMENT_PARSE_FAILURE_TEACUP,
                    CaptionVariable.of("input", input)
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