package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day4 implements Day {
    @Override
    public void part1() {
        int total = 0;
        for (String line : Util.readInput()) {
            int numMatching = getNumMatching(line);
            if (numMatching > 0) {
                total += 1 << (numMatching - 1);
            }
        }
        System.out.println(total);
    }

    @Override
    public void part2() {
        List<String> lines = Util.readInput().stream().filter(line -> !line.isEmpty()).toList();
        int[] counts = new int[lines.size()];
        Arrays.fill(counts, 1);

        int total = 0;
        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];
            total += count;

            int numMatching = getNumMatching(lines.get(i));
            for (int k = 1; k <= numMatching; k++) {
                if (i + k < counts.length) {
                    counts[i + k] += count;
                }
            }
        }

        System.out.println(total);
    }

    private int getNumMatching(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex == -1) {
            return 0;
        }
        String lineAfterColon = line.substring(colonIndex);

        int pipeIndex = lineAfterColon.indexOf('|');
        if (pipeIndex == -1) {
            return 0;
        }

        Set<Integer> myNumbers = new HashSet<>();
        for (String myNumber : lineAfterColon.substring(pipeIndex + 1).split(" ")) {
            if (!myNumber.isEmpty()) {
                Integer n = Util.parseIntOrNull(myNumber);
                if (n != null) {
                    myNumbers.add(n);
                }
            }
        }

        int numMatching = 0;

        for (String theirNumber : lineAfterColon.substring(0, pipeIndex).split(" ")) {
            if (!theirNumber.isEmpty()) {
                Integer n = Util.parseIntOrNull(theirNumber);
                if (n != null && myNumbers.contains(n)) {
                    numMatching++;
                }
            }
        }

        return numMatching;
    }
}
