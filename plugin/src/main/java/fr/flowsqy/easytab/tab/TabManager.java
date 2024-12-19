package fr.flowsqy.easytab.tab;

import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import fr.flowsqy.easytab.group.GroupData;
import fr.flowsqy.easytab.utils.NameTranslator;
import fr.flowsqy.easytab.utils.SortedLinkedList;

public class TabManager {

    private final Lock lock;
    private final SortedLinkedList<PlayerSnapshot> list;
    private final NameTranslator nameTranslator;

    private record PlayerSnapshot(@NotNull PlayerProfile profile, @NotNull GroupData[] groups) {
    }

    public TabManager() {
        lock = new ReentrantLock();
        nameTranslator = new NameTranslator(10, Comparator.comparing(String::valueOf, (s1, s2) -> s1.compareTo(s2)));
        final Comparator<PlayerSnapshot> comparator = ((ps1, ps2) -> {
            // TODO Compare groups
        }).thenComparing(ps -> ps.profile().name());
        list = new SortedLinkedList<>(comparator, nameTranslator.getMaxValue());
    }

    public void add(@NotNull Player player, @NotNull GroupData[] groups) {
        lock.lock();
        try {
            // Handle adding
        } finally {
            lock.unlock();
        }
    }

}
