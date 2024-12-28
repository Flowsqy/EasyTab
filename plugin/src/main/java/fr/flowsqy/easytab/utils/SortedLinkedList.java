package fr.flowsqy.easytab.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SortedLinkedList<T> implements Iterable<T> {

    private final long maxValue;
    private Node first;
    private int size;
    private final Comparator<T> comparator;

    public SortedLinkedList(@NotNull Comparator<T> comparator, long maxValue) {
        this.comparator = comparator;
        this.maxValue = maxValue;
        size = 0;
        first = null;
    }

    @NotNull
    public List<Update<T>> add(@NotNull T element) {
        size++;
        // Each position are calculated with two divisions to avoid buffer overflow
        if (first == null) {
            final long newPosition = maxValue >>> 1;
            first = new Node(newPosition, element);
            return Collections.singletonList(new Update<>(-1, newPosition, element));
        }
        if (comparator.compare(element, first.value) < 0) {
            final long newPosition = first.position >>> 1;
            final Node newNode = new Node(newPosition, element);
            newNode.next = first;
            first = newNode;
            if (newPosition == 0) {
                return recalculatePositions(newNode);
            }
            return Collections.singletonList(new Update<>(-1, newPosition, element));
        }
        var currentNode = first;
        while (currentNode.next != null) {
            final int comp = comparator.compare(element, currentNode.next.value);
            if (comp > 0) {
                currentNode = currentNode.next;
                continue;
            }
            final long newPosition = (currentNode.position >>> 1) + (currentNode.next.position >>> 1);
            final Node newNode = new Node(newPosition, element);
            newNode.next = currentNode.next;
            currentNode.next = newNode;
            if (newPosition == currentNode.position) {
                return recalculatePositions(newNode);
            }
            return Collections.singletonList(new Update<>(-1, newPosition, element));
        }
        final long newPosition = (maxValue >>> 1) + (currentNode.position >>> 1);
        final Node newNode = new Node(newPosition, element);
        currentNode.next = newNode;
        if (newPosition == currentNode.position) {
            return recalculatePositions(newNode);
        }
        return Collections.singletonList(new Update<>(-1, newPosition, element));
    }

    @NotNull
    private List<Update<T>> recalculatePositions(@NotNull Node newNode) {
        final List<Update<T>> updates = new LinkedList<>();
        final long space = maxValue / (size + 1);
        long position = space;
        Node node = first;
        while (node != null) {
            updates.add(new Update<>(node == newNode ? -1 : node.position, position, node.value));
            node.position = position;
            position += space;
            node = node.next;
        }
        return updates;
    }

    @Nullable
    public Update<T> removeFirst(@NotNull Predicate<T> predicate) {
        if (first == null) {
            return null;
        }
        if (predicate.test(first.value)) {
            final Update<T> update = new Update<>(first.position, -1, first.value);
            first = first.next;
            size--;
            return update;
        }
        Node currentNode = first;
        while (currentNode.next != null && !predicate.test(currentNode.next.value)) {
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

    @NotNull
    public List<Update<T>> clear() {
        if (first == null) {
            return Collections.emptyList();
        }
        final var updates = new LinkedList<Update<T>>();
        var node = first;
        while (node != null) {
            updates.add(new Update<>(node.position, -1, node.value));
            node = node.next;
        }
        first = null;
        return updates;
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

    @Override
    public Iterator<T> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<T> {

        private Node next = first;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            final var node = next;
            next = next.next;
            return node.value;
        }

    }

}
