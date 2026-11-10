package com.hoang.vaultly.modules.ledger.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.service.FundService;
import com.hoang.vaultly.modules.ledger.dto.request.LedgerTransactionRequest;
import com.hoang.vaultly.modules.ledger.dto.response.FundBalanceResponse;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.enums.SystemRole;
import com.hoang.vaultly.modules.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LedgerIdempotencyDuplicateTest {
    @Autowired
    LedgerService ledgerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FundService fundService;

    private User testUser;
    private UUID fundId;

    @BeforeEach
    void setUp() {
        String uniqueUsername = "tester_" + UUID.randomUUID().toString().substring(0, 8);
        testUser = userRepository.save(User.builder()
                .username(uniqueUsername)
                .password("password123")
                .displayName("Concurrency Tester")
                .systemRole(SystemRole.USER)
                .build());

        setAuthentication(testUser.getUsername());

        FundCreationResponse fund = fundService.createFund(new FundCreationRequest(
                "Test Fund", "Test Concurrency", "VND"));
        fundId = fund.id();

    }

    @Test
    @DisplayName("Multiple threads sending the same idempotency key, expect only one is success")
    void testConcurrentRequestWithSameIdempotencyKey() throws InterruptedException {
        int threadCount = 10;
        BigDecimal depositAmount = new BigDecimal(100_000);
        UUID sharedIdempotencyKey = UUID.randomUUID();

        // Create thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger duplicateBlockedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                setAuthentication(testUser.getUsername());

                readyLatch.countDown();
                try {
                    startLatch.await();

                    ledgerService.deposit(fundId, new LedgerTransactionRequest(
                            depositAmount,
                            "Concurrent Deposit with" + " same key",
                            sharedIdempotencyKey));
                    successCount.incrementAndGet();
                } catch (AppException exception) {
                    if (exception.getErrorCode() == ErrorCode.TRANSACTION_DUPLICATE) {
                        duplicateBlockedCount.incrementAndGet();
                    }
                } catch (DataIntegrityViolationException exception) {
                    // Chặn ở tầng PostgreSQL Database (Unique Constraint)
                    duplicateBlockedCount.incrementAndGet();
                } catch (Exception ignored) {

                } finally {
                    doneLatch.countDown();
                }
            });
        }
        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();


        assertEquals(1, successCount.get(), "Only one transaction is recorded");
        assertEquals(threadCount - 1, duplicateBlockedCount.get(), "All duplicate requests must be blocked");

        // Check balance: Fund cash only increments once (100.000 VND)
        setAuthentication(testUser.getUsername());
        FundBalanceResponse balance = ledgerService.getFundBalance(fundId);
        BigDecimal expectedCash = BigDecimal.ZERO.add(depositAmount);

        assertEquals(0, balance.cashBalance().compareTo(expectedCash),
                "Fund balance only increments once, equals deposit amount");
    }

    private void setAuthentication(String username) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
