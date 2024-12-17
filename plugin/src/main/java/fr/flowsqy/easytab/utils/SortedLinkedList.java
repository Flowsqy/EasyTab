package fr.flowsqy.easytab.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SortedLinkedList<T> {

    private Node first;
    private int size;
    private final Comparator<T> comparator;

    public SortedLinkedList(@NotNull Comparator<T> comparator) {
        this.comparator = comparator;
        size = 0;
        first = null;
    }

    @NotNull
    public List<Update<T>> add(@NotNull T element) {
        size++;
        // Each position are calculated with two divisions to avoid buffer overflow
        if (first == null) {
            final long newPosition = Long.MAX_VALUE >>> 2;
            first = new Node(newPosition, element);
            return Collections.singletonList(new Update<>(-1, newPosition, element));
        }
        if (comparator.compare(element, first.value) < 0) {
            final long newPosition = first.position >>> 2;
            final Node newNode = new Node(newPosition, element);
            newNode.next = first;
            first = newNode;
            if (newPosition == 0) {
                return recalculatePositions();
            }
            return Collections.singletonList(new Update<>(-1, newPosition, element));
        }
        var currentNode = first;
        while (currentNode.next != null) {
            final int comp = comparator.compare(element, currentNode.value);
            if (comp > 0) {
                currentNode = currentNode.next;
                continue;
            }
            final long newPosition = currentNode.position >>> 2 + currentNode.next.position >>> 2;
            final Node newNode = new Node(newPosition, element);
            newNode.next = currentNode.next;
            currentNode.next = newNode;
            if (newPosition == currentNode.position) {
                return recalculatePositions();
            }
            return Collections.singletonList(new Update<>(-1, newPosition, element));
        }
        final long newPosition = Long.MAX_VALUE >>> 2 + currentNode.position >>> 2;
        final Node newNode = new Node(newPosition, element);
        currentNode.next = newNode;
        if (newPosition == currentNode.position) {
            return recalculatePositions();
        }
        return Collections.singletonList(new Update<>(-1, newPosition, element));
    }

    @NotNull
    private List<Update<T>> recalculatePositions() {
        final List<Update<T>> updates = new LinkedList<>();
        final long space = Long.MAX_VALUE / (size + 1);
        long position = space;
        Node node = first;
        while (node != null) {
            updates.add(new Update<>(node.position, position, node.value));
            node.position = position;
            position += space;
            node = node.next;
        }
        return updates;
    }

    @Nullable
    public Update<T> remove(T element) {
        if (first == null) {
            return null;
        }
        if (first.next == null) {
            if (!first.value.equals(element)) {
                return null;
            }
            final Update<T> update = new Update<>(first.position, -1, first.value);
            first = null;
            size--;
            return update;
        }
        Node currentNode = first;
        while (currentNode.next != null && !currentNode.next.value.equals(element)) {
            currentNode = currentNode.next;
        }
        final var next = currentNode.next;
        if (next == null) {
            return null;
        }

        final Update<T> update = new Update<>(next.position, -1, next.value);
        currentNode.next = next.next;
        size--;
        return update;
    }

    public record Update<T>(long previousPosition, long newPosition, T value) {
    }

    private class Node {

        private Node next;
        private long position;
        private final T value;

        public Node(long position, @NotNull T value) {
            this.position = position;
            this.value = value;
        }

    }

}
