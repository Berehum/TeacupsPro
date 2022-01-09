package io.github.berehum.teacups.show;

import io.github.berehum.teacups.attraction.components.Teacup;
import io.github.berehum.teacups.show.actions.IShowAction;
import io.github.berehum.teacups.show.reader.ShowFileReader;
import io.github.berehum.teacups.show.reader.lines.IShowFileLine;
import io.github.berehum.teacups.show.reader.lines.StateShowFileLine;
import io.github.berehum.teacups.show.reader.lines.TickShowFileLine;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show {

    private final Map<Teacup, BukkitTask> activeShows = new HashMap<>();

    private final List<IShowAction> startActions = new ArrayList<>();
    private final List<IShowAction> stopActions = new ArrayList<>();
    private final Map<Integer, List<IShowAction>> showActionMap = new HashMap<>();

    private File loadedFile;
    private int highestTick = 0;

    public void disable() {
        activeShows.clear();
    }

    public void startShow(JavaPlugin plugin, Teacup teacup) {
        if (teacup.getActiveShow() != null) return;
        teacup.setActiveShow(this);
        executeStartActions(teacup);

        //Teacups don't stop unless the command is executed.
        if (showActionMap.isEmpty()) return;

        activeShows.put(teacup, new BukkitRunnable() {

            int currentTick = 0;

            @Override
            public void run() {
                if (!teacup.isActive()) {
                    this.cancel();
                    return;
                }

                //Teacups stop whenever the latest tick show action is reached.
                if (currentTick > highestTick) {
                    teacup.stop(true);
                }

                List<IShowAction> showActions = showActionMap.get(currentTick);
                if (showActions != null) {
                    showActions.forEach(action -> action.execute(teacup));
                }
                currentTick++;
            }

        }.runTaskTimer(plugin, 0L, 1L));

    }

    public void stopShow(Teacup teacup) {
        if (teacup.getActiveShow() != this) return;
        teacup.setActiveShow(null);
        executeStopActions(teacup);
        BukkitTask task = activeShows.get(teacup);
        if (task != null && !task.isCancelled()) task.cancel();
        activeShows.remove(teacup);
    }

    public boolean load(File file) {
        startActions.clear();
        stopActions.clear();
        showActionMap.clear();
        List<IShowFileLine> showFileLines = ShowFileReader.getShowFileLines(file);
        for (IShowFileLine line : showFileLines) {
            if (line instanceof TickShowFileLine) {
                TickShowFileLine tickLine = (TickShowFileLine) line;
                int tick = tickLine.getTick();

                if (tick > highestTick) {
                    highestTick = tick;
                }

                List<IShowAction> showActions = (showActionMap.containsKey(tick)) ? showActionMap.get(tick) : new ArrayList<>();
                showActions.add(tickLine.toAction());
                showActionMap.put(tick, showActions);
                continue;
            }
            if (line instanceof StateShowFileLine) {
                StateShowFileLine stateLine = (StateShowFileLine) line;
                StateShowFileLine.State state = stateLine.getState();
                if (state == StateShowFileLine.State.START) {
                    startActions.add(stateLine.toAction());
                } else {
                    stopActions.add(stateLine.toAction());
                }
            }
        }

        loadedFile = file;
        return true;
    }

    public void executeStartActions(Teacup teacup) {
        startActions.forEach(action -> action.execute(teacup));
    }

    public void executeStopActions(Teacup teacup) {
        stopActions.forEach(action -> action.execute(teacup));
    }

    public File getFile() {
        return loadedFile;
    }
}
