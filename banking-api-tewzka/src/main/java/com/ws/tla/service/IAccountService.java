package com.ws.tla.service;

import com.ws.tla.entity.Account;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.NoDataException;

import javax.validation.Valid;
import java.util.List;

public interface IAccountService {

    List<Account> getAccountsInfoList();

    Account getAccountById(Long accId) throws NoDataException;

    void saveAccount(Long customerPrflId, @Valid Account account) throws NoDataException;

    void deposit(Long acctID, int amt);

    void withdraw(Long acctID, int amt, int initialBalance) throws BankServiceApiException;

    void transfer(Long sourceAcctId, Long destAcctId, int amount, int srcAcctTotalBalance, int destAcctTotalBalance) throws BankServiceApiException;

    void delete(Long acctId) throws BankServiceApiException;
}
