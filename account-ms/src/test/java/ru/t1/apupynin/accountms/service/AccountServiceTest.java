package ru.t1.apupynin.accountms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.apupynin.accountms.entity.Account;
import ru.t1.apupynin.accountms.entity.Transaction;
import ru.t1.apupynin.accountms.enums.AccountStatus;
import ru.t1.apupynin.accountms.enums.TransactionStatus;
import ru.t1.apupynin.accountms.enums.TransactionType;
import ru.t1.apupynin.accountms.repository.AccountRepository;
import ru.t1.apupynin.accountms.repository.CardRepository;
import ru.t1.apupynin.accountms.repository.PaymentRepository;
import ru.t1.apupynin.accountms.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock AccountRepository accountRepository;
    @Mock CardRepository cardRepository;
    @Mock PaymentRepository paymentRepository;
    @Mock TransactionRepository transactionRepository;

    @InjectMocks AccountService accountService;

    Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setId(1L);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(new BigDecimal("1000"));
    }

    @Test
    void handleClientTransactions_missingRequiredFields_returns() {
        accountService.handleClientTransactions(Map.of("accountId", 1L));
        verifyNoInteractions(accountRepository, transactionRepository);
    }

    @Test
    void handleClientTransactions_accountNotFound_returns() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        accountService.handleClientTransactions(Map.of(
                "accountId", 1L,
                "type", TransactionType.DEBIT.name(),
                "amount", new BigDecimal("100")
        ));
        verify(accountRepository).findById(1L);
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void handleClientTransactions_blockedAccount_savesBlockedTransaction() {
        account.setStatus(AccountStatus.BLOCKED);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.handleClientTransactions(Map.of(
                "accountId", 1L,
                "cardId", 2L,
                "type", TransactionType.DEBIT.name(),
                "amount", new BigDecimal("100")
        ));

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        assertEquals(TransactionStatus.BLOCKED, captor.getValue().getStatus());
    }
}
