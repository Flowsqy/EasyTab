package fr.flowsqy.easytab.tab;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public record PlayerProfile(@NotNull UUID id, @NotNull String name) {
}
