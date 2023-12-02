package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 implements Day {
    private record Round(int red, int green, int blue) {}

    @Override
    public void part1() {
        var games = parseGames();

        int[] total = {0};
        games.forEach((gameNum, rounds) -> {
            boolean possible = true;
            for (Round round : rounds) {
                if (round.red > 12 || round.green > 13 || round.blue > 14) {
                    possible = false;
                    break;
                }
            }

            if (possible) {
                total[0] += gameNum;
            }
        });

        System.out.println(total[0]);
    }

    @Override
    public void part2() {
        var games = parseGames();

        int total = 0;
        for (List<Round> rounds : games.values()) {
            int maxRed = 0, maxGreen = 0, maxBlue = 0;
            for (Round round : rounds) {
                maxRed = Math.max(maxRed, round.red);
                maxGreen = Math.max(maxGreen, round.green);
                maxBlue = Math.max(maxBlue, round.blue);
            }
            total += maxRed * maxGreen * maxBlue;
        }

        System.out.println(total);
    }

    private Map<Integer, List<Round>> parseGames() {
        Map<Integer, List<Round>> games = new HashMap<>();

        for (String line : Util.readInput()) {
            String[] parts = line.split(": ", 2);
            if (parts.length != 2) {
                continue;
            }

            if (!parts[0].startsWith("Game ")) {
                continue;
            }
            Integer gameNum = Util.parseIntOrNull(parts[0].substring("Game ".length()));
            if (gameNum == null) {
                continue;
            }

            List<Round> rounds = new ArrayList<>();
            for (String round : parts[1].split("; ")) {
                int red = 0, green = 0, blue = 0;
                for (String roundPart : round.split(", ")) {
                    String[] roundPartSplit = roundPart.split(" ", 2);
                    if (roundPartSplit.length != 2) {
                        continue;
                    }

                    Integer count = Util.parseIntOrNull(roundPartSplit[0]);
                    if (count == null) {
                        continue;
                    }

                    switch (roundPartSplit[1]) {
                        case "red" -> red += count;
                        case "green" -> green += count;
                        case "blue" -> blue += count;
                    }
                }

                rounds.add(new Round(red, green, blue));
            }

            games.put(gameNum, rounds);
        }

        return games;
    }
}
