package com.wtech.app;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.GenerationMode;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Report;
import net.jqwik.api.Reporting;
import net.jqwik.api.ShrinkingMode;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.constraints.LowerChars;
import net.jqwik.api.constraints.Negative;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.constraints.UpperChars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test using jqwik library, wide number of possibilities are tested.
 * <p>
 * Possibilities on Arbitraries facade:
 * <p>
 * <@Nullable T> Arbitrary<T> just(@Nullable T var1);
 * <K, V> Arbitrary<Map.Entry<K, V>> entries(Arbitrary<K> var1, Arbitrary<V> var2);
 * <K, V> MapArbitrary<K, V> maps(Arbitrary<K> var1, Arbitrary<V> var2);
 * <M> ActionSequenceArbitrary<M> sequences(Arbitrary<? extends Action<M>> var1);
 * <T> Arbitrary<List<T>> shuffle(List<T> var1);
 * <T> Arbitrary<T> create(Supplier<T> var1);
 * <T> Arbitrary<T> defaultFor(Class<T> var1, Class<?>[] var2);
 * <T> Arbitrary<T> defaultFor(TypeUsage var1, Function<TypeUsage, Arbitrary<Object>> var2);
 * <T> Arbitrary<T> frequency(List<Tuple.Tuple2<Integer, T>> var1);
 * <T> Arbitrary<T> frequencyOf(List<Tuple.Tuple2<Integer, Arbitrary<T>>> var1);
 * <T> Arbitrary<T> fromGenerator(IntFunction<RandomGenerator<T>> var1);
 * <T> Arbitrary<T> lazy(Supplier<Arbitrary<T>> var1);
 * <T> Arbitrary<T> lazyOf(List<Supplier<Arbitrary<T>>> var1);
 * <T> Arbitrary<T> of(Collection<T> var1);
 * <T> Arbitrary<T> oneOf(Collection<Arbitrary<? extends T>> var1);
 * <T> Arbitrary<T> recursive(Supplier<Arbitrary<T>> var1, Function<Arbitrary<T>, Arbitrary<T>> var2, int var3, int var4);
 * <T> TraverseArbitrary<T> traverse(Class<T> var1, TraverseArbitrary.Traverser var2);
 * <T> TypeArbitrary<T> forType(Class<T> var1);
 * Arbitrary<Character> of(char[] var1);
 * BigDecimalArbitrary bigDecimals();
 * BigIntegerArbitrary bigIntegers();
 * ByteArbitrary bytes();
 * CharacterArbitrary chars();
 * DoubleArbitrary doubles();
 * FloatArbitrary floats();
 * IntegerArbitrary integers();
 * LongArbitrary longs();
 * ShortArbitrary shorts();
 * StringArbitrary strings();
 */
public class JqwikPropertyBasedTest {

    @Property
    @Report(Reporting.GENERATED)
    public void doubleReverseRetainsOriginalString(@ForAll String original) {
        String reversed = new StringBuilder(original).reverse().toString();
        String doubleReversed = new StringBuilder(reversed).reverse().toString();
        assertEquals(original, doubleReversed);
    }

    @Property
    public void pushPopInvariance(@ForAll Integer element) {
        Stack<Integer> stack = new Stack<>();
        int initialSize = stack.size();
        stack.push(element);
        boolean sizeIncreased = stack.size() == initialSize + 1;
        boolean elementIsSame = element.equals(stack.pop());
        assertTrue(sizeIncreased && elementIsSame);
    }

    @Property
    @Report(Reporting.GENERATED)
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
    @Report(Reporting.GENERATED)
    public void divideNegativeBySelf(@ForAll @Negative int value) {
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
        return Arbitraries.integers().filter(val -> val != 0);
    }

    @Property
    public void divideLargeBySmall(@ForAll @Positive int a, @ForAll @Positive int b) { // tries > checks
        Assume.that(a > b);
        int result = App.divide(a, b);
        assertTrue(result >= 1);
    }

    @Property
    @Report(Reporting.GENERATED)
    public void sortedListShouldHaveSameElementsAndBeInOrder(@ForAll List<Integer> original) {
        List<Integer> sorted = new ArrayList<>(original);
        Collections.sort(sorted);
        assertTrue(containsSameElements(original, sorted) && isSorted(sorted));
    }

    private boolean containsSameElements(List<Integer> original, List<Integer> sorted) {
        return original.stream().sorted().collect(Collectors.toList()).equals(sorted);
    }

    private boolean isSorted(List<Integer> sorted) {
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i) > sorted.get(i + 1)) {
                return false;
            }
        }
        return true;
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
        //assertTrue(result >= a); // TODO DEMO: for failing and going to the shrinking process
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

    @Property(tries = 500, shrinking = ShrinkingMode.OFF, generation = GenerationMode.RANDOMIZED)
    @Report(Reporting.GENERATED)
    public void testConfigurableProperty(@ForAll @Positive int a) { // tries > checks
        int result = App.divide(a, a);
        assertEquals(1, result);
    }

    @Property
    public void customDataStructureProperty(@ForAll("complexStructureGenerator") ComplexDataStructure data) {
        // Verify that the ComplexDataStructure instance is always valid
        assertTrue(data.isValid());
    }

    @Provide
    Arbitrary<ComplexDataStructure> complexStructureGenerator() {
        IntegerArbitrary numbers = Arbitraries.integers().between(1, 100);
        return numbers.map(ComplexDataStructure::new);
    }

    static class ComplexDataStructure {
        private final int number;

        ComplexDataStructure(int number) {
            this.number = number;
        }

        int getValue() {
            return number;
        }

        /**
         * Dummy implementation of isValid, can be anything.
         */
        boolean isValid() {
            return (this.number / this.number) == 1;
        }
    }

    /**
     * When you run this test, jqwik will generate a random integer that fails the assertion (e.g., 50 or higher).
     * The shrinking process then kicks in, trying to find the smallest failing example.
     * In this case, it should ideally shrink the counterexample to 50, the smallest integer that
     * fails the assertion number < 50.
     * <p>
     * Here's how the process unfolds:
     * <p>
     * 1) jqwik randomly generates a value, let's say 157.
     * 2) The test fails because 157 is not less than 50.
     * 3) jqwik starts the shrinking process to find a smaller failing example.
     * 4) It tries smaller values (e.g., 78, 64, 57, 53, 51) and continues until it reaches 50,
     * which is the smallest value for which the test still fails.
     * 5) The shrinking process stops and reports 50 as the minimal counter-example.
     * 6) Through this example, you can observe that the shrinking process automatically refines the failing test case,
     * making it easier to identify the exact boundary where the property does not hold,
     * thus simplifying debugging and understanding the nature of the test failure.
     *
     * @param number random number generated by the test
     */
    @Property
    public void allIntegersShouldBeLessThan50_ShrinkingProcess(@ForAll("numbersBetween1And100") int number) {
        //assertTrue(number < 50); // TODO DEMO: for failing and going to the shrinking process
    }

    /**
     * A Range from 1 to 60 to limit possibilities
     *
     * @return Arbitrary<Integer> number between 1 and 100
     */
    @Provide
    Arbitrary<Integer> numbersBetween1And100() {
        return Arbitraries.integers().between(1, 100);
    }

    /**
     * In this modified example, the @Report(Reporting.GENERATED) annotation above the sumOfIntegersShouldNotBe100
     * method ensures that jqwik logs all generated lists that it tests, including those during the shrinking process.
     * This output will help you see exactly what values were used during testing, providing a clearer view of the
     * testing process and the shrinking steps jqwik performs when it encounters a failing case.
     *
     * @param numbers a list of integers
     */
    @Property
    @Report(Reporting.GENERATED)
    public void sumOfIntegersShouldNotBe100(@ForAll("integerLists") List<Integer> numbers) {
        int sum = numbers.stream().mapToInt(Integer::intValue).sum();
        //assertTrue( sum != 100); // TODO DEMO
    }

    @Provide
    Arbitrary<List<Integer>> integerLists() {
        Random random = new Random();
        return Arbitraries.integers().between(1, 30).list().ofMinSize(1).ofMaxSize(10)
                .map(list -> {
                    // Introduce a random modification with a direct Random instance
                    if (random.nextBoolean()) {
                        int randomIndex = random.nextInt(list.size());
                        list.set(randomIndex, list.get(randomIndex) * 2);
                    }
                    return list;
                });
    }

    static class EmailValidator {
        static boolean isValid(String email) {
            int atIndex = email.indexOf('@');
            int dotIndex = email.lastIndexOf('.');
            return atIndex > 0 && dotIndex > atIndex && dotIndex < email.length() - 1;
        }
    }

    /**
     * This a jqwik test class with a property test to validate various email formats. The test will generate
     * strings and assert that the validation function's output matches the expected result for valid and invalid email formats.
     *
     * @param email values like emails to be validated
     */
    @Property
    @Report(Reporting.GENERATED)
    public void emailValidationTest(@ForAll String email) {
        boolean actualResult = EmailValidator.isValid(email);

        // Simulating a basic validation for expected result, based on simplistic conditions
        boolean expectedResult = email.contains("@") && email.lastIndexOf('.') > email.indexOf('@') && !email.endsWith(".");

        assertEquals(expectedResult, actualResult, "Email validation failed for: " + email);
    }

    /**
     * Use lower case chars from a through z
     *
     * @param input string values [a-z]
     */
    @Property
    void testStringWithCustomCharsetLowerCase(@ForAll @LowerChars String input) {
        assertTrue(input.matches("[a-z]*"), "Input string must contain only characters from '[a-z]'");
    }

    /**
     * Use lower case chars from A through Z
     *
     * @param input string values [A-Z]
     */
    @Property
    void testStringWithCustomCharsetUpperCase(@ForAll @UpperChars String input) {
        assertTrue(input.matches("[A-Z]*"), "Input string must contain only characters from '[A-Z]'");
    }

    @Property
    //@Report(Reporting.GENERATED)
    public void testUTF8String(@ForAll("utf8Strings") String input) {
        System.out.println("Generated UTF-8 String: " + input);
        assertFalse(input.isEmpty());
    }

    /**
     * Only uses this list of characters:
     * !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
     *
     * @return string within the character range
     */
    @Provide
    Arbitrary<String> utf8Strings() {
        return Arbitraries.strings()
                .withCharRange((char) 0x0020, (char) 0x007E) // Range of Unicode code points
                .ofMinLength(1)
                .ofMaxLength(50);
    }
}
