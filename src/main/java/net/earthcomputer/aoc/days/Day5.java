package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Day5 implements Day {
    @Override
    public void part1() {
        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        long[] values = input.seeds.stream().mapToLong(Long::longValue).toArray();

        applyMap(values, input.seedToSoil);
        applyMap(values, input.soilToFertilizer);
        applyMap(values, input.fertilizerToWater);
        applyMap(values, input.waterToLight);
        applyMap(values, input.lightToTemperature);
        applyMap(values, input.temperatureToHumidity);
        applyMap(values, input.humidityToLocation);

        long minLocation = Arrays.stream(values).min().orElse(0);
        System.out.println(minLocation);
    }

    private void applyMap(long[] values, List<RangeMap> map) {
        for (int i = 0; i < values.length; i++) {
            for (RangeMap range : map) {
                Long newValue = range.map(values[i]);
                if (newValue != null) {
                    values[i] = newValue;
                    break;
                }
            }
        }
    }

    @Override
    public void part2() {
        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        List<Range> ranges = new ArrayList<>(input.seeds.size() / 2);
        for (int i = 0; i + 1 < input.seeds.size(); i += 2) {
            ranges.add(new Range(input.seeds.get(i), input.seeds.get(i) + input.seeds.get(i + 1)));
        }

        ranges = applyMapOnRanges(ranges, input.seedToSoil);
        ranges = applyMapOnRanges(ranges, input.soilToFertilizer);
        ranges = applyMapOnRanges(ranges, input.fertilizerToWater);
        ranges = applyMapOnRanges(ranges, input.waterToLight);
        ranges = applyMapOnRanges(ranges, input.lightToTemperature);
        ranges = applyMapOnRanges(ranges, input.temperatureToHumidity);
        ranges = applyMapOnRanges(ranges, input.humidityToLocation);

        long min = ranges.stream().mapToLong(Range::start).min().orElse(0);
        System.out.println(min);
    }

    private List<Range> applyMapOnRanges(List<Range> ranges, List<RangeMap> map) {
        List<Range> result = new ArrayList<>();

        for (RangeMap rangeMap : map) {
            List<Range> leftoverRanges = new ArrayList<>();
            for (Range range : ranges) {
                var mapped = rangeMap.mapRange(range);
                if (mapped.mappedRange != null) {
                    result.add(mapped.mappedRange);
                }
                Collections.addAll(leftoverRanges, mapped.leftoverRanges);
            }
            ranges = leftoverRanges;
        }

        result.addAll(ranges);
        return result;
    }

    private record Input(
        List<Long> seeds,
        List<RangeMap> seedToSoil,
        List<RangeMap> soilToFertilizer,
        List<RangeMap> fertilizerToWater,
        List<RangeMap> waterToLight,
        List<RangeMap> lightToTemperature,
        List<RangeMap> temperatureToHumidity,
        List<RangeMap> humidityToLocation
    ) {
        @Nullable
        static Input parse() {
            Iterator<String> lines = Util.readInput().iterator();

            if (!lines.hasNext()) {
                return null;
            }
            String seedsLine = lines.next();
            if (!seedsLine.startsWith("seeds: ")) {
                return null;
            }
            List<Long> seeds = new ArrayList<>();
            for (String seedStr : seedsLine.substring("seeds: ".length()).split(" ")) {
                Long seed = Util.parseLongOrNull(seedStr);
                if (seed == null) {
                    return null;
                }
                seeds.add(seed);
            }

            // skip empty line
            if (!lines.hasNext()) {
                return null;
            }
            lines.next();

            List<RangeMap> seedToSoil = RangeMap.parseRangeMapList("seed-to-soil map:", lines);
            List<RangeMap> soilToFertilizer = RangeMap.parseRangeMapList("soil-to-fertilizer map:", lines);
            List<RangeMap> fertilizerToWater = RangeMap.parseRangeMapList("fertilizer-to-water map:", lines);
            List<RangeMap> waterToLight = RangeMap.parseRangeMapList("water-to-light map:", lines);
            List<RangeMap> lightToTemperature = RangeMap.parseRangeMapList("light-to-temperature map:", lines);
            List<RangeMap> temperatureToHumidity = RangeMap.parseRangeMapList("temperature-to-humidity map:", lines);
            List<RangeMap> humidityToLocation = RangeMap.parseRangeMapList("humidity-to-location map:", lines);
            if (seedToSoil == null || soilToFertilizer == null || fertilizerToWater == null || waterToLight == null || lightToTemperature == null || temperatureToHumidity == null || humidityToLocation == null) {
                return null;
            }

            return new Input(seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation);
        }
    }

    private record Range(long start, long end) {}

    private record RangeMap(long target, long sourceStart, long sourceLen) {
        @Nullable
        static RangeMap parse(String line) {
            String[] parts = line.split(" ");
            if (parts.length < 3) {
                return null;
            }

            Long target = Util.parseLongOrNull(parts[0]);
            Long sourceStart = Util.parseLongOrNull(parts[1]);
            Long sourceLen = Util.parseLongOrNull(parts[2]);
            if (target == null || sourceStart == null || sourceLen == null) {
                return null;
            }

            return new RangeMap(target, sourceStart, sourceLen);
        }

        @Nullable
        static List<RangeMap> parseRangeMapList(String header, Iterator<String> lines) {
            if (!lines.hasNext() || !header.equals(lines.next())) {
                return null;
            }

            List<RangeMap> rangeList = new ArrayList<>();
            while (lines.hasNext()) {
                String line = lines.next();
                if (line.isEmpty()) {
                    break;
                }

                RangeMap range = parse(line);
                if (range != null) {
                    rangeList.add(range);
                }
            }
            return rangeList;
        }

        @Nullable
        Long map(long n) {
            if (n >= sourceStart && n < sourceStart + sourceLen) {
                return n - sourceStart + target;
            } else {
                return null;
            }
        }

        private record MapRangeResult(@Nullable Range mappedRange, Range... leftoverRanges) {}

        MapRangeResult mapRange(Range range) {
            long sourceEnd = sourceStart + sourceLen;

            // case 1: none of the range gets mapped
            // map:   |----|
            // range:        |----|
            if (sourceEnd <= range.start || sourceStart >= range.end) {
                return new MapRangeResult(null, range);
            }

            // case 2: entire range gets mapped
            // map:   |--------|
            // range:   |****|
            if (sourceStart <= range.start && sourceEnd >= range.end) {
                return new MapRangeResult(new Range(range.start - sourceStart + target, range.end - sourceStart + target));
            }

            // case 3: some of the range gets mapped with leftover on both sides
            // map:     |----|
            // range: |--****--|
            if (sourceStart > range.start && sourceEnd < range.end) {
                return new MapRangeResult(
                    new Range(target, target + sourceLen),
                    new Range(range.start, sourceStart),
                    new Range(sourceEnd, range.end)
                );
            }

            // case 4: some of the range gets mapped with leftover on the left
            // map:      |------|
            // range: |---****|
            if (sourceStart > range.start) {
                return new MapRangeResult(
                    new Range(target, range.end - sourceStart + target),
                    new Range(range.start, sourceStart)
                );
            }

            // case 5: some of the range gets mapped with leftover on the right
            // map:   |------|
            // range:    |***---|
            return new MapRangeResult(
                new Range(range.start - sourceStart + target, target + sourceLen),
                new Range(sourceEnd, range.end)
            );
        }
    }
}
