package com.ws.tla.service.impl;

import com.ws.tla.constants.BankApiConstants;
import com.ws.tla.entity.Account;
import com.ws.tla.entity.Card;
import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.repository.AccountRepository;
import com.ws.tla.repository.CardRepository;
import com.ws.tla.service.IAccountService;
import com.ws.tla.service.ICustomerProfileService;
import com.ws.tla.util.InputValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private InputValidatorUtil inputValidatorUtil;

    @Autowired
    private ICustomerProfileService customerProfileService;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Account> getAccountsInfoList() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long acctId) throws NoDataException {
        log.info("Getting AccountInfo for AccountId{}", acctId);
        Optional<Account> account;
        if (acctId != null) {
            account = accountRepository.findById(acctId);

        } else {
            log.error("No Account info as AccountId is null");
            return null;
        }
        return account
                .orElseThrow(() -> new NoDataException(BankApiConstants.NO_ACCOUNT_FOUND));
    }

    @Override
    public void saveAccount(Long customerPrflId, @Valid Account account) throws NoDataException {
        inputValidatorUtil.nullCheck(account);
        CustomerProfile customerPrfl = customerProfileService.getCustomerPrflInfoById(customerPrflId);
        account.setCustomerProfile(customerPrfl);
        Card card = account.getCard();
        Account savedAccount = accountRepository.save(account);
        card.setAccount(savedAccount);
        cardRepository.save(card);
    }

    @Override
    public void deposit(Long acctID, int amt) {
        try {
            accountRepository.updateAccountByAcctID(acctID, amt);
        } catch (Exception ex) {
            log.error("Unable to update", ex);
        }
    }

    @Override
    public void withdraw(Long acctID, int amt, int initialBalance) throws BankServiceApiException {
        if (amt <= initialBalance) {
            accountRepository.withdrawAmountByAcctID(acctID, amt);
        } else {
            log.error("Not enough funds to withdraw amount {}", amt);
            throw new BankServiceApiException("Not enough funds available to withdraw");
        }
    }

    @Override
    public void transfer(Long sourceAcctId, Long destAcctId, int amount, int srcAcctTotalBalance, int destAcctTotalBalance) throws BankServiceApiException {
        if (amount <= srcAcctTotalBalance) {
            accountRepository.withdrawAmountByAcctID(sourceAcctId, amount);
            accountRepository.updateAccountByAcctID(destAcctId, amount);
        } else {
            log.error("Not enough funds to make a transfer {}", amount);
            throw new BankServiceApiException("Not enough funds available in Account to make a transfer");
        }
    }

    @Override
    public void delete(Long acctIdToDelete) {
        accountRepository.deleteById(acctIdToDelete);
    }
}
