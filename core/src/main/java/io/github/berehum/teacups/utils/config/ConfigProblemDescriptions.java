package io.github.berehum.teacups.utils.config;

public enum ConfigProblemDescriptions {

    INVALID_ACTION("Action '%s' is invalid"),
    INVALID_TIME("Time '%s' is invalid"),
    INVALID_ARGUMENT("Argument '%s' is invalid"),
    MISSING_ARGUMENT("Missing argument: %s");

    private final String description;

    ConfigProblemDescriptions(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public String getDescription(String... format) {
        return String.format(description, (Object[]) format);
    }

}
