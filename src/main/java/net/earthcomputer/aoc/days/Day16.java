package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Day16 implements Day {
    private static final int NORTH = 1;
    private static final int EAST = 2;
    private static final int SOUTH = 4;
    private static final int WEST = 8;

    @Override
    public void part1() {
        char[][] grid = readInput();
        if (grid == null) {
            System.out.println("Invalid input");
            return;
        }

        System.out.println(run(grid, 0, 0, EAST));
    }

    @Override
    public void part2() {
        char[][] grid = readInput();
        if (grid == null) {
            System.out.println("Invalid input");
            return;
        }

        int max = 0;
        for (int startY = 0; startY < grid.length; startY++) {
            max = Math.max(max, run(grid, 0, startY, EAST));
            max = Math.max(max, run(grid, grid[0].length - 1, startY, WEST));
        }
        for (int startX = 0; startX < grid[0].length; startX++) {
            max = Math.max(max, run(grid, startX, 0, SOUTH));
            max = Math.max(max, run(grid, startX, grid.length - 1, NORTH));
        }
        System.out.println(max);
    }

    private int run(char[][] grid, int startX, int startY, int startDir) {
        byte[][] beams = new byte[grid.length][grid[0].length];

        Deque<Beam> beamsToProcess = new ArrayDeque<>();
        beamsToProcess.add(new Beam(startX, startY, startDir));

        while (!beamsToProcess.isEmpty()) {
            Beam beam = beamsToProcess.removeLast();
            if ((beams[beam.y][beam.x] & beam.dir) != 0) {
                continue;
            }
            beams[beam.y][beam.x] |= (byte) beam.dir;
            switch (grid[beam.y][beam.x]) {
                case '.' -> {
                    switch (beam.dir) {
                        case NORTH -> north(beam, grid, beamsToProcess);
                        case EAST -> east(beam, grid, beamsToProcess);
                        case SOUTH -> south(beam, grid, beamsToProcess);
                        case WEST -> west(beam, grid, beamsToProcess);
                    }
                }
                case '/' -> {
                    switch (beam.dir) {
                        case NORTH -> east(beam, grid, beamsToProcess);
                        case EAST -> north(beam, grid, beamsToProcess);
                        case SOUTH -> west(beam, grid, beamsToProcess);
                        case WEST -> south(beam, grid, beamsToProcess);
                    }
                }
                case '\\' -> {
                    switch (beam.dir) {
                        case NORTH -> west(beam, grid, beamsToProcess);
                        case EAST -> south(beam, grid, beamsToProcess);
                        case SOUTH -> east(beam, grid, beamsToProcess);
                        case WEST -> north(beam, grid, beamsToProcess);
                    }
                }
                case '-' -> {
                    switch (beam.dir) {
                        case NORTH, SOUTH -> {
                            west(beam, grid, beamsToProcess);
                            east(beam, grid, beamsToProcess);
                        }
                        case EAST -> east(beam, grid, beamsToProcess);
                        case WEST -> west(beam, grid, beamsToProcess);
                    }
                }
                case '|' -> {
                    switch (beam.dir) {
                        case WEST, EAST -> {
                            north(beam, grid, beamsToProcess);
                            south(beam, grid, beamsToProcess);
                        }
                        case NORTH -> north(beam, grid, beamsToProcess);
                        case SOUTH -> south(beam, grid, beamsToProcess);
                    }
                }
            }
        }

        int count = 0;
        for (byte[] row : beams) {
            for (byte b : row) {
                if (b != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private void north(Beam beam, char[][] grid, Deque<Beam> beamsToProcess) {
        if (beam.y > 0) {
            beamsToProcess.addLast(new Beam(beam.x, beam.y - 1, NORTH));
        }
    }

    private void east(Beam beam, char[][] grid, Deque<Beam> beamsToProcess) {
        if (beam.x < grid[0].length - 1) {
            beamsToProcess.addLast(new Beam(beam.x + 1, beam.y, EAST));
        }
    }

    private void south(Beam beam, char[][] grid, Deque<Beam> beamsToProcess) {
        if (beam.y < grid.length - 1) {
            beamsToProcess.addLast(new Beam(beam.x, beam.y + 1, SOUTH));
        }
    }

    private void west(Beam beam, char[][] grid, Deque<Beam> beamsToProcess) {
        if (beam.x > 0) {
            beamsToProcess.addLast(new Beam(beam.x - 1, beam.y, WEST));
        }
    }

    private char[] @Nullable [] readInput() {
        char[][] ret = Util.readInput().stream().filter(s -> !s.isEmpty()).map(String::toCharArray).toArray(char[][]::new);
        if (ret.length == 0) {
            return null;
        }
        if (Arrays.stream(ret).anyMatch(row -> row.length != ret[0].length)) {
            return null;
        }
        return ret;
    }

    private record Beam(int x, int y, int dir) {}
}
