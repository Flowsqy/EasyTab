package fr.flowsqy.easytab.tab;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TeamUpdate(@Nullable String previousName, @Nullable String newName,
        @NotNull PlayerSnapshot playerSnapshot) {
}
