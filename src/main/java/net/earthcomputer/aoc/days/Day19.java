package net.earthcomputer.aoc.days;

import net.earthcomputer.aoc.Day;
import net.earthcomputer.aoc.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Day19 implements Day {
    @Override
    public void part1() {
        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        int total = 0;
        for (Part part : input.parts) {
            String ruleName = "in";
            ruleLoop: while (!"A".equals(ruleName) && !"R".equals(ruleName)) {
                List<Rule> rule = input.rules.get(ruleName);
                if (rule == null) {
                    System.out.println("Could not find rule " + ruleName);
                    return;
                }
                for (Rule term : rule) {
                    if (term.condition == null || term.condition.apply(part)) {
                        ruleName = term.nextRule;
                        continue ruleLoop;
                    }
                }
            }

            if ("A".equals(ruleName)) {
                for (int value : part.variables.values()) {
                    total += value;
                }
            }
        }

        System.out.println(total);
    }

    @Override
    public void part2() {
        record State(String ruleName, int termIndex, Part mins, Part maxs) {
            long count() {
                long product = 1;
                for (Variable variable : Variable.values()) {
                    product *= maxs.get(variable) - mins.get(variable) + 1;
                }
                return product;
            }
        }

        Input input = Input.parse();
        if (input == null) {
            System.out.println("Invalid input");
            return;
        }

        Deque<State> states = new ArrayDeque<>();
        states.addLast(new State("in", 0, Part.all(1), Part.all(4000)));

        long total = 0;

        State state;
        while ((state = states.pollLast()) != null) {
            if ("R".equals(state.ruleName)) {
                continue;
            }
            if ("A".equals(state.ruleName)) {
                total += state.count();
                continue;
            }

            List<Rule> terms = input.rules.get(state.ruleName);
            if (terms == null) {
                System.out.println("Could not find rule " + state.ruleName);
                return;
            }
            if (state.termIndex >= terms.size()) {
                continue;
            }
            Condition condition = terms.get(state.termIndex).condition;
            if (condition != null) {
                int min = state.mins.get(condition.variable);
                int max = state.maxs.get(condition.variable);
                switch (condition.operation) {
                    case LT -> {
                        if (min < condition.value) {
                            Part newMaxs = state.maxs.copy();
                            newMaxs.set(condition.variable, Math.min(max, condition.value - 1));
                            states.addLast(new State(terms.get(state.termIndex).nextRule, 0, state.mins, newMaxs));
                        }
                        if (max >= condition.value) {
                            Part newMins = state.mins.copy();
                            newMins.set(condition.variable, Math.max(min, condition.value));
                            states.addLast(new State(state.ruleName, state.termIndex + 1, newMins, state.maxs));
                        }
                    }
                    case GT -> {
                        if (max > condition.value) {
                            Part newMins = state.mins.copy();
                            newMins.set(condition.variable, Math.max(min, condition.value + 1));
                            states.addLast(new State(terms.get(state.termIndex).nextRule, 0, newMins, state.maxs));
                        }
                        if (min <= condition.value) {
                            Part newMaxs = state.maxs.copy();
                            newMaxs.set(condition.variable, Math.min(max, condition.value));
                            states.addLast(new State(state.ruleName, state.termIndex + 1, state.mins, newMaxs));
                        }
                    }
                }
            } else {
                states.addLast(new State(terms.get(state.termIndex).nextRule, 0, state.mins, state.maxs));
            }
        }

        System.out.println(total);
    }

    private enum Variable {
        X, M, A, S;

        @Nullable
        static Variable fromChar(char c) {
            return switch (c) {
                case 'x' -> X;
                case 'm' -> M;
                case 'a' -> A;
                case 's' -> S;
                default -> null;
            };
        }
    }

    private enum Operation {
        LT, GT;

        @Nullable
        static Operation fromChar(char c) {
            return switch (c) {
                case '<' -> LT;
                case '>' -> GT;
                default -> null;
            };
        }
    }

    private record Condition(Variable variable, Operation operation, int value) {
        @Nullable
        static Condition parse(String str) {
            if (str.length() < 3) {
                return null;
            }
            Variable variable = Variable.fromChar(str.charAt(0));
            if (variable == null) {
                return null;
            }
            Operation operation = Operation.fromChar(str.charAt(1));
            if (operation == null) {
                return null;
            }
            Integer value = Util.parseIntOrNull(str.substring(2));
            if (value == null) {
                return null;
            }

            return new Condition(variable, operation, value);
        }

        boolean apply(Part part) {
            int partValue = part.get(variable);
            return switch (operation) {
                case LT -> partValue < value;
                case GT -> partValue > value;
            };
        }
    }

    private record Rule(@Nullable Condition condition, String nextRule) {
        @Nullable
        static Rule parse(String str) {
            int colonIndex = str.indexOf(':');
            if (colonIndex >= 0) {
                Condition condition = Condition.parse(str.substring(0, colonIndex));
                if (condition == null) {
                    return null;
                }
                return new Rule(condition, str.substring(colonIndex + 1));
            } else {
                return new Rule(null, str);
            }
        }
    }

    private record Part(EnumMap<Variable, Integer> variables) {
        static Part all(int value) {
            Part part = new Part(new EnumMap<>(Variable.class));
            for (Variable variable : Variable.values()) {
                part.set(variable, value);
            }
            return part;
        }

        int get(Variable variable) {
            return variables.get(variable);
        }

        void set(Variable variable, int value) {
            variables.put(variable, value);
        }

        Part copy() {
            Part part = new Part(new EnumMap<>(Variable.class));
            part.variables.putAll(variables);
            return part;
        }

        @Nullable
        static Part parse(String str) {
            if (!str.startsWith("{") || !str.endsWith("}")) {
                return null;
            }
            Part part = Part.all(0);
            for (String assignment : str.substring(1, str.length() - 1).split(",")) {
                String[] kv = assignment.split("=");
                if (kv.length != 2) {
                    return null;
                }
                if (kv[0].length() != 1) {
                    return null;
                }
                Variable variable = Variable.fromChar(kv[0].charAt(0));
                if (variable == null) {
                    return null;
                }
                Integer value = Util.parseIntOrNull(kv[1]);
                if (value == null) {
                    return null;
                }
                part.set(variable, value);
            }
            return part;
        }
    }

    private record Input(Map<String, List<Rule>> rules, List<Part> parts) {
        @Nullable
        static Input parse() {
            Map<String, List<Rule>> rules = new HashMap<>();
            List<Part> parts = new ArrayList<>();

            Iterator<String> linesItr = Util.readInput().iterator();
            while (linesItr.hasNext()) {
                String line = linesItr.next();
                if (line.isEmpty()) {
                    break;
                }

                int braceIndex = line.indexOf('{');
                if (braceIndex == -1) {
                    return null;
                }
                if (!line.endsWith("}")) {
                    return null;
                }
                String ruleName = line.substring(0, braceIndex);

                List<Rule> terms = new ArrayList<>();
                for (String termStr : line.substring(braceIndex + 1, line.length() - 1).split(",")) {
                    Rule term = Rule.parse(termStr);
                    if (term == null) {
                        return null;
                    }
                    terms.add(term);
                }

                rules.put(ruleName, terms);
            }

            while (linesItr.hasNext()) {
                String line = linesItr.next();
                if (line.isEmpty()) {
                    continue;
                }
                Part part = Part.parse(line);
                if (part == null) {
                    return null;
                }
                parts.add(part);
            }

            return new Input(rules, parts);
        }
    }
}
