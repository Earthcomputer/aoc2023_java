package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;

import java.util.ArrayList;
import java.util.List;

public class Day11 implements Day {
    @Override
    public void part1() {
        run(1);
    }

    @Override
    public void part2() {
        run(999999);
    }

    private void run(long extra) {
        List<Point> points = parseInput();

        long maxX = points.stream().mapToLong(Point::x).max().orElse(0);
        long maxY = points.stream().mapToLong(Point::y).max().orElse(0);

        for (long x = maxX; x >= 0; x--) {
            long x_f = x;
            boolean isColumnEmpty = points.stream().noneMatch(point -> point.x == x_f);
            if (isColumnEmpty) {
                for (int i = 0; i < points.size(); i++) {
                    Point point = points.get(i);
                    if (point.x > x) {
                        points.set(i, new Point(point.x + extra, point.y));
                    }
                }
            }
        }

        for (long y = maxY; y >= 0; y--) {
            long y_f = y;
            boolean isRowEmpty = points.stream().noneMatch(point -> point.y == y_f);
            if (isRowEmpty) {
                for (int i = 0; i < points.size(); i++) {
                    Point point = points.get(i);
                    if (point.y > y) {
                        points.set(i, new Point(point.x, point.y + extra));
                    }
                }
            }
        }

        long total = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                total += Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
            }
        }

        System.out.println(total);
    }

    private List<Point> parseInput() {
        List<String> lines = Util.readInput();
        List<Point> result = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    result.add(new Point(x, y));
                }
            }
        }
        return result;
    }

    private record Point(long x, long y) {}
}
