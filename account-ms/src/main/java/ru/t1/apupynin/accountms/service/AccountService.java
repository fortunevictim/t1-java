package ru.t1.apupynin.accountms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.apupynin.accountms.entity.Account;
import ru.t1.apupynin.accountms.entity.Card;
import ru.t1.apupynin.accountms.entity.Transaction;
import ru.t1.apupynin.accountms.enums.AccountStatus;
import ru.t1.apupynin.accountms.enums.CardStatus;
import ru.t1.apupynin.accountms.enums.TransactionStatus;
import ru.t1.apupynin.accountms.enums.TransactionType;
import ru.t1.apupynin.accountms.repository.AccountRepository;
import ru.t1.apupynin.accountms.repository.CardRepository;
import ru.t1.apupynin.accountms.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          CardRepository cardRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void handleClientProducts(Map<String, Object> payload) {
        // Placeholder: in a full impl we'd create an Account for deposit-like products
        Long clientId = toLong(payload.get("clientId"));
        Long productId = toLong(payload.get("productId"));
        if (clientId == null || productId == null) return;
        Account account = new Account(clientId, productId, BigDecimal.ZERO, null, Boolean.TRUE, Boolean.FALSE, AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Transactional
    public void handleClientCards(Map<String, Object> payload) {
        log.info("Processing card request with payload: {}", payload);
        
        Long accountId = toLong(payload.get("accountId"));
        String paymentSystem = (String) payload.get("paymentSystem");
        
        log.info("Parsed accountId: {}, paymentSystem: {}", accountId, paymentSystem);
        
        if (accountId == null || paymentSystem == null) {
            log.warn("Missing required fields: accountId={}, paymentSystem={}", accountId, paymentSystem);
            return;
        }
        
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.warn("Account not found with id: {}", accountId);
            return;
        }
        
        if (account.getStatus() == AccountStatus.BLOCKED) {
            log.warn("Account {} is blocked, cannot create card", accountId);
            return;
        }
        
        String cardNumber = generateCardNumber();
        Card card = new Card(account.getId(), cardNumber, ru.t1.apupynin.accountms.enums.PaymentSystem.valueOf(paymentSystem), CardStatus.ACTIVE);
        Card savedCard = cardRepository.save(card);
        
        log.info("Created card with id: {}, number: {}, paymentSystem: {}", savedCard.getId(), cardNumber, paymentSystem);
    }

    @Transactional
    public void handleClientTransactions(Map<String, Object> payload) {
        // TODO maybe in 3rd HW or later
        Long accountId = toLong(payload.get("accountId"));
        String type = (String) payload.get("type");
        BigDecimal amount = toBigDecimal(payload.get("amount"));
        if (accountId == null || type == null || amount == null) return;
        Transaction tx = new Transaction(accountId, null, TransactionType.valueOf(type), amount, TransactionStatus.COMPLETE, LocalDateTime.now());
        transactionRepository.save(tx);
    }

    private static Long toLong(Object o) {
        if (o instanceof Number n) return n.longValue();
        if (o instanceof String s) try { return Long.parseLong(s); } catch (Exception ignored) {}
        return null;
    }

    private static BigDecimal toBigDecimal(Object o) {
        if (o instanceof BigDecimal b) return b;
        if (o instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
        if (o instanceof String s) try { return new BigDecimal(s); } catch (Exception ignored) {}
        return null;
    }

    private static String generateCardNumber() {
        String nanoTime = String.valueOf(System.nanoTime());
        // Ensure we have at least 16 characters by padding with zeros if needed
        if (nanoTime.length() < 16) {
            nanoTime = String.format("%016d", Long.parseLong(nanoTime));
        }
        return nanoTime.substring(0, 16);
    }
}


