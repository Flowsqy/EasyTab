package fr.flowsqy.easytab.listener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import fr.flowsqy.easytab.group.GroupData;
import fr.flowsqy.easytab.permission.GroupExtractor;
import fr.flowsqy.easytab.tab.PlayerProfile;
import fr.flowsqy.easytab.tab.ScoreboardManager;
import fr.flowsqy.easytab.tab.TabManager;

public class ConnectionListener implements Listener {

    private final Map<String, GroupData> groupDatas;
    private final GroupExtractor groupExtractor;
    private final TabManager tabManager;
    private final ScoreboardManager scoreboardManager;

    public ConnectionListener(@NotNull Map<String, GroupData> groupDatas, @NotNull GroupExtractor groupExtractor,
            @NotNull TabManager tabManager, @NotNull ScoreboardManager scoreboardManager) {
        this.groupDatas = groupDatas;
        this.groupExtractor = groupExtractor;
        this.tabManager = tabManager;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        final String[] groups = groupExtractor.extract(player);
        final var playerGroupDatas = new LinkedList<GroupData>();
        for (var group : groups) {
            final var groupData = groupDatas.get(group);
            if (groupData == null) {
                continue;
            }
            playerGroupDatas.add(groupData);
        }

        final var updates = tabManager.add(new PlayerProfile(player.getUniqueId(), player.getName()),
                playerGroupDatas.toArray(GroupData[]::new));
        scoreboardManager.executeChanges(updates);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onQuit(PlayerQuitEvent event) {
        final var player = event.getPlayer();
        final var update = tabManager.remove(new PlayerProfile(player.getUniqueId(), player.getName()));
        if (update == null) {
            return;
        }
        scoreboardManager.executeChanges(Collections.singletonList(update));
    }

}
