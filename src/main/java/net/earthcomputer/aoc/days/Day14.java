package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day14 implements Day {
    @Override
    public void part1() {
        char[][] grid = readInput();
        if (grid == null) {
            System.out.println("Invalid input");
            return;
        }

        tiltNorth(grid);

        System.out.println(computeLoad(grid));
    }

    @Override
    public void part2() {
        char[][] grid = readInput();
        if (grid == null) {
            System.out.println("Invalid input");
            return;
        }

        Map<HashableGrid, Integer> seenGrids = new HashMap<>();
        for (int i = 0; i < 1000000000; i++) {
            Integer lastSeenIndex = seenGrids.get(new HashableGrid(grid));
            if (lastSeenIndex != null) {
                int sameIterAs = (1000000000 - lastSeenIndex) % (i - lastSeenIndex) + lastSeenIndex;
                var foundGrid = seenGrids.entrySet().stream().filter(entry -> entry.getValue() == sameIterAs).map(entry -> entry.getKey().grid).findAny();
                if (foundGrid.isEmpty()) {
                    System.out.println("Didn't encounter the correct grid!");
                    return;
                }
                grid = foundGrid.get();
                break;
            }

            seenGrids.put(new HashableGrid(copyGrid(grid)), i);
            tiltNorth(grid);
            tiltWest(grid);
            tiltSouth(grid);
            tiltEast(grid);
        }

        System.out.println(computeLoad(grid));
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

    private char[][] copyGrid(char[][] grid) {
        char[][] newGrid = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            newGrid[i] = grid[i].clone();
        }
        return newGrid;
    }

    private int computeLoad(char[][] grid) {
        int total = 0;
        for (int rowNum = 0; rowNum < grid.length; rowNum++) {
            for (int colNum = 0; colNum < grid[0].length; colNum++) {
                if (grid[rowNum][colNum] == 'O') {
                    total += grid.length - rowNum;
                }
            }
        }
        return total;
    }

    private void tiltNorth(char[][] grid) {
        for (int rowNum = 0; rowNum < grid.length; rowNum++) {
            for (int colNum = 0; colNum < grid[0].length; colNum++) {
                if (grid[rowNum][colNum] == 'O') {
                    int moveToRow;
                    for (moveToRow = rowNum; moveToRow > 0; moveToRow--) {
                        if (grid[moveToRow - 1][colNum] != '.') {
                            break;
                        }
                    }
                    grid[rowNum][colNum] = '.';
                    grid[moveToRow][colNum] = 'O';
                }
            }
        }
    }

    private void tiltSouth(char[][] grid) {
        for (int rowNum = grid.length - 1; rowNum >= 0; rowNum--) {
            for (int colNum = 0; colNum < grid[0].length; colNum++) {
                if (grid[rowNum][colNum] == 'O') {
                    int moveToRow;
                    for (moveToRow = rowNum; moveToRow < grid.length - 1; moveToRow++) {
                        if (grid[moveToRow + 1][colNum] != '.') {
                            break;
                        }
                    }
                    grid[rowNum][colNum] = '.';
                    grid[moveToRow][colNum] = 'O';
                }
            }
        }
    }

    private void tiltWest(char[][] grid) {
        for (int colNum = 0; colNum < grid[0].length; colNum++) {
            for (int rowNum = 0; rowNum < grid.length; rowNum++) {
                if (grid[rowNum][colNum] == 'O') {
                    int moveToCol;
                    for (moveToCol = colNum; moveToCol > 0; moveToCol--) {
                        if (grid[rowNum][moveToCol - 1] != '.') {
                            break;
                        }
                    }
                    grid[rowNum][colNum] = '.';
                    grid[rowNum][moveToCol] = 'O';
                }
            }
        }
    }

    private void tiltEast(char[][] grid) {
        for (int colNum = grid[0].length - 1; colNum >= 0; colNum--) {
            for (int rowNum = 0; rowNum < grid.length; rowNum++) {
                if (grid[rowNum][colNum] == 'O') {
                    int moveToCol;
                    for (moveToCol = colNum; moveToCol < grid[0].length - 1; moveToCol++) {
                        if (grid[rowNum][moveToCol + 1] != '.') {
                            break;
                        }
                    }
                    grid[rowNum][colNum] = '.';
                    grid[rowNum][moveToCol] = 'O';
                }
            }
        }
    }

    private record HashableGrid(char[][] grid) {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof HashableGrid other && Arrays.deepEquals(grid, other.grid);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(grid);
        }
    }
}
