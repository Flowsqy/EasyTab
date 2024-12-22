package fr.flowsqy.easytab.tab;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.flowsqy.easytab.group.GroupData;
import fr.flowsqy.easytab.utils.NameTranslator;
import fr.flowsqy.easytab.utils.SortedLinkedList;
import fr.flowsqy.easytab.utils.SortedLinkedList.Update;

public class TabManager {

    private final Lock lock;
    private final SortedLinkedList<PlayerSnapshot> list;
    private final NameTranslator nameTranslator;

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

    @NotNull
    private List<Update<PlayerSnapshot>> unsafeAdd(@NotNull PlayerProfile playerProfile, @NotNull GroupData[] groups) {
        final var copiedGroups = new GroupData[groups.length];
        System.arraycopy(groups, 0, copiedGroups, 0, groups.length);
        Arrays.sort(copiedGroups, Comparator.comparingInt(GroupData::priority));
        return list.add(new PlayerSnapshot(playerProfile, copiedGroups));
    }

    @Nullable
    private Update<PlayerSnapshot> unsafeRemove(@NotNull PlayerProfile playerProfile) {
        return list.removeFirst(ps -> ps.profile().equals(playerProfile));
    }

    @NotNull
    public List<TeamUpdate> add(@NotNull PlayerProfile playerProfile, @NotNull GroupData[] groups) {
        lock.lock();
        try {
            final var updates = unsafeAdd(playerProfile, groups);
            final var teamUpdates = new LinkedList<TeamUpdate>();
            for (var update : updates) {
                final var previousName = update.previousPosition() < 0 ? null
                        : nameTranslator.translate(update.previousPosition());
                final var newName = nameTranslator.translate(update.newPosition());
                final var teamUpdate = new TeamUpdate(previousName, newName, update.value());
                teamUpdates.add(teamUpdate);
            }
            return teamUpdates;
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    public TeamUpdate remove(@NotNull PlayerProfile playerProfile) {
        lock.lock();
        try {
            final var update = unsafeRemove(playerProfile);
            if (update == null) {
                return null;
            }
            final var previousName = nameTranslator.translate(update.previousPosition());
            return new TeamUpdate(previousName, null, update.value());
        } finally {
            lock.unlock();
        }
    }

    @NotNull
    public List<TeamUpdate> actualize(@NotNull PlayerProfile playerProfile, @NotNull GroupData[] groups) {
        lock.lock();
        try {
            final var removeUpdate = unsafeRemove(playerProfile);
            if (removeUpdate == null) {
                return add(playerProfile, groups);
            }
            final var updates = unsafeAdd(playerProfile, groups);
            final var teamUpdates = new LinkedList<TeamUpdate>();
            for (var update : updates) {
                final var previousPosition = update.value().profile().equals(playerProfile)
                        ? removeUpdate.previousPosition()
                        : update.previousPosition();
                final var previousName = nameTranslator.translate(previousPosition);
                final var newName = nameTranslator.translate(update.newPosition());
                final var teamUpdate = new TeamUpdate(previousName, newName, update.value());
                teamUpdates.add(teamUpdate);
            }
            return teamUpdates;
        } finally {
            lock.unlock();
        }
    }

}
