package ru.t1.apupynin.accountms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.apupynin.accountms.entity.Account;
import ru.t1.apupynin.accountms.entity.Card;
import ru.t1.apupynin.accountms.entity.Transaction;
import ru.t1.apupynin.accountms.enums.AccountStatus;
import ru.t1.apupynin.accountms.enums.CardStatus;
import ru.t1.apupynin.accountms.enums.PaymentSystem;
import ru.t1.apupynin.accountms.enums.TransactionStatus;
import ru.t1.apupynin.accountms.enums.TransactionType;
import ru.t1.apupynin.accountms.repository.AccountRepository;
import ru.t1.apupynin.accountms.repository.CardRepository;
import ru.t1.apupynin.accountms.repository.PaymentRepository;
import ru.t1.apupynin.accountms.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceMoreTest {

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
        account.setIsRecalc(false);
    }

    @Test
    void creditTransaction_increasesBalance_andSavesCompleteTransaction() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.handleClientTransactions(Map.of(
                "accountId", 1L,
                "type", TransactionType.CREDIT.name(),
                "amount", new BigDecimal("200")
        ));

        assertEquals(new BigDecimal("1200"), account.getBalance());
        verify(accountRepository).save(account);
        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());
        assertEquals(TransactionStatus.COMPLETE, txCaptor.getValue().getStatus());
    }

    @Test
    void debitTransaction_insufficientFunds_blocksTransaction() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.handleClientTransactions(Map.of(
                "accountId", 1L,
                "type", TransactionType.DEBIT.name(),
                "amount", new BigDecimal("2000")
        ));

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());
        assertEquals(TransactionStatus.BLOCKED, txCaptor.getValue().getStatus());
        assertEquals(new BigDecimal("1000"), account.getBalance());
    }

    @Test
    void handleClientCards_missingFields_returns() {
        accountService.handleClientCards(Map.of("accountId", 1L));
        verifyNoInteractions(accountRepository, cardRepository);
    }

    @Test
    void handleClientCards_blockedAccount_noCardCreated() {
        account.setStatus(AccountStatus.BLOCKED);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.handleClientCards(Map.of(
                "accountId", 1L,
                "paymentSystem", PaymentSystem.VISA.name()
        ));

        verify(accountRepository).findById(1L);
        verifyNoInteractions(cardRepository);
    }

    @Test
    void handleClientCards_happyPath_savesCard() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(cardRepository.save(any(Card.class))).thenAnswer(inv -> {
            Card c = inv.getArgument(0);
            c.setId(99L);
            return c;
        });

        accountService.handleClientCards(Map.of(
                "accountId", 1L,
                "paymentSystem", PaymentSystem.MASTERCARD.name()
        ));

        verify(cardRepository).save(any(Card.class));
    }
}
