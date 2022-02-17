package io.github.berehum.teacupspro.utils.config;

/**
 * @author Leonardo Bishop
 * @link https://github.com/LMBishop/Quests
 */
public final class ConfigProblem {

    private final ConfigProblemType type;
    private final String description;
    private final String line;

    public ConfigProblem(ConfigProblemType type, String description, String line) {
        this.type = type;
        this.description = (description == null) ? "?" : description;
        this.line = (line == null) ? "?" : line;
    }

    public ConfigProblemType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getLine() {
        return line;
    }

    public enum ConfigProblemType {

        ERROR("Error", "E", 1),
        WARNING("Warning", "W", 2);

        private final String title;
        private final String shortened;
        private final int priority;

        ConfigProblemType(String title, String shortened, int priority) {
            this.title = title;
            this.shortened = shortened;
            this.priority = priority;
        }

        public String getTitle() {
            return title;
        }

        public String getShortened() {
            return shortened;
        }

        public int getPriority() {
            return priority;
        }

    }
}
