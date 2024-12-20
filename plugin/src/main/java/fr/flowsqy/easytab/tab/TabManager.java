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
        final Comparator<PlayerSnapshot> comparator = new GroupsComparator().thenComparing(ps -> ps.profile().name());
        list = new SortedLinkedList<>(comparator, nameTranslator.getMaxValue());
    }

    private static class GroupsComparator implements Comparator<PlayerSnapshot> {
        @Override
        public int compare(PlayerSnapshot ps1, PlayerSnapshot ps2) {
            final int maxIndex;
            final int result;
            final GroupData[] groups1 = ps1.groups(), groups2 = ps2.groups();
            final int l1 = groups1.length, l2 = groups2.length;
            if (l1 < l2) {
                maxIndex = l1;
                result = 1;
            } else if (l2 < l1) {
                maxIndex = l2;
                result = -1;
            } else {
                maxIndex = l1;
                result = 0;
            }
            for (int index = 0; index < maxIndex; index++) {
                final int comp = Integer.compare(-groups1[index].priority(), -groups2[index].priority());
                if (comp != 0) {
                    return comp;
                }
            }
            return result;
        }
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
