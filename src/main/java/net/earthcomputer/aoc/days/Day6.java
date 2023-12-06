package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day6 implements Day {
    @Override
    public void part1() {
        Input input = Input.parse(Util.readInput());
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }
        
        long product = 1;
        for (int i = 0; i < input.times.length; i++) {
            product *= getWinningCount(input.times[i], input.distances[i]);
        }

        System.out.println(product);
    }

    @Override
    public void part2() {
        Input input = Input.parse(Util.readInput().stream().map(s -> s.replace(" ", "")).toList());
        if (input == null || input.times.length != 1) {
            System.out.println("Invalid input");
            return;
        }

        System.out.println(getWinningCount(input.times[0], input.distances[0]));
    }
    
    private long getWinningCount(long totalTime, long distanceToBeat) {
        long winningCount = 0;
        for (long holdTime = 0; holdTime < totalTime; holdTime++) {
            long distance = (totalTime - holdTime) * holdTime;
            if (distance > distanceToBeat) {
                winningCount++;
            }
        }
        return winningCount;
    }

    private record Input(long[] times, long[] distances) {
        @Nullable
        static Input parse(List<String> lines) {
            if (lines.size() < 2) {
                return null;
            }
            
            String timeLine = lines.get(0);
            if (!timeLine.startsWith("Time:")) {
                return null;
            }
            long[] times = Arrays.stream(timeLine.substring("Time:".length()).split(" "))
                .map(Util::parseLongOrNull)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .toArray();

            String distanceLine = lines.get(1);
            if (!distanceLine.startsWith("Distance:")) {
                return null;
            }
            long[] distances = Arrays.stream(distanceLine.substring("Distance:".length()).split(" "))
                .map(Util::parseLongOrNull)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .toArray();

            if (times.length != distances.length) {
                return null;
            }

            return new Input(times, distances);
        }
    }
}
