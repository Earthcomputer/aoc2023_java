package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day9 implements Day {
    @Override
    public void part1() {
        int total = 0;
        for (int[] sequence : parseInput()) {
            total += predictNext(sequence, false);
        }
        System.out.println(total);
    }

    @Override
    public void part2() {
        int total = 0;
        for (int[] sequence : parseInput()) {
            total += predictNext(sequence, true);
        }
        System.out.println(total);
    }

    private int[][] parseInput() {
        return Util.readInput().stream()
            .filter(line -> !line.isEmpty())
            .map(line -> Arrays.stream(line.split(" "))
                .map(Util::parseIntOrNull)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .toArray())
            .toArray(int[][]::new);
    }

    private int predictNext(int[] sequence, boolean predictPrev) {
        List<int[]> deltas = new ArrayList<>();
        deltas.add(sequence);

        int[] prevDeltas;
        while (!Arrays.stream(prevDeltas = deltas.get(deltas.size() - 1)).allMatch(i -> i == 0) && prevDeltas.length > 1) {
            int[] nextDeltas = new int[prevDeltas.length - 1];
            for (int i = 0; i < nextDeltas.length; i++) {
                nextDeltas[i] = prevDeltas[i + 1] - prevDeltas[i];
            }
            deltas.add(nextDeltas);
        }

        int prediction = 0;
        for (int i = deltas.size() - 1; i >= 0; i--) {
            if (predictPrev) {
                prediction = deltas.get(i)[0] - prediction;
            } else {
                prediction += deltas.get(i)[deltas.get(i).length - 1];
            }
        }

        return prediction;
    }
}
