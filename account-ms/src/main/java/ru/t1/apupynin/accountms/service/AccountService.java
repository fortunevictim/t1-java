package ru.t1.apupynin.accountms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.apupynin.accountms.entity.Account;
import ru.t1.apupynin.accountms.entity.Card;
import ru.t1.apupynin.accountms.entity.Payment;
import ru.t1.apupynin.accountms.entity.Transaction;
import ru.t1.apupynin.accountms.enums.AccountStatus;
import ru.t1.apupynin.accountms.enums.CardStatus;
import ru.t1.apupynin.accountms.enums.PaymentType;
import ru.t1.apupynin.accountms.enums.TransactionStatus;
import ru.t1.apupynin.accountms.enums.TransactionType;
import ru.t1.apupynin.accountms.repository.AccountRepository;
import ru.t1.apupynin.accountms.repository.CardRepository;
import ru.t1.apupynin.accountms.repository.PaymentRepository;
import ru.t1.apupynin.accountms.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    public AccountService(AccountRepository accountRepository,
                          CardRepository cardRepository,
                          TransactionRepository transactionRepository,
                          PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void handleClientProducts(Map<String, Object> payload) {
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
        log.info("Processing transaction with payload: {}", payload);
        
        Long accountId = toLong(payload.get("accountId"));
        Long cardId = toLong(payload.get("cardId"));
        String type = (String) payload.get("type");
        BigDecimal amount = toBigDecimal(payload.get("amount"));
        
        if (accountId == null || type == null || amount == null) {
            log.warn("Missing required fields for transaction: accountId={}, type={}, amount={}", accountId, type, amount);
            return;
        }
        
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.warn("Account not found with id: {}", accountId);
            return;
        }
        
        if (account.getStatus() == AccountStatus.BLOCKED || account.getStatus() == AccountStatus.SUSPENDED) {
            log.warn("Account {} is blocked/suspended, transaction rejected", accountId);
            Transaction tx = new Transaction(accountId, cardId, TransactionType.valueOf(type), amount, TransactionStatus.BLOCKED, LocalDateTime.now());
            transactionRepository.save(tx);
            return;
        }
        
        if (cardId != null && isCardSuspicious(cardId)) {
            log.warn("Card {} has suspicious activity, blocking account {}", cardId, accountId);
            account.setStatus(AccountStatus.BLOCKED);
            accountRepository.save(account);
            Transaction tx = new Transaction(accountId, cardId, TransactionType.valueOf(type), amount, TransactionStatus.BLOCKED, LocalDateTime.now());
            transactionRepository.save(tx);
            return;
        }
        
        TransactionType transactionType = TransactionType.valueOf(type);
        boolean isCredit = isCreditTransaction(transactionType);
        
        if (isCredit) {
            account.setBalance(account.getBalance().add(amount));
            log.info("Credited {} to account {}, new balance: {}", amount, accountId, account.getBalance());
            
            if (account.getIsRecalc() && isPaymentDay(account)) {
                processMonthlyPayment(account);
            }
        } else {
            if (account.getBalance().compareTo(amount) >= 0) {
                account.setBalance(account.getBalance().subtract(amount));
                log.info("Debited {} from account {}, new balance: {}", amount, accountId, account.getBalance());
            } else {
                log.warn("Insufficient funds for account {}, balance: {}, requested: {}", accountId, account.getBalance(), amount);
                Transaction tx = new Transaction(accountId, cardId, transactionType, amount, TransactionStatus.BLOCKED, LocalDateTime.now());
                transactionRepository.save(tx);
                return;
            }
        }
        
        accountRepository.save(account);
        
        Transaction tx = new Transaction(accountId, cardId, transactionType, amount, TransactionStatus.COMPLETE, LocalDateTime.now());
        transactionRepository.save(tx);
        
        if (account.getIsRecalc() && isCredit) {
            createPaymentSchedule(account, amount);
        }
        
        log.info("Transaction processed successfully for account {}", accountId);
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

    private boolean isCreditTransaction(TransactionType type) {
        return type == TransactionType.CREDIT || type == TransactionType.TRANSFER_IN || type == TransactionType.REFUND;
    }
    
    private boolean isCardSuspicious(Long cardId) {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minus(5, ChronoUnit.MINUTES);
        List<Transaction> recentTransactions = transactionRepository.findByCardIdAndTimestampAfter(cardId, fiveMinutesAgo);
        
        return recentTransactions.size() > 10;
    }
    
    private boolean isPaymentDay(Account account) {
        return LocalDateTime.now().getDayOfMonth() == 15;
    }
    
    private void processMonthlyPayment(Account account) {
        if (account.getInterestRate() == null) return;
        
        BigDecimal monthlyPayment = account.getBalance().multiply(account.getInterestRate()).divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
        
        if (account.getBalance().compareTo(monthlyPayment) >= 0) {
            account.setBalance(account.getBalance().subtract(monthlyPayment));
            log.info("Processed monthly payment {} for account {}, new balance: {}", monthlyPayment, account.getId(), account.getBalance());
        } else {
            log.warn("Insufficient funds for monthly payment {} on account {}, balance: {}", monthlyPayment, account.getId(), account.getBalance());
            markOverduePayments(account);
        }
    }
    
    private void createPaymentSchedule(Account account, BigDecimal amount) {
        if (account.getInterestRate() == null) return;
        
        LocalDateTime paymentDate = LocalDateTime.now().plusMonths(1);
        BigDecimal monthlyPayment = amount.multiply(account.getInterestRate()).divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
        
        for (int i = 0; i < 12; i++) {
            Payment payment = new Payment(
                account.getId(),
                paymentDate.plusMonths(i),
                monthlyPayment,
                true, // isCredit
                null, // payedAt
                PaymentType.PAYMENT,
                false // expired
            );
            paymentRepository.save(payment);
        }
        
        log.info("Created payment schedule for account {} with {} monthly payments of {}", account.getId(), 12, monthlyPayment);
    }
    
    private void markOverduePayments(Account account) {
        List<Payment> overduePayments = paymentRepository.findByAccountIdAndPayedAtIsNullAndPaymentDateBefore(account.getId(), LocalDateTime.now());
        for (Payment payment : overduePayments) {
            payment.setExpired(true);
            paymentRepository.save(payment);
        }
        log.info("Marked {} overdue payments for account {}", overduePayments.size(), account.getId());
    }
    
    @Transactional
    public void handleClientPayments(Map<String, Object> payload) {
        log.info("Processing payment with payload: {}", payload);
        
        Long accountId = toLong(payload.get("accountId"));
        BigDecimal amount = toBigDecimal(payload.get("amount"));
        
        if (accountId == null || amount == null) {
            log.warn("Missing required fields for payment: accountId={}, amount={}", accountId, amount);
            return;
        }
        
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.warn("Account not found with id: {}", accountId);
            return;
        }
        
        List<Payment> unpaidPayments = paymentRepository.findByAccountIdAndPayedAtIsNull(accountId);
        BigDecimal totalDebt = unpaidPayments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (amount.compareTo(totalDebt) == 0) {
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            
            Payment payment = new Payment(
                accountId,
                LocalDateTime.now(),
                amount,
                true, // isCredit
                LocalDateTime.now(), // payedAt
                PaymentType.PAYMENT,
                false // expired
            );
            paymentRepository.save(payment);
            
            for (Payment unpaidPayment : unpaidPayments) {
                unpaidPayment.setPayedAt(LocalDateTime.now());
                paymentRepository.save(unpaidPayment);
            }
            
            log.info("Payment processed successfully for account {}, amount: {}", accountId, amount);
        } else {
            log.warn("Payment amount {} does not match total debt {} for account {}", amount, totalDebt, accountId);
        }
    }
}


