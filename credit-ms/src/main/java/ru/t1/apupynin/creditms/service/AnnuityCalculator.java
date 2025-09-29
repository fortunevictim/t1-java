package ru.t1.apupynin.creditms.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnnuityCalculator {

    public static class PaymentItem {
        public LocalDateTime paymentDate;
        public BigDecimal paymentAmount;
        public BigDecimal interestPart;
        public BigDecimal principalPart;
        public BigDecimal remainingDebt;
    }

    public BigDecimal monthlyPayment(BigDecimal principal, BigDecimal annualRate, int months) {
        if (months <= 0) throw new IllegalArgumentException("months must be > 0");
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), mc);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate, mc);
        BigDecimal pow = onePlusR.pow(months, mc);
        BigDecimal numerator = monthlyRate.multiply(pow, mc);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE, mc);
        return principal.multiply(numerator, mc).divide(denominator, 2, RoundingMode.HALF_UP);
    }

    public List<PaymentItem> schedule(BigDecimal principal, BigDecimal annualRate, int months, LocalDateTime firstPaymentDate) {
        List<PaymentItem> result = new ArrayList<>();
        BigDecimal payment = monthlyPayment(principal, annualRate, months);
        BigDecimal remaining = principal;
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), mc);

        for (int i = 0; i < months; i++) {
            PaymentItem item = new PaymentItem();
            item.paymentDate = firstPaymentDate.plusMonths(i);
            item.paymentAmount = payment;
            item.interestPart = remaining.multiply(monthlyRate, mc).setScale(2, RoundingMode.HALF_UP);
            item.principalPart = payment.subtract(item.interestPart, mc).setScale(2, RoundingMode.HALF_UP);
            remaining = remaining.subtract(item.principalPart, mc).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            item.remainingDebt = remaining;
            result.add(item);
        }
        return result;
    }
}


