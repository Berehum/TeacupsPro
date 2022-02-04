package io.github.berehum.teacupspro.show.reader;

import io.github.berehum.teacupspro.TeacupsMain;
import io.github.berehum.teacupspro.show.reader.lines.IShowFileLine;
import io.github.berehum.teacupspro.show.reader.lines.StateShowFileLine;
import io.github.berehum.teacupspro.show.reader.lines.TickShowFileLine;
import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionType;
import io.github.berehum.teacupspro.utils.config.ConfigProblem;
import io.github.berehum.teacupspro.utils.config.ConfigProblemDescriptions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowFileReader {

    private static final Map<String, List<ConfigProblem>> configProblems = new HashMap<>();

    private ShowFileReader() {
    }

    public static List<IShowFileLine> getShowFileLines(File file) {
        List<IShowFileLine> lines = new ArrayList<>();
        if (!file.isFile() || !file.exists()) return lines;
        String fileName = file.getName();
        configProblems.remove(fileName);

        try (Stream<String> stream = Files.lines((file.toPath()))) {
            int lineNo = 1;
            for (String line : stream.collect(Collectors.toList())) {
                IShowFileLine showFileLine = getShowFileLine(fileName, lineNo, line);
                if (showFileLine != null) {
                    lines.add(showFileLine);
                }
                lineNo++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    //START rpm teacup cartgroup cart 100
    private static IShowFileLine getShowFileLine(String fileName, int lineNo, String line) {
        //skip empty lines
        if (line.isEmpty()) {
            return null;
        }
        IShowFileLine showFileLine;
        String[] args = line.split(" ");

        //allow comments with #
        if (args[0].startsWith("#")) {
            return null;
        }

        boolean isTickLine = true;

        int tick = -1;
        StateShowFileLine.State state = null;
        try {
            tick = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            isTickLine = false;
        }

        if (isTickLine) {
            if (tick < 0) {
                addConfigProblem(fileName, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                        ConfigProblemDescriptions.INVALID_TIME.getDescription(args[0]), String.valueOf(lineNo)));
                return null;
            }
        } else {
            state = StateShowFileLine.State.get(args[0]);
            if (state == null) {
                addConfigProblem(fileName, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                        ConfigProblemDescriptions.INVALID_TIME.getDescription(args[0]), String.valueOf(lineNo)));
                return null;
            }
        }

        ShowActionType type = TeacupsMain.getInstance().getShowActionTypes().get(args[1]);
        if (type == null) {
            addConfigProblem(fileName, new ConfigProblem(ConfigProblem.ConfigProblemType.ERROR,
                    ConfigProblemDescriptions.INVALID_ACTION.getDescription(args[1]), String.valueOf(lineNo)));
            return null;
        }

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(args));
        arrayList.remove(0);
        arrayList.remove(0);
        String[] newArgs = arrayList.toArray(new String[0]);

        if (isTickLine) {
            showFileLine = new TickShowFileLine(tick, type, fileName, lineNo, newArgs);
        } else {
            showFileLine = new StateShowFileLine(state, type, fileName, lineNo, newArgs);
        }

        if (showFileLine.toAction() == null) return null;
        return showFileLine;
    }

    public static Map<String, List<ConfigProblem>> getConfigProblems() {
        return configProblems;
    }

    public static void addConfigProblems(String fileName, List<ConfigProblem> configProblemList) {
        List<ConfigProblem> problems = configProblems.get(fileName);
        if (problems == null) problems = new ArrayList<>();
        problems.addAll(configProblemList);
        configProblems.put(fileName, problems);
    }

    public static void addConfigProblem(String fileName, ConfigProblem configProblem) {
        List<ConfigProblem> problems = configProblems.get(fileName);
        if (problems == null) problems = new ArrayList<>();
        problems.add(configProblem);
        configProblems.put(fileName, problems);
    }

    public static void showProblems(CommandSender sender, String fileName, List<ConfigProblem> problems) {
        if (problems == null || problems.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Teacups did not detect any problems with your configuration.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Detected problems and potential issues:");
        Set<ConfigProblem.ConfigProblemType> problemTypes = new HashSet<>();
        int count = 0;
        Map<ConfigProblem.ConfigProblemType, List<ConfigProblem>> sortedProblems = new EnumMap<>(ConfigProblem.ConfigProblemType.class);
        for (ConfigProblem problem : problems) {
            ConfigProblem.ConfigProblemType problemType = problem.getType();
            problemTypes.add(problemType);

            List<ConfigProblem> specificProblems = (sortedProblems.containsKey(problemType)) ? sortedProblems.get(problemType) : new ArrayList<>();
            specificProblems.add(problem);
            sortedProblems.put(problemType, specificProblems);
        }

        ConfigProblem.ConfigProblemType highest = null;
        for (ConfigProblem.ConfigProblemType type : ConfigProblem.ConfigProblemType.values()) {
            if (!sortedProblems.containsKey(type)) continue;
            highest = type;
            break;
        }

        ChatColor highestColor = matchConfigProblemToColor(highest);

        sender.sendMessage(highestColor + fileName + ChatColor.DARK_GRAY + " ----");
        for (ConfigProblem.ConfigProblemType type : ConfigProblem.ConfigProblemType.values()) {
            if (!sortedProblems.containsKey(type)) continue;
            for (ConfigProblem problem : sortedProblems.get(type)) {
                sender.sendMessage(ChatColor.DARK_GRAY + " | - " + matchConfigProblemToColor(problem.getType())
                        + problem.getType().getShortened() + ChatColor.DARK_GRAY + ": "
                        + ChatColor.GRAY + problem.getDescription() + ChatColor.DARK_GRAY + " : line " + problem.getLine());
                count++;
            }
        }

        List<String> legend = new ArrayList<>();
        for (ConfigProblem.ConfigProblemType type : ConfigProblem.ConfigProblemType.values()) {
            if (!problemTypes.contains(type)) continue;
            legend.add(matchConfigProblemToColor(type) + type.getShortened() + ChatColor.DARK_GRAY + " = " + matchConfigProblemToColor(type) + type.getTitle());
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "----");
        sender.sendMessage(ChatColor.GRAY.toString() + count + " problem(s) | " + String.join(ChatColor.DARK_GRAY + ", ", legend));
    }

    //------------------------------------------------------------------------------------------------------------------

    public static void showProblems(CommandSender sender, Map<String, List<ConfigProblem>> problems) {
        if (problems == null || problems.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Teacups did not detect any problems with your configuration.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "Detected problems and potential issues:");
        Set<ConfigProblem.ConfigProblemType> problemTypes = new HashSet<>();
        int count = 0;
        for (Map.Entry<String, List<ConfigProblem>> entry : problems.entrySet()) {
            Map<ConfigProblem.ConfigProblemType, List<ConfigProblem>> sortedProblems = new EnumMap<>(ConfigProblem.ConfigProblemType.class);
            for (ConfigProblem problem : entry.getValue()) {
                if (sortedProblems.containsKey(problem.getType())) {
                    sortedProblems.get(problem.getType()).add(problem);
                } else {
                    List<ConfigProblem> specificProblems = new ArrayList<>();
                    specificProblems.add(problem);
                    sortedProblems.put(problem.getType(), specificProblems);
                }
                problemTypes.add(problem.getType());
            }
            ConfigProblem.ConfigProblemType highest = null;
            for (ConfigProblem.ConfigProblemType type : ConfigProblem.ConfigProblemType.values()) {
                if (sortedProblems.containsKey(type)) {
                    highest = type;
                    break;
                }
            }
            ChatColor highestColor = matchConfigProblemToColor(highest);

            sender.sendMessage(highestColor + entry.getKey() + ChatColor.DARK_GRAY + " ----");
            for (ConfigProblem.ConfigProblemType type : ConfigProblem.ConfigProblemType.values()) {
                if (sortedProblems.containsKey(type)) {
                    for (ConfigProblem problem : sortedProblems.get(type)) {
                        sender.sendMessage(ChatColor.DARK_GRAY + " | - " + matchConfigProblemToColor(problem.getType())
                                + problem.getType().getShortened() + ChatColor.DARK_GRAY + ": "
                                + ChatColor.GRAY + problem.getDescription() + ChatColor.DARK_GRAY + " : line " + problem.getLine());
                        count++;
                    }
                }
            }
        }
        List<String> legend = new ArrayList<>();
        for (ConfigProblem.ConfigProblemType type : ConfigProblem.ConfigProblemType.values()) {
            if (problemTypes.contains(type)) {
                legend.add(matchConfigProblemToColor(type) + type.getShortened() + ChatColor.DARK_GRAY + " = " + matchConfigProblemToColor(type) + type.getTitle());
            }
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "----");

        sender.sendMessage(ChatColor.GRAY.toString() + count + " problem(s) | " + String.join(ChatColor.DARK_GRAY + ", ", legend));
    }

    public static ChatColor matchConfigProblemToColor(ConfigProblem.ConfigProblemType configProblem) {
        if (configProblem == null) {
            return ChatColor.WHITE;
        }
        switch (configProblem) {
            case ERROR:
                return ChatColor.RED;
            case WARNING:
                return ChatColor.YELLOW;
            default:
                return ChatColor.WHITE;
        }
    }
}
