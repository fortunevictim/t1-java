package ru.t1.apupynin.creditms.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AnnuityCalculatorTest {

    @Test
    void monthlyPayment_basic() {
        AnnuityCalculator calc = new AnnuityCalculator();
        BigDecimal payment = calc.monthlyPayment(new BigDecimal("100000"), new BigDecimal("0.12"), 12);
        assertTrue(payment.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(payment.compareTo(new BigDecimal("10000")) < 0);
    }

    @Test
    void monthlyPayment_throwsOnZeroMonths() {
        AnnuityCalculator calc = new AnnuityCalculator();
        assertThrows(IllegalArgumentException.class, () -> calc.monthlyPayment(new BigDecimal("100000"), new BigDecimal("0.12"), 0));
    }
}
