package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Main;

public class Day1 implements Day {
    private static final String[] NUMBERS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

    @Override
    public void part1() {
        run(false);
    }

    @Override
    public void part2() {
        run(true);
    }

    private void run(boolean part2) {
        int total = 0;
        for (String line : Main.readInput()) {
            // look for first number
            firstNum: for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c >= '0' && c <= '9') {
                    total += 10 * (c - '0');
                    break;
                }
                if (part2) {
                    for (int n = 1; n <= 9; n++) {
                        if (line.startsWith(NUMBERS[n - 1], i)) {
                            total += 10 * n;
                            break firstNum;
                        }
                    }
                }
            }

            // look for last number
            lastNum: for (int i = line.length() - 1; i >= 0; i--) {
                char c = line.charAt(i);
                if (c >= '0' && c <= '9') {
                    total += c - '0';
                    break;
                }
                if (part2) {
                    for (int n = 1; n <= 9; n++) {
                        if (line.startsWith(NUMBERS[n - 1], i)) {
                            total += n;
                            break lastNum;
                        }
                    }
                }
            }
        }
        System.out.println(total);
    }
}
