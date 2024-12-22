package fr.flowsqy.easytab.tab;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class ScoreboardManager {

    public void executeChanges(@NotNull List<TeamUpdate> updates) {
        final Set<Scoreboard> scoreboards = collectScoreboards();
        for (var scoreboard : scoreboards) {
            executeChanges(updates, scoreboard);
        }
    }

    private void executeChanges(@NotNull List<TeamUpdate> updates, @NotNull Scoreboard scoreboard) {
        for (var update : updates) {
            if (update.newName() == null) {
                final var team = scoreboard.getTeam(update.previousName());
                if (team != null) {
                    team.unregister();
                }
                continue;
            }
            final var previousTeam = update.previousName() == null ? null : scoreboard.getTeam(update.previousName());
            final var newTeam = scoreboard.registerNewTeam(update.newName());
            newTeam.addEntry(update.playerSnapshot().profile().name());
            applyFormat(newTeam, update.playerSnapshot());
            if (previousTeam != null) {
                copyFromPrevious(newTeam, previousTeam);
                previousTeam.unregister();
            }
        }
    }

    private void applyFormat(@NotNull Team team, @NotNull PlayerSnapshot playerSnapshot) {

    }

    private void copyFromPrevious(@NotNull Team newTeam, @NotNull Team previousTeam) {
    }

    private Set<Scoreboard> collectScoreboards() {
        final var scoreboards = new HashSet<Scoreboard>();
        for (var player : Bukkit.getOnlinePlayers()) {
            scoreboards.add(player.getScoreboard());
        }
        return scoreboards;
    }

}
