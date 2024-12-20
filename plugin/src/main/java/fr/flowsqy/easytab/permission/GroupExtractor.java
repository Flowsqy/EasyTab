package fr.flowsqy.easytab.permission;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;

public class GroupExtractor {

    private final LuckPerms luckperms;

    public GroupExtractor(@NotNull LuckPerms luckperms) {
        this.luckperms = luckperms;
    }

    public String[] extract(@NotNull Player player) {
        final var playerAdapter = luckperms.getPlayerAdapter(Player.class);
        final var user = playerAdapter.getUser(player);
        final var groups = user.getInheritedGroups(playerAdapter.getQueryOptions(player));
        return groups.stream().map(Group::getName).toArray(String[]::new);
    }

}
