package com.ws.tla.service.impl;

import com.ws.tla.entity.Account;
import com.ws.tla.entity.Card;
import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.repository.AccountRepository;
import com.ws.tla.repository.CardRepository;
import com.ws.tla.service.ICustomerProfileService;
import com.ws.tla.util.InputValidatorUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ICustomerProfileService customerProfileService;

    @Mock
    private InputValidatorUtil inputValidatorUtil;

    @Before
    public void Setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAccountsInfoList() {
        List<Account> acctList = new ArrayList<>();
        acctList.add(getAccountDetails());
        when(accountRepository.findAll()).thenReturn(acctList);
        List<Account> accountList = accountServiceImpl.getAccountsInfoList();
        assertNotNull(accountList);
        verify(accountRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetAccountById() throws NoDataException {
        when(accountRepository.findById(getAccountDetails().getAcctId())).thenReturn(Optional.of(getAccountDetails()));
        Account account = accountServiceImpl.getAccountById(getAccountDetails().getAcctId());
        assertNotNull(account);
        verify(accountRepository, Mockito.times(1)).findById(getAccountDetails().getAcctId());

    }

    @Test(expected = NoDataException.class)
    public void testGetAccountByIdFailure() throws NoDataException {
        when(accountRepository.findById(null)).thenReturn(Optional.of(getAccountDetails()));
        assertNull(accountServiceImpl.getAccountById(getAccountDetails().getAcctId()));
        verify(accountRepository, Mockito.times(1)).findById(getAccountDetails().getAcctId());

    }

    @Test
    public void testSaveAccount() throws NoDataException {
        Account account = getAccountDetails();
        doNothing().when(inputValidatorUtil).nullCheck(any(Object.class));
        when(customerProfileService.getCustomerPrflInfoById(account.getAcctId())).thenReturn(account.getCustomerProfile());
        when(accountRepository.save(account)).thenReturn(account);
        when(cardRepository.save(account.getCard())).thenReturn(account.getCard());
        accountServiceImpl.saveAccount(account.getCustomerProfile().getCustProfileId(), account);
        verify(accountRepository, Mockito.times(1)).save(account);
        verify(cardRepository, Mockito.times(1)).save(account.getCard());
    }

    @Test
    public void testDeposit() {
        Account account = getAccountDetails();
        doNothing().when(accountRepository).updateAccountByAcctID(account.getAcctId(), 1200);
        accountServiceImpl.deposit(account.getAcctId(), 1200);
        verify(accountRepository, Mockito.times(1)).updateAccountByAcctID(account.getAcctId(), 1200);
    }

    @Test
    public void testWithdraw() throws BankServiceApiException {
        Account account = getAccountDetails();
        doNothing().when(accountRepository).withdrawAmountByAcctID(account.getAcctId(), 100);
        accountServiceImpl.withdraw(account.getAcctId(), 100, Math.toIntExact(getAccountDetails().getBalance()));
        verify(accountRepository, Mockito.times(1)).withdrawAmountByAcctID(account.getAcctId(), 100);
    }

    @Test(expected = BankServiceApiException.class)
    public void testWithdrawFailure() throws BankServiceApiException {
        Account account = getAccountDetails();
        doNothing().when(accountRepository).withdrawAmountByAcctID(account.getAcctId(), 1000);
        accountServiceImpl.withdraw(account.getAcctId(), 1000, Math.toIntExact(getAccountDetails().getBalance()));
        verify(accountRepository, Mockito.times(1)).withdrawAmountByAcctID(account.getAcctId(), 1000);
    }

    @Test
    public void testTransfer() throws BankServiceApiException {
        Account account = getAccountDetails();
        doNothing().when(accountRepository).withdrawAmountByAcctID(account.getAcctId(), 100);
        doNothing().when(accountRepository).updateAccountByAcctID(222, 100);
        accountServiceImpl.transfer(account.getAcctId(), 222L, 100, 200, 100);
        verify(accountRepository, Mockito.times(1)).withdrawAmountByAcctID(account.getAcctId(), 100);
        verify(accountRepository, Mockito.times(1)).updateAccountByAcctID(222, 100);

    }

    @Test(expected = BankServiceApiException.class)
    public void testTransferFailure() throws BankServiceApiException {
        Account account = getAccountDetails();
        doNothing().when(accountRepository).withdrawAmountByAcctID(account.getAcctId(), 1000);
        doNothing().when(accountRepository).updateAccountByAcctID(222, 1000);
        accountServiceImpl.transfer(account.getAcctId(), 222L, 1000, 200, 100);
        verify(accountRepository, Mockito.times(1)).withdrawAmountByAcctID(account.getAcctId(), 1000);
        verify(accountRepository, Mockito.times(1)).updateAccountByAcctID(222, 1000);

    }

    @Test
    public void testDelete() throws BankServiceApiException {
        Account account = getAccountDetails();
        doNothing().when(accountRepository).deleteById(account.getAcctId());
        accountServiceImpl.delete(account.getAcctId());
        verify(accountRepository, Mockito.times(1)).deleteById(account.getAcctId());
    }

    private Account getAccountDetails() {
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setCustProfileId(111L);

        Card card = new Card();
        card.setCardId(55667788L);

        Account account = new Account();
        account.setAcctId(123L);
        account.setCustomerProfile(customerProfile);
        account.setAcctNum("12345");
        account.setBalance(200);
        account.setCard(card);
        account.setAcctStat("Active");
        return account;
    }
}
