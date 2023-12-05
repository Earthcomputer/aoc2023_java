package net.earthcomputer.aoc;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Util {
    private Util() {
    }

    @Nullable
    public static Integer parseIntOrNull(String input) {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static Long parseLongOrNull(String input) {
        try {
            return Long.valueOf(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static List<String> readInput() {
        try {
            return Files.readAllLines(Path.of("input.txt"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
