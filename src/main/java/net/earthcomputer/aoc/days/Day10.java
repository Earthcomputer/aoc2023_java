package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Day10 implements Day {
    @Override
    public void part1() {
        int[] loopLength = {0};
        if (!followLoop(Util.readInput(), point -> loopLength[0]++)) {
            return;
        }
        System.out.println(loopLength[0] / 2);
    }

    @Override
    public void part2() {
        List<String> grid = Util.readInput();
        Set<Point> loopSet = new HashSet<>();
        if (!followLoop(grid, loopSet::add)) {
            return;
        }

        int totalArea = 0;
        for (int y = 0; y < grid.size(); y++) {
            // for horizontal stretches of loop, "isInsideLoop" refers to above the loop.
            boolean isInsideLoop = false;
            String line = grid.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (loopSet.contains(new Point(x, y))) {
                    char loopChar = line.charAt(x);
                    switch (loopChar) {
                        case 'S' -> {
                            boolean left = startNeighborCheck(grid, x, y, loopSet, Delta.LEFT);
                            boolean right = startNeighborCheck(grid, x, y, loopSet, Delta.RIGHT);
                            boolean up = startNeighborCheck(grid, x, y, loopSet, Delta.UP);
                            boolean down = startNeighborCheck(grid, x, y, loopSet, Delta.DOWN);
                            if ((up && down) || (right && up) || (left && up)) {
                                isInsideLoop = !isInsideLoop;
                            }
                        }
                        case '|', 'L', 'J' -> isInsideLoop = !isInsideLoop;
                    }
                } else {
                    if (isInsideLoop) {
                        totalArea++;
                    }
                }
            }
        }

        System.out.println(totalArea);
    }

    private boolean startNeighborCheck(List<String> grid, int x, int y, Set<Point> loopSet, Delta direction) {
        return direction.boundsCheck(grid, x, y)
            && loopSet.contains(new Point(x + direction.dx, y + direction.dy))
            && DeltaPair.BY_CHAR.get(grid.get(y + direction.dy).charAt(x + direction.dx)).contains(direction.negate());
    }

    private boolean followLoop(List<String> grid, Consumer<Point> loopPointConsumer) {
        // find S
        int y = 0;
        int x = 0;
        boolean foundS = false;
        outer: for (; y < grid.size(); y++) {
            String line = grid.get(y);
            for (x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'S') {
                    foundS = true;
                    break outer;
                }
            }
        }
        if (!foundS) {
            System.out.println("Could not find S");
            return false;
        }

        loopPointConsumer.accept(new Point(x, y));

        // find loop next to S
        Delta direction = null;
        for (Delta d : Delta.ORTHOGONALS) {
            if (d.boundsCheck(grid, x, y)) {
                DeltaPair pair = DeltaPair.BY_CHAR.get(grid.get(y + d.dy).charAt(x + d.dx));
                if (pair != null && pair.contains(d.negate())) {
                    x += d.dx;
                    y += d.dy;
                    direction = d;
                    break;
                }
            }
        }
        if (direction == null) {
            System.out.println("Could not find loop from S");
            return false;
        }

        char c;
        while ((c = grid.get(y).charAt(x)) != 'S') {
            loopPointConsumer.accept(new Point(x, y));
            DeltaPair pair = DeltaPair.BY_CHAR.get(c);
            if (pair == null) {
                System.out.println("Loop ended");
                return false;
            }
            direction = pair.getOtherDelta(direction.negate());
            if (!direction.boundsCheck(grid, x, y)) {
                System.out.println("Loop went off the edge of the grid");
                return false;
            }
            x += direction.dx;
            y += direction.dy;
        }

        return true;
    }

    private record Delta(int dx, int dy) {
        static final Delta UP = new Delta(0, -1);
        static final Delta DOWN = new Delta(0, 1);
        static final Delta LEFT = new Delta(-1, 0);
        static final Delta RIGHT = new Delta(1, 0);
        static final Delta[] ORTHOGONALS = { UP, DOWN, LEFT, RIGHT };

        Delta negate() {
            return new Delta(-dx, -dy);
        }

        boolean boundsCheck(List<String> grid, int x, int y) {
            x += dx;
            y += dy;
            return y >= 0 && y < grid.size() && x >= 0 && x < grid.get(y).length();
        }
    }

    private record DeltaPair(Delta a, Delta b) {
        static final Map<Character, DeltaPair> BY_CHAR = new HashMap<>();
        static {
            BY_CHAR.put('|', new DeltaPair(Delta.UP, Delta.DOWN));
            BY_CHAR.put('-', new DeltaPair(Delta.LEFT, Delta.RIGHT));
            BY_CHAR.put('7', new DeltaPair(Delta.LEFT, Delta.DOWN));
            BY_CHAR.put('F', new DeltaPair(Delta.RIGHT, Delta.DOWN));
            BY_CHAR.put('J', new DeltaPair(Delta.LEFT, Delta.UP));
            BY_CHAR.put('L', new DeltaPair(Delta.RIGHT, Delta.UP));
        }

        boolean contains(Delta delta) {
            return a.equals(delta) || b.equals(delta);
        }

        Delta getOtherDelta(Delta delta) {
            return a.equals(delta) ? b : a;
        }
    }

    private record Point(int x, int y) {}
}
