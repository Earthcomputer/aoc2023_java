package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day12 implements Day {
    @Override
    public void part1() {
        long total = 0;
        for (String line : Util.readInput()) {
            InputLine inputLine = InputLine.parse(line);
            if (inputLine == null) {
                continue;
            }
            total += getNumCombinations(inputLine.record, inputLine.contiguousGroups, new HashMap<>());
        }
        System.out.println(total);
    }

    @Override
    public void part2() {
        long total = 0;
        for (String line : Util.readInput()) {
            InputLine inputLine = InputLine.parse(line);
            if (inputLine == null) {
                continue;
            }
            List<SpringState> record = new ArrayList<>(inputLine.record);
            for (int i = 0; i < 4; i++) {
                record.add(SpringState.UNKNOWN);
                record.addAll(inputLine.record);
            }
            List<Integer> contiguousGroups = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                contiguousGroups.addAll(inputLine.contiguousGroups);
            }
            total += getNumCombinations(record, contiguousGroups, new HashMap<>());
        }
        System.out.println(total);
    }

    private long getNumCombinations(List<SpringState> record, List<Integer> contiguousGroups, Map<InputLine, Long> cache) {
        if (contiguousGroups.isEmpty()) {
            // check we didn't miss a #
            if (record.contains(SpringState.BROKEN)) {
                return 0;
            }

            return 1;
        }

        Long cachedValue = cache.get(new InputLine(record, contiguousGroups));
        if (cachedValue != null) {
            return cachedValue;
        }

        int firstGroupLen = contiguousGroups.get(0);
        long total = 0;
        for (int i = 0; i + firstGroupLen <= record.size(); i++) {
            boolean matches = !record.subList(i, i + firstGroupLen).contains(SpringState.FUNCTIONAL);
            if (i + firstGroupLen < record.size() && record.get(i + firstGroupLen) == SpringState.BROKEN) {
                // we would be skipping a # by placing here
                matches = false;
            }
            if (matches) {
                if (i + firstGroupLen == record.size()) {
                    if (contiguousGroups.size() == 1) {
                        total++;
                    }
                } else {
                    total += getNumCombinations(record.subList(i + firstGroupLen + 1, record.size()), contiguousGroups.subList(1, contiguousGroups.size()), cache);
                }
            }
            if (record.get(i) == SpringState.BROKEN) {
                // we can't skip a #
                break;
            }
        }

        cache.put(new InputLine(record, contiguousGroups), total);
        return total;
    }

    private enum SpringState {
        FUNCTIONAL, BROKEN, UNKNOWN
    }

    private record InputLine(List<SpringState> record, List<Integer> contiguousGroups) {
        @Nullable
        static InputLine parse(String line) {
            String[] parts = line.split(" ");
            if (parts.length != 2) {
                return null;
            }
            List<SpringState> record = new ArrayList<>(parts[0].length());
            for (int i = 0; i < parts[0].length(); i++) {
                switch (parts[0].charAt(i)) {
                    case '.' -> record.add(SpringState.FUNCTIONAL);
                    case '#' -> record.add(SpringState.BROKEN);
                    case '?' -> record.add(SpringState.UNKNOWN);
                    default -> {
                        return null;
                    }
                }
            }
            List<Integer> contiguousGroups = Arrays.stream(parts[1].split(",")).map(Util::parseIntOrNull).filter(Objects::nonNull).toList();
            return new InputLine(record, contiguousGroups);
        }
    }
}
