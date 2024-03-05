package com.wtech.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalValuesTest {

    @Test
    void testAddPositives() {
        int answer = App.add(2, 3);
        assertEquals(answer, 5);
    }

    @Test
    void testAddNegatives() {
        int answer = App.add(-2, -3);
        assertEquals(answer, -5);
    }
}