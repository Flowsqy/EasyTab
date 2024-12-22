package fr.flowsqy.easytab.tab;

import org.jetbrains.annotations.NotNull;

import fr.flowsqy.easytab.group.GroupData;

public record PlayerSnapshot(@NotNull PlayerProfile profile, @NotNull GroupData[] groups) {
}
