package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 implements Day {
    @Override
    public void part1() {
        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        Long steps = getNumberOfSteps(input, "AAA", "ZZZ"::equals);
        if (steps == null) {
            return;
        }

        System.out.println(steps);
    }

    @Override
    public void part2() {
        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        long result = 1;
        for (String location : input.graph.keySet()) {
            if (location.endsWith("A")) {
                Long steps = getNumberOfSteps(input, location, l -> l.endsWith("Z"));
                if (steps == null) {
                    return;
                }
                result = result * steps / gcd(result, steps);
            }
        }

        System.out.println(result);
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long temp = a;
            a = b;
            b = temp % b;
        }
        return a;
    }

    @Nullable
    private Long getNumberOfSteps(Input input, String startingLocation, Predicate<String> isEndingLocation) {
        long steps = 0;
        String location = startingLocation;

        while (!isEndingLocation.test(location)) {
            GraphValue graphValue = input.graph.get(location);
            if (graphValue == null) {
                System.out.println("No value associated with location " + location);
                return null;
            }
            if (input.instructions.charAt((int) (steps % input.instructions.length())) == 'L') {
                location = graphValue.left;
            } else {
                location = graphValue.right;
            }
            steps++;
        }

        return steps;
    }

    private record GraphValue(String left, String right) {}

    private record Input(String instructions, Map<String, GraphValue> graph) {
        private static final Pattern LINE_PATTERN = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

        @Nullable
        static Input parse() {
            List<String> lines = Util.readInput();
            if (lines.size() < 3) {
                return null;
            }

            String instructions = lines.get(0);
            if (instructions.isEmpty()) {
                return null;
            }
            for (int i = 0; i < instructions.length(); i++) {
                char c = instructions.charAt(i);
                if (c != 'L' && c != 'R') {
                    return null;
                }
            }

            Map<String, GraphValue> graph = new HashMap<>();

            for (String line : lines.subList(2, lines.size())) {
                Matcher matcher = LINE_PATTERN.matcher(line);
                if (!matcher.matches()) {
                    continue;
                }
                String key = matcher.group(1);
                String left = matcher.group(2);
                String right = matcher.group(3);
                graph.put(key, new GraphValue(left, right));
            }

            return new Input(instructions, graph);
        }
    }
}
