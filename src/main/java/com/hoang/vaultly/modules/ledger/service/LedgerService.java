package com.hoang.vaultly.modules.ledger.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.fund.repository.FundMemberRepository;
import com.hoang.vaultly.modules.fund.repository.FundRepository;
import com.hoang.vaultly.modules.ledger.dto.request.LedgerTransactionRequest;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.entity.LedgerAccount;
import com.hoang.vaultly.modules.ledger.entity.LedgerTransaction;
import com.hoang.vaultly.modules.ledger.enums.AccountType;
import com.hoang.vaultly.modules.ledger.enums.EntryType;
import com.hoang.vaultly.modules.ledger.enums.TransactionStatus;
import com.hoang.vaultly.modules.ledger.enums.TransactionType;
import com.hoang.vaultly.modules.ledger.mapper.TransactionResponseMapper;
import com.hoang.vaultly.modules.ledger.repository.LedgerAccountRepository;
import com.hoang.vaultly.modules.ledger.repository.LedgerTransactionRepository;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerService {
    private final FundRepository fundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final LedgerTransactionRepository ledgerTransactionRepository;

    private final UserService userService;

    private final TransactionResponseMapper transactionResponseMapper;

    /**
     * DEPOSIT: member sends money to fund
     * - DEBIT: CASH
     * - CREDIT: MEMBER_EQUITY
     */
    public LedgerTransactionResponse deposit(UUID fundId, LedgerTransactionRequest request){
        Fund fund = getFundOrThrow(fundId);
        User currentUser = userService.getCurrentUser();
        FundMember fundMember = fundMemberRepository.findByFund_IdAndUser_Id(fund.getId(), currentUser.getId());

        // Find member and fund ledger account
        LedgerAccount cashAccount = getFundAccount(fund.getId(), AccountType.CASH);
        LedgerAccount memberEquityAccount = getMemberAccount(fundMember.getId(), AccountType.MEMBER_EQUITY);

        // Create transaction
        LedgerTransaction ledgerTransaction = LedgerTransaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.COMPLETED)
                .createdBy(currentUser)
                .fund(fund)
                .description(request.description())
                .idempotencyKey(request.idempotencyKey())
                .build();

        ledgerTransaction.recordEntry(cashAccount, EntryType.DEBIT, request.amount());
        ledgerTransaction.recordEntry(memberEquityAccount, EntryType.CREDIT, request.amount());

        LedgerTransaction savedTransaction = ledgerTransactionRepository.save(ledgerTransaction);

        return transactionResponseMapper.toLedgerTransactionResponse(ledgerTransaction);
    }

    private Fund getFundOrThrow(UUID fundId){
        return fundRepository.findById(fundId).orElseThrow(() -> new AppException(ErrorCode.FUND_NOT_FOUND));
    }
    private LedgerAccount getFundAccount(UUID fundId, AccountType type){
        return ledgerAccountRepository.findByFund_IdAndAccountType(fundId, type);
    }
    private LedgerAccount getMemberAccount(UUID memberId, AccountType type){
        return ledgerAccountRepository.findByFundMember_IdAndAccountType(memberId, type);
    }
}
