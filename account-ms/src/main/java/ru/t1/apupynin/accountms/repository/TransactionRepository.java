package ru.t1.apupynin.accountms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.accountms.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}


