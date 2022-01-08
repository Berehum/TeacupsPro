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
import io.github.berehum.teacups.TeacupsMain;
import io.github.berehum.teacups.show.Show;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiFunction;

/**
 * Argument that parses into a {@link Show}
 *
 * @param <C> Command sender type
 */
public final class ShowArgument<C> extends CommandArgument<C, Show> {

    private ShowArgument(
            final boolean required,
            final @NonNull String name,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<C>, @NonNull String,
                    @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription defaultDescription
    ) {
        super(required, name, new ShowParser<>(), defaultValue, Show.class, suggestionsProvider, defaultDescription);
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
    public static <C> @NonNull CommandArgument<C, Show> of(final @NonNull String name) {
        return ShowArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional command component
     *
     * @param name Component name
     * @param <C>  Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Show> optional(final @NonNull String name) {
        return ShowArgument.<C>newBuilder(name).asOptional().build();
    }

    /**
     * Create a new required command component with a default value
     *
     * @param name          Component name
     * @param defaultShow Default teacup
     * @param <C>           Command sender type
     * @return Created component
     */
    public static <C> @NonNull CommandArgument<C, Show> optional(
            final @NonNull String name,
            final @NonNull String defaultShow
    ) {
        return ShowArgument.<C>newBuilder(name).asOptionalWithDefault(defaultShow).build();
    }


    public static final class Builder<C> extends CommandArgument.Builder<C, Show> {

        private Builder(final @NonNull String name) {
            super(Show.class, name);
        }

        /**
         * Builder a new boolean component
         *
         * @return Constructed component
         */
        @Override
        public @NonNull ShowArgument<C> build() {
            return new ShowArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }

    }


    public static final class ShowParser<C> implements ArgumentParser<C, Show> {

        @Override
        public @NonNull ArgumentParseResult<Show> parse(final @NonNull CommandContext<C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(ShowParser.class, commandContext));
            }
            inputQueue.remove();

            Optional<Show> show = TeacupsMain.getInstance().getShowManager().getShow(input);
            return show.map(ArgumentParseResult::success).orElseGet(() -> ArgumentParseResult.failure(new ShowParseException(input, commandContext)));
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
            return new ArrayList<>(TeacupsMain.getInstance().getShowManager().getShowMap().keySet());
        }

    }


    /**
     * Show parse exception
     */
    public static final class ShowParseException extends ParserException {

        private static final long serialVersionUID = 927476591631527552L;
        private final String input;

        /**
         * Construct a new Show parse exception
         *
         * @param input   String input
         * @param context Command context
         */
        public ShowParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    ShowParser.class,
                    context,
                    Caption.of("No show found for '{input}'"),
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