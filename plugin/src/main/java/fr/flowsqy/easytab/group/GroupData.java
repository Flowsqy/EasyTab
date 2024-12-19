package fr.flowsqy.easytab.group;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record GroupData(@NotNull String groupName, int priority, @Nullable ChatColor color, @Nullable String prefix,
        @Nullable String suffix) {
}
