package ru.t1.apupynin.accountms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.accountms.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountIdAndPayedAtIsNull(Long accountId);
    List<Payment> findByAccountIdAndPayedAtIsNullAndPaymentDateBefore(Long accountId, LocalDateTime date);
}


