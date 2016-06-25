package com.kohanevich.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AtmCalculatorTest {

    @Test
    public void testWithdraw() {
        int withdrawAmount = 500;
        AtmCalculator calculator = new AtmCalculator();
        int initAmount = calculator.checkAmount();
        calculator.withdraw(withdrawAmount);
        int finalAmount = calculator.checkAmount();
        int actualAmount = initAmount - finalAmount;
        assertEquals(withdrawAmount, actualAmount);
    }
}