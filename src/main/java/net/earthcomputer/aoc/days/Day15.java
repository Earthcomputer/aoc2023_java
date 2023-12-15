package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15 implements Day {
    @Override
    public void part1() {
        System.out.println(Arrays.stream(Util.readInput().get(0).split(",")).mapToInt(this::hash).sum());
    }

    @Override
    public void part2() {
        @SuppressWarnings("unchecked")
        List<Entry>[] hashmap = new List[256];
        Arrays.setAll(hashmap, i -> new ArrayList<>());

        for (String part : Util.readInput().get(0).split(",")) {
            if (part.endsWith("-")) {
                String label = part.substring(0, part.length() - 1);
                int h = hash(label);
                for (int i = 0; i < hashmap[h].size(); i++) {
                    if (hashmap[h].get(i).key.equals(label)) {
                        hashmap[h].remove(i);
                        break;
                    }
                }
            } else {
                String[] entryParts = part.split("=");
                if (entryParts.length != 2) {
                    System.out.println("Invalid input");
                    return;
                }
                String label = entryParts[0];
                Integer focalLength = Util.parseIntOrNull(entryParts[1]);
                if (focalLength == null) {
                    System.out.println("Invalid input");
                    return;
                }

                int h = hash(label);
                boolean found = false;
                for (Entry entry : hashmap[h]) {
                    if (entry.key.equals(label)) {
                        entry.value = focalLength;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    hashmap[h].add(new Entry(label, focalLength));
                }
            }
        }

        int total = 0;
        for (int boxNum = 0; boxNum < hashmap.length; boxNum++) {
            for (int i = 0; i < hashmap[boxNum].size(); i++) {
                total += (boxNum + 1) * (i + 1) * hashmap[boxNum].get(i).value;
            }
        }

        System.out.println(total);
    }

    private int hash(String str) {
        int h = 0;
        for (int i = 0; i < str.length(); i++) {
            h += str.charAt(i);
            h *= 17;
        }
        return h & 255;
    }

    private static final class Entry {
        String key;
        int value;

        Entry(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
