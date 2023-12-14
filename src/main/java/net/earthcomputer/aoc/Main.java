package net.earthcomputer.aoc;

import net.earthcomputer.aoc.days.Day1;
import net.earthcomputer.aoc.days.Day10;
import net.earthcomputer.aoc.days.Day11;
import net.earthcomputer.aoc.days.Day12;
import net.earthcomputer.aoc.days.Day13;
import net.earthcomputer.aoc.days.Day14;
import net.earthcomputer.aoc.days.Day2;
import net.earthcomputer.aoc.days.Day3;
import net.earthcomputer.aoc.days.Day4;
import net.earthcomputer.aoc.days.Day5;
import net.earthcomputer.aoc.days.Day6;
import net.earthcomputer.aoc.days.Day7;
import net.earthcomputer.aoc.days.Day8;
import net.earthcomputer.aoc.days.Day9;
import org.jetbrains.annotations.Nullable;

import java.util.Scanner;

public class Main {
    private static final Day[] DAYS = {
        new Day1(),
        new Day2(),
        new Day3(),
        new Day4(),
        new Day5(),
        new Day6(),
        new Day7(),
        new Day8(),
        new Day9(),
        new Day10(),
        new Day11(),
        new Day12(),
        new Day13(),
        new Day14(),
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer day = askForNumber(scanner, "Enter the day: ");
        if (day == null) {
            return;
        }
        if (day == 0 || day > DAYS.length) {
            System.out.println(day + " is not between 1-" + DAYS.length);
            return;
        }

        Integer part = askForNumber(scanner, "Enter the part: ");
        if (part == null) {
            return;
        }
        switch (part) {
            case 1 -> DAYS[day - 1].part1();
            case 2 -> DAYS[day - 1].part2();
            default -> System.out.println(part + " is not 1 or 2");
        }
    }

    @Nullable
    private static Integer askForNumber(Scanner scanner, String prompt) {
        System.out.print(prompt);

        Integer result = Util.parseIntOrNull(scanner.nextLine());
        if (result == null) {
            System.out.println("Invalid number");
        }
        return result;
    }
}