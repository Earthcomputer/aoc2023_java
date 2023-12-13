package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Day13 implements Day {
    @Override
    public void part1() {
        run(0);
    }

    @Override
    public void part2() {
        run(1);
    }

    private void run(int expectedErrorCount) {
        List<List<String>> grids = readInput();
        if (grids == null) {
            System.out.println("Invalid input");
            return;
        }

        int total = 0;
        for (List<String> grid : grids) {
            for (int column = 1; column < grid.get(0).length(); column++) {
                int errorCount = 0;
                for (int dColumn = 0; dColumn < column && column + dColumn < grid.get(0).length(); dColumn++) {
                    for (int row = 0; row < grid.size(); row++) {
                        if (grid.get(row).charAt(column - dColumn - 1) != grid.get(row).charAt(column + dColumn)) {
                            errorCount++;
                        }
                    }
                }
                if (errorCount == expectedErrorCount) {
                    total += column;
                }
            }

            for (int row = 0; row < grid.size(); row++) {
                int errorCount = 0;
                for (int dRow = 0; dRow < row && row + dRow < grid.size(); dRow++) {
                    for (int column = 0; column < grid.get(0).length(); column++) {
                        if (grid.get(row - dRow - 1).charAt(column) != grid.get(row + dRow).charAt(column)) {
                            errorCount++;
                        }
                    }
                }
                if (errorCount == expectedErrorCount) {
                    total += 100 * row;
                }
            }
        }

        System.out.println(total);
    }

    @Nullable
    private List<List<String>> readInput() {
        List<List<String>> grids = new ArrayList<>();
        List<String> grid = new ArrayList<>();
        for (String line : Util.readInput()) {
            if (line.isEmpty()) {
                if (!grid.isEmpty() && !grid.get(0).isEmpty()) {
                    var grid_f = grid;
                    if (grid.stream().anyMatch(l -> l.length() != grid_f.get(0).length())) {
                        return null;
                    }
                    grids.add(grid);
                }
                grid = new ArrayList<>();
            } else {
                grid.add(line);
            }
        }
        if (!grid.isEmpty() && !grid.get(0).isEmpty()) {
            var grid_f = grid;
            if (grid.stream().anyMatch(l -> l.length() != grid_f.get(0).length())) {
                return null;
            }
            grids.add(grid);
        }
        return grids;
    }
}
