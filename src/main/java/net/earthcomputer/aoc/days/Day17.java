package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Day17 implements Day {
    @Override
    public void part1() {
        run(1, 3);
    }

    @Override
    public void part2() {
        run(4, 10);
    }

    private void run(int minDistance, int maxDistance) {
        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        Node[] graph = new Node[input.grid.length];
        Arrays.setAll(graph, i -> Node.huge());
        graph[0].h = 0;
        graph[0].v = 0;

        PriorityQueue<QueueEntry> queue = new PriorityQueue<>();
        queue.add(QueueEntry.from(graph, 0));
        QueueEntry entry;
        while ((entry = queue.poll()) != null) {
            if (!entry.isValid(graph)) {
                continue;
            }
            int x = entry.index % input.width;
            int y = entry.index / input.width;

            if (graph[entry.index].v != Integer.MAX_VALUE) {
                int dist = graph[entry.index].v;
                for (int dx = 1; x + dx < input.width && dx <= maxDistance; dx++) {
                    int i = y * input.width + x + dx;
                    dist += input.grid[i];
                    if (dx >= minDistance && dist < graph[i].h) {
                        graph[i].h = dist;
                        queue.add(QueueEntry.from(graph, i));
                    }
                }

                dist = graph[entry.index].v;
                for (int dx = 1; dx <= x && dx <= maxDistance; dx++) {
                    int i = y * input.width + x - dx;
                    dist += input.grid[i];
                    if (dx >= minDistance && dist < graph[i].h) {
                        graph[i].h = dist;
                        queue.add(QueueEntry.from(graph, i));
                    }
                }
            }

            if (graph[entry.index].h != Integer.MAX_VALUE) {
                int dist = graph[entry.index].h;
                for (int dy = 1; (y + dy) * input.width < input.grid.length && dy <= maxDistance; dy++) {
                    int i = (y + dy) * input.width + x;
                    dist += input.grid[i];
                    if (dy >= minDistance && dist < graph[i].v) {
                        graph[i].v = dist;
                        queue.add(QueueEntry.from(graph, i));
                    }
                }

                dist = graph[entry.index].h;
                for (int dy = 1; dy <= y && dy <= maxDistance; dy++) {
                    int i = (y - dy) * input.width + x;
                    dist += input.grid[i];
                    if (dy >= minDistance && dist < graph[i].v) {
                        graph[i].v = dist;
                        queue.add(QueueEntry.from(graph, i));
                    }
                }
            }
        }

        System.out.println(Math.min(graph[graph.length - 1].h, graph[graph.length - 1].v));
    }

    private record Input(int width, int[] grid) {
        @Nullable
        static Input parse() {
            List<String> lines = Util.readInput().stream().filter(line -> !line.isEmpty()).toList();
            if (lines.isEmpty() || !lines.stream().allMatch(line -> line.length() == lines.get(0).length())) {
                return null;
            }
            return new Input(lines.get(0).length(), lines.stream().flatMapToInt(String::chars).map(i -> i - '0').toArray());
        }
    }

    private static final class Node {
        int h, v;

        static Node huge() {
            Node n = new Node();
            n.h = Integer.MAX_VALUE;
            n.v = Integer.MAX_VALUE;
            return n;
        }
    }

    private record QueueEntry(int index, int cmp) implements Comparable<QueueEntry> {
        static QueueEntry from(Node[] graph, int index) {
            return new QueueEntry(index, Math.min(graph[index].h, graph[index].v));
        }

        boolean isValid(Node[] graph) {
            return cmp == Math.min(graph[index].h, graph[index].v);
        }

        @Override
        public int compareTo(QueueEntry o) {
            return Integer.compare(cmp, o.cmp);
        }
    }
}
