package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day18 implements Day {
    @Override
    public void part1() {
        run(parseInput());
    }

    @Override
    public void part2() {
        run(parseInput().stream().map(instruction -> new Instruction(
            switch (instruction.color & 15) {
                case 1 -> Dir.DOWN;
                case 2 -> Dir.LEFT;
                case 3 -> Dir.UP;
                /*case 0,*/ default -> Dir.RIGHT;
            },
            instruction.color >> 4,
            instruction.color
        )).toList());
    }

    private void run(List<Instruction> input) {
        List<Point> points = new ArrayList<>(input.size());

        long x = 0;
        long y = 0;
        long perimeter = 0;
        for (Instruction instruction : input) {
            switch (instruction.dir) {
                case DOWN -> y += instruction.distance;
                case UP -> y -= instruction.distance;
                case LEFT -> x -= instruction.distance;
                case RIGHT -> x += instruction.distance;
            }
            points.add(new Point(x, y));
            perimeter += Math.abs(instruction.distance);
        }

        long realArea = 0;
        for (int i = 0; i < points.size(); i++) {
            realArea += (points.get(i).y + points.get((i + 1) % points.size()).y) * (points.get(i).x - points.get((i + 1) % points.size()).x);
        }
        realArea /= 2;

        long interiorArea = Math.abs(realArea) - perimeter / 2 + 1;
        long area = perimeter + interiorArea;
        System.out.println(area);
    }

    private List<Instruction> parseInput() {
        return Util.readInput().stream().map(line -> {
            String[] parts = line.split(" ");
            if (parts.length != 3) {
                return null;
            }

            if (parts[0].length() != 1) {
                return null;
            }
            Dir dir = Dir.fromChar(parts[0].charAt(0));
            if (dir == null) {
                return null;
            }

            Long distance = Util.parseLongOrNull(parts[1]);
            if (distance == null) {
                return null;
            }

            if (!parts[2].startsWith("(#") || !parts[2].endsWith(")")) {
                return null;
            }
            Integer color = Util.parseIntOrNull(parts[2].substring(2, parts[2].length() - 1), 16);
            if (color == null) {
                return null;
            }

            return new Instruction(dir, distance, color);
        }).filter(Objects::nonNull).toList();
    }

    private enum Dir {
        UP, DOWN, LEFT, RIGHT;

        @Nullable
        static Dir fromChar(char c) {
            return switch (c) {
                case 'U' -> UP;
                case 'D' -> DOWN;
                case 'L' -> LEFT;
                case 'R' -> RIGHT;
                default -> null;
            };
        }
    }

    private record Instruction(Dir dir, long distance, int color) {
    }

    private record Point(long x, long y) {
    }
}
