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
public class LedgerConcurrencyTest {

    @Autowired
    private LedgerService ledgerService;

    @Autowired
    private FundService fundService;

    @Autowired
    private UserRepository userRepository;

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

        // deposit 1000000 to fund
        ledgerService.deposit(fundId, new LedgerTransactionRequest(
                new BigDecimal("1000000"),
                "Initial Deposit",
                UUID.randomUUID()
        ));
    }

    @Test
    @DisplayName("10 thread concurrently withdraw 200.000k from fund, expect fund money will be 0")
    void testConcurrentWithdrawalsPreventOverdraft() throws InterruptedException {
        int threadCount = 10;
        BigDecimal withdrawalAmount = new BigDecimal("200000");

        // create thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // CountDownLatch: used as a start signal
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                // Assign login permission to child threads
                setAuthentication(testUser.getUsername());

                readyLatch.countDown(); // notify "Ready"
                try {
                    startLatch.await(); // wait for start latch

                    // Start withdrawing
                    ledgerService.withdrawal(fundId, new LedgerTransactionRequest(
                            withdrawalAmount,
                            "Concurrent Withdrawal",
                            UUID.randomUUID() // unique idempotency key for each request
                    ));
                    successCount.incrementAndGet(); // withdraw successfully
                } catch (AppException ex) {
                    // If request gets blocked (insufficient equity/fund cash) -> test success
                    if (ex.getErrorCode() == ErrorCode.INSUFFICIENT_MEMBER_EQUITY
                            || ex.getErrorCode() == ErrorCode.INSUFFICIENT_FUND_CASH) {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception ignored) {
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(); // wait until 10 threads are ready
        startLatch.countDown(); // shoot all 10 threads calling withdrawal() at the same time
        doneLatch.await(); // wait until 10 threads are done
        executorService.shutdown();

        // Assertion
        // assertEquals(expectation, reality)
        assertEquals(5, successCount.get(), "Exactly 5 transactions are allowed to be commited");
        assertEquals(5, failureCount.get(), "Exactly 5 transactions are blocked due to insufficient user equity");

        // Read the actual balance from database
        setAuthentication(testUser.getUsername());
        FundBalanceResponse balance = ledgerService.getFundBalance(fundId);

        assertEquals(0, balance.cashBalance().compareTo(BigDecimal.ZERO), "Fund cash must be 0");
        assertEquals(0, balance.myEquity().compareTo(BigDecimal.ZERO), "Member equity must be 0");
    }

    private void setAuthentication(String username) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}