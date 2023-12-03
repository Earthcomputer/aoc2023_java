package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Day3 implements Day {
    @Override
    public void part1() {
        List<String> lines = Util.readInput();

        int total = 0;

        for (int lineNo = 0; lineNo < lines.size(); lineNo++) {
            String line = lines.get(lineNo);

            int column = 0;
            while (column < line.length()) {
                // skip over non-numbers
                if (!Character.isDigit(line.charAt(column))) {
                    column++;
                    continue;
                }

                // find the end of the number
                int startColumn = column;
                while (column < line.length() && Character.isDigit(line.charAt(column))) {
                    column++;
                }

                if (hasAdjacentSymbol(lines, lineNo, startColumn, column)) {
                    Integer n = Util.parseIntOrNull(line.substring(startColumn, column));
                    if (n != null) {
                        total += n;
                    }
                }
            }
        }

        System.out.println(total);
    }

    private boolean hasAdjacentSymbol(List<String> lines, int lineNo, int startColumn, int endColumn) {
        // check above
        if (lineNo != 0) {
            String lineAbove = lines.get(lineNo - 1);
            for (int col = startColumn == 0 ? 0 : startColumn - 1; col < Math.min(endColumn + 1, lineAbove.length()); col++) {
                if (isSymbol(lineAbove.charAt(col))) {
                    return true;
                }
            }
        }

        // check below
        if (lineNo != lines.size() - 1) {
            String lineBelow = lines.get(lineNo + 1);
            for (int col = startColumn == 0 ? 0 : startColumn - 1; col < Math.min(endColumn + 1, lineBelow.length()); col++) {
                if (isSymbol(lineBelow.charAt(col))) {
                    return true;
                }
            }
        }

        // check left
        if (startColumn != 0) {
            if (isSymbol(lines.get(lineNo).charAt(startColumn - 1))) {
                return true;
            }
        }

        // check right
        if (endColumn != lines.get(lineNo).length()) {
            if (isSymbol(lines.get(lineNo).charAt(endColumn))) {
                return true;
            }
        }

        return false;
    }

    private boolean isSymbol(char c) {
        return !Character.isDigit(c) && c != '.';
    }

    @Override
    public void part2() {
        List<String> lines = Util.readInput();

        int total = 0;

        for (int lineNo = 0; lineNo < lines.size(); lineNo++) {
            String line = lines.get(lineNo);

            for (int column = 0; column < line.length(); column++) {
                if (line.charAt(column) == '*') {
                    Integer ratio = getGearRatio(lines, lineNo, column);
                    if (ratio != null) {
                        total += ratio;
                    }
                }
            }
        }

        System.out.println(total);
    }

    @Nullable
    private Integer getGearRatio(List<String> lines, int lineNo, int column) {
        int count = 0;
        int product = 1;

        // check above
        if (lineNo != 0) {
            String lineAbove = lines.get(lineNo - 1);
            Integer n = getNumberAt(lineAbove, column);
            if (n != null) {
                // if there is a number in the middle above, then that is the only number above
                count++;
                product *= n;
            } else {
                // if there is no number in the middle above, then any diagonal numbers above cannot be connected
                n = getNumberAt(lineAbove, column - 1);
                if (n != null) {
                    count++;
                    product *= n;
                }

                n = getNumberAt(lineAbove, column + 1);
                if (n != null) {
                    count++;
                    product *= n;
                }
            }
        }

        // check below
        if (lineNo != lines.size() - 1) {
            String lineBelow = lines.get(lineNo + 1);
            Integer n = getNumberAt(lineBelow, column);
            if (n != null) {
                // if there is a number in the middle below, then that is the only number below
                count++;
                product *= n;
            } else {
                // if there is no number in the middle below, then any diagonal numbers below cannot be connected
                n = getNumberAt(lineBelow, column - 1);
                if (n != null) {
                    count++;
                    product *= n;
                }

                n = getNumberAt(lineBelow, column + 1);
                if (n != null) {
                    count++;
                    product *= n;
                }
            }
        }

        // check left
        Integer n = getNumberAt(lines.get(lineNo), column - 1);
        if (n != null) {
            count++;
            product *= n;
        }

        // check right
        n = getNumberAt(lines.get(lineNo), column + 1);
        if (n != null) {
            count++;
            product *= n;
        }

        return count == 2 ? product : null;
    }

    @Nullable
    private Integer getNumberAt(String str, int index) {
        if (index < 0 || index >= str.length() || !Character.isDigit(str.charAt(index))) {
            return null;
        }

        int startIndex = index;
        while (startIndex > 0 && Character.isDigit(str.charAt(startIndex - 1))) {
            startIndex--;
        }
        int endIndex = index;
        while (endIndex < str.length() && Character.isDigit(str.charAt(endIndex))) {
            endIndex++;
        }

        return Util.parseIntOrNull(str.substring(startIndex, endIndex));
    }
}
