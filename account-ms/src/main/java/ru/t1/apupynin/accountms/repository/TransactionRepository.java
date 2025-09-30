package ru.t1.apupynin.accountms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.accountms.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardIdAndTimestampAfter(Long cardId, LocalDateTime timestamp);
}


