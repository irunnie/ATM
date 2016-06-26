package com.kohanevich.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;
import static com.kohanevich.service.Status.*;
import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class AtmCalculatorTest {

    private AtmCalculator calculator = new AtmCalculator();

    @DataProvider
    public static Object[][] withdrawDataProvider() {
        return new Object[][] {
            {500, EMPTY_ATM, prepareEmptyAtm(), prepareEmptyAtm()},
            {500, AVAILABLE, newHashMap(prepareFullAtm()), of(20, 20, 50, 20, 100, 20, 200, 20, 500, 19)},
            {300, AVAILABLE, newHashMap(prepareFullAtm()), of(20, 20, 50, 20, 100, 19, 200, 19, 500, 20)},
            {250, AVAILABLE, newHashMap(prepareFullAtm()), of(20, 20, 50, 19, 100, 20, 200, 19, 500, 20)},
            {-5, build(AVAILABLE_ONLY, 20), prepareFullAtm(), prepareFullAtm()},
            {0, build(AVAILABLE_ONLY, 20), prepareFullAtm(), prepareFullAtm()},
            {5, build(AVAILABLE_ONLY, 20), prepareFullAtm(), prepareFullAtm()},
            {30, build(AVAILABLE_ONLY, 20), prepareFullAtm(), prepareFullAtm()},
            {25, build(AVAILABLE_ONLY, 20), prepareFullAtm(), prepareFullAtm()},
            {310, build(AVAILABLE_ONLY, 300), prepareFullAtm(), prepareFullAtm()},
            {9999, build(AVAILABLE_ONLY, 5000), prepareFullAtm(), prepareFullAtm()},
            {250, AVAILABLE, newHashMap(of(20, 20, 50, 10, 100, 0, 200, 0, 500, 20)),
                        of(20, 20, 50, 5, 100, 0, 200, 0, 500, 20)},
            {300, build(AVAILABLE_ONLY, 200), newHashMap(of(20, 15, 50, 0, 100, 0, 200, 0, 500, 0)),
                    of(20, 15, 50, 0, 100, 0, 200, 0, 500, 0)},
        };
    }

    @Test
    @UseDataProvider("withdrawDataProvider")
    public void testWithdraw(int withdrawAmount, Status expectedStatus, Map<Integer, Integer> initAtm, Map<Integer, Integer> expectedAtm) {
        calculator.setAtm(initAtm);
        assertEquals(expectedStatus, calculator.withdraw(withdrawAmount));
        assertEquals(expectedAtm, calculator.getAtm());
    }

    private static Map<Integer, Integer> prepareEmptyAtm() {
       return of(20, 0, 50, 0, 100, 0, 200, 0, 500, 0);
    }

    private static Map<Integer, Integer> prepareFullAtm() {
        return of(20, 20, 50, 20, 100, 20, 200, 20, 500, 20);
    }
}