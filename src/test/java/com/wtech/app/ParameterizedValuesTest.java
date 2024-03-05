package com.wtech.app;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterizedValuesTest {

    @ParameterizedTest
    @CsvSource({"4,2,6", "6,2,8", "6,3,9"})
    void testAddPositives(int x, int y, int expected) {
        int answer = App.add(x, y);
        assertEquals(expected, answer);
    }

    @ParameterizedTest
    @CsvSource({"-4,2,-2", "-6,-2,-8", "6,-4,2"})
    void testAddNegatives(int x, int y, int expected) {
        int answer = App.add(x, y);
        assertEquals(expected, answer);
    }

    @ParameterizedTest
    @MethodSource("providePositiveIntegerInputs")
    void testDivisionBySelfPositive(int input) {
        int answer = App.divide(input, input);
        assertEquals(answer, 1);
    }

    @ParameterizedTest
    @MethodSource("provideNegativeIntegerInputs")
    void testDivisionBySelfNegative(int input) {
        int answer = App.divide(input, input);
        assertEquals(answer, 1);
    }

    private static Stream<Arguments> provideNegativeIntegerInputs() {
        return IntStream.rangeClosed(-20, -1).mapToObj(Arguments::of);
    }

    private static Stream<Arguments> providePositiveIntegerInputs() {
        return IntStream.rangeClosed(+1, +20).mapToObj(Arguments::of);
    }

    private static Stream<Arguments> provideIntegerInputs() { // A LONG RUNNING POSSIBILITY
        return IntStream
                .rangeClosed(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .mapToObj(Arguments::of);
    }
}