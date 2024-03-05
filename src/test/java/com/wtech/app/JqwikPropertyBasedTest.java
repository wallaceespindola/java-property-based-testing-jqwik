package com.wtech.app;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.GenerationMode;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.ShrinkingMode;
import net.jqwik.api.constraints.Positive;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JqwikPropertyBasedTest {

    @Test
    public void testWithAssertJ() {
        String test = "test";
        assertThat(test).isNotNull();
        assertThat(test).isNotBlank();
        assertThat(test).isEqualTo("test");
    }

    @Test
    public void testWithJunit5() {
        String test = "test";
        assertNotNull(test, "String value should not be null");
        assertFalse(test.isEmpty(), "String value should not be empty");
        assertEquals("test", test, "Strings must be equal");
    }

    @Property
    public void divideBySelf(@ForAll int value) {
        int result = App.divide(value, value);
        assertEquals(result, 1);
    }

    @Property
    public void additionIsCommutative(@ForAll int a, @ForAll int b) {
        assertEquals(a + b, b + a);
    }

    @Property
    public void dividePositiveBySelf(@ForAll @Positive int value) {
        int result = App.divide(value, value);
        assertEquals(result, 1);
    }

    @Property
    public void divideNonZeroBySelf(@ForAll("nonZeroNumbers") int value) {
        int result = App.divide(value, value);
        assertEquals(result, 1);
    }

    @Provide
    Arbitrary<Integer> nonZeroNumbers() {
        return Arbitraries.integers().filter(v -> v != 0);
    }

    @Property
    public void divideLargeBySmall(@ForAll @Positive int a, @ForAll @Positive int b) { // tries > checks
        Assume.that(a > b);
        int result = App.divide(a, b);
        assertTrue(result >= 1);
    }

    /**
     * Test expected to fail for result Shrinking:
     * jqwik can very efficiently find cases where our code fails the test.
     * However, this isn’t always useful if the generated case is quite obscure.
     * Instead, jqwik will attempt to “shrink” any failing cases down to the minimal case.
     * This will help us more directly find any edge cases that we need to account for.
     * Let’s try a seemingly simple case. Squaring a number should produce a result that’s larger than the input.
     */
    @Property
    public void square(@ForAll @Positive int a) {
        int result = a * a;
        assertTrue(result >= a);
        /*
         * But why does this fail? There’s nothing unusual about it.
         * So let’s try it ourselves.
         * 46,341 (a random number) squared is 2,147,488,281, which happens to be larger than an integer can hold.
         * However, 46,340 squared fits in the range.
         * So we have indeed found a surprising edge here – we can’t square 46,341 and store the result in an integer.
         */

        // Attention: Re-Running Tests
        // If our test fails, we will typically want to fix the bugs and then re-run the tests.
        // However, if the generated test cases are random, then re-running the test might not hit the
        // same failing condition that it did before.
    }

    @Property(tries = 5000, shrinking = ShrinkingMode.OFF, generation = GenerationMode.RANDOMIZED)
    public void testConfigurableProperty(@ForAll @Positive int a) { // tries > checks
        int result = App.divide(a, a);
        assertEquals(1, result);
    }
}