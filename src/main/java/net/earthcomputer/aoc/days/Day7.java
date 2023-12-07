package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day7 implements Day {
    @Override
    public void part1() {
        run(false);
    }

    @Override
    public void part2() {
        run(true);
    }

    private void run(boolean part2) {
        List<Hand> input = parseInput();
        input.sort((hand1, hand2) -> hand1.compareTo(hand2, part2));

        int total = 0;
        for (int i = 0; i < input.size(); i++) {
            total += input.get(i).bid * (i + 1);
        }

        System.out.println(total);
    }

    private List<Hand> parseInput() {
        return Util.readInput().stream().map(Hand::parse).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
    }

    @Nullable
    private static Integer charIndex(char c) {
        int index = "23456789TJQKA".indexOf(c);
        return index == -1 ? null : index;
    }

    private enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND,
    }

    private record Hand(String hand, int bid) {
        @Nullable
        static Hand parse(String line) {
            if (line.length() < 7) {
                return null;
            }
            if (line.charAt(5) != ' ') {
                return null;
            }
            Integer bid = Util.parseIntOrNull(line.substring(6));
            if (bid == null) {
                return null;
            }
            return new Hand(line.substring(0, 5), bid);
        }

        int compareTo(Hand other, boolean part2) {
            HandType thisHandType = this.getHandType(part2);
            HandType otherHandType = other.getHandType(part2);
            if (thisHandType != otherHandType) {
                return thisHandType.compareTo(otherHandType);
            }

            for (int i = 0; i < 5; i++) {
                if (this.hand.charAt(i) != other.hand.charAt(i)) {
                    if (part2) {
                        if (this.hand.charAt(i) == 'J') {
                            return -1;
                        }
                        if (other.hand.charAt(i) == 'J') {
                            return 1;
                        }
                    }

                    Integer thisCharIndex = charIndex(this.hand.charAt(i));
                    if (thisCharIndex == null) {
                        thisCharIndex = 13;
                    }
                    Integer otherCharIndex = charIndex(other.hand.charAt(i));
                    if (otherCharIndex == null) {
                        otherCharIndex = 13;
                    }
                    return thisCharIndex.compareTo(otherCharIndex);
                }
            }

            return 0;
        }

        private HandType getHandType(boolean part2) {
            int[] counts = new int[13];
            for (char c : hand.toCharArray()) {
                Integer index = charIndex(c);
                if (index != null) {
                    counts[index]++;
                }
            }

            if (part2) {
                int jokerCount = counts[9];
                counts[9] = 0;
                if (jokerCount > 0) {
                    int maxIndex = 0;
                    for (int i = 1; i < counts.length; i++) {
                        if (counts[i] > counts[maxIndex]) {
                            maxIndex = i;
                        }
                    }
                    counts[maxIndex] += jokerCount;
                }
            }

            int[] countCounts = new int[6];
            for (int count : counts) {
                countCounts[count]++;
            }

            if (countCounts[5] == 1) {
                return HandType.FIVE_OF_A_KIND;
            }
            if (countCounts[4] == 1) {
                return HandType.FOUR_OF_A_KIND;
            }
            if (countCounts[3] == 1 && countCounts[2] == 1) {
                return HandType.FULL_HOUSE;
            }
            if (countCounts[3] == 1) {
                return HandType.THREE_OF_A_KIND;
            }
            if (countCounts[2] == 2) {
                return HandType.TWO_PAIRS;
            }
            if (countCounts[2] == 1) {
                return HandType.ONE_PAIR;
            }
            return HandType.HIGH_CARD;
        }
    }
}
