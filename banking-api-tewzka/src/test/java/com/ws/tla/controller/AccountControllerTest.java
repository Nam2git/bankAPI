package com.ws.tla.controller;

import com.ws.tla.entity.Account;
import com.ws.tla.entity.Card;
import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.model.dto.AccountDto;
import com.ws.tla.model.dto.BalanceDto;
import com.ws.tla.model.dto.CardDto;
import com.ws.tla.model.dto.StatusResponse;
import com.ws.tla.repository.AccountRepository;
import com.ws.tla.service.IAccountService;
import com.ws.tla.service.ICustomerProfileService;
import com.ws.tla.service.helper.BankServiceApiHelper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ws.tla.enums.CardType.CREDIT_CARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private IAccountService accountService;

    @Mock
    private ICustomerProfileService customerProfileService;

    @Mock
    private Account account;

    @Mock
    private InputValidatorUtil inputValidatorUtil;

    @Mock
    private BankServiceApiHelper bankServiceApiHelper;

    @Mock
    private BalanceDto balanceDto;

    @Before
    public void Setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testGetAllAccounts() {
        List<Account> acctList = new ArrayList<>();
        acctList.add(getAccountDetails());

        when(accountService.getAccountsInfoList()).thenReturn(acctList);
        ResponseEntity<java.util.List<Account>> actualResponse = accountController.getAllAccounts();
        assertNotNull(actualResponse);
    }

    @Test()
    public void testGetAccountById() throws NoDataException {
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(getAccountDetails()));
        when(accountService.getAccountById(any(Long.class))).thenReturn(getAccountDetails());
        ResponseEntity<Account> actualResponse = accountController.getAccountById(123L);
        assertNotNull(actualResponse);
        assertEquals(123L, actualResponse.getBody().getAcctId());
    }

    @Test
    public void testGetAccountByIdFailures() throws NoDataException {
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(getAccountDetails()));
        when(accountService.getAccountById(any(Long.class))).thenReturn(null);
        ResponseEntity<Account> actualResponse = accountController.getAccountById(123L);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test()
    public void testCreateAccount() throws NoDataException {
        when(bankServiceApiHelper.accountBuilder(getAccountDto())).thenReturn(getAccountDetails());
        doNothing().when(inputValidatorUtil).nullCheck(any(Object.class));
        when(customerProfileService.getCustomerPrflInfoById(any(Long.class))).thenReturn(new CustomerProfile());
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        doNothing().when(accountService).saveAccount(any(Long.class), any(Account.class));
        ResponseEntity<StatusResponse> actualResponse = accountController.createAccount(getAccountDto());
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(new StatusResponse("Account Created Successfully"), actualResponse.getBody());
    }

    @Test()
    public void testGetBalance() throws NoDataException {
        when(accountService.getAccountById(any(Long.class))).thenReturn(getAccountDetails());
        when(bankServiceApiHelper.accountBalanceBuilder(getAccountDetails())).thenReturn(getBalanceDto());
        ResponseEntity<BalanceDto> actualResponse = accountController.getBalance(any(Long.class));
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    public void testGetBalanceFailures() throws NoDataException {
        when(accountService.getAccountById(any(Long.class))).thenReturn(null);
        when(bankServiceApiHelper.accountBalanceBuilder(getAccountDetails())).thenReturn(null);
        ResponseEntity<BalanceDto> actualResponse = accountController.getBalance(any(Long.class));
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test()
    public void testDepositAmount() throws NoDataException {
        BalanceDto balanceDto = getBalanceDto();
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(balanceDto);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.depositAmt(account.getAcctId(), 1200);
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.OK, actualStatusResponse.getStatusCode());
        assertEquals(new StatusResponse("Amount 1200 is deposited to AccountId 123 and total current balance is: 2400"), actualStatusResponse.getBody());
    }

    @Test()
    public void testDepositAmountFailures() throws NoDataException {
        doNothing().when(accountService).deposit(getAccountDetails().getAcctId(), 200);
        ResponseEntity<StatusResponse> actualResponse = accountController.depositAmt(getAccountDetails().getAcctId(), 200);
        assertNotNull(actualResponse);
    }

    @Test()
    public void testDeleteAcct() throws NoDataException, BankServiceApiException {
        BalanceDto balanceDto = getBalanceDto();
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(balanceDto);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.deleteAcct(account.getAcctId());
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.OK, actualStatusResponse.getStatusCode());
        assertEquals(new StatusResponse("Account 123 is deleted "), actualStatusResponse.getBody());
    }

    @Test()
    public void testDeleteAcctFailures() throws NoDataException, BankServiceApiException {
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(null);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.deleteAcct(account.getAcctId());
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.BAD_REQUEST, actualStatusResponse.getStatusCode());
    }

    @Test()
    public void testWithdrawAmt() throws NoDataException, BankServiceApiException {
        BalanceDto balanceDto = getBalanceDto();
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(balanceDto);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.withdrawAmt(account.getAcctId(), 100);
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.OK, actualStatusResponse.getStatusCode());
        assertEquals(new StatusResponse("Amount 100 is withdrawn from AccountId 123 and Total Balance is: 1100"), actualStatusResponse.getBody());
    }

    @Test()
    public void testWithdrawAmtFailures() throws NoDataException, BankServiceApiException {
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(null);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.withdrawAmt(account.getAcctId(), 100);
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.BAD_REQUEST, actualStatusResponse.getStatusCode());
    }

    @Test()
    public void testTransferAmt() throws NoDataException, BankServiceApiException {
        BalanceDto balanceDto = getBalanceDto();
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(balanceDto);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.transferAmt(account.getAcctId(), 211L, 200);
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.OK, actualStatusResponse.getStatusCode());
        assertEquals(new StatusResponse("Amount 200 is transferred from AccountId 123 to Account Id 211"), actualStatusResponse.getBody());
    }

    @Test()
    public void testTransferAmtFailures() throws NoDataException, BankServiceApiException {
        Account account = getAccountDetails();
        when(accountService.getAccountById(any(Long.class))).thenReturn(account);
        when(bankServiceApiHelper.accountBalanceBuilder(account)).thenReturn(null);
        doReturn(ResponseEntity.ok().body(balanceDto)).when(Mockito.mock(AccountController.class)).getBalance(any(Long.class));
        ResponseEntity<StatusResponse> actualStatusResponse = accountController.transferAmt(account.getAcctId(), 211L, 100);
        assertNotNull(actualStatusResponse);
        assertEquals(HttpStatus.BAD_REQUEST, actualStatusResponse.getStatusCode());
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

    private AccountDto getAccountDto() {
        CardDto cardDto = new CardDto();
        cardDto.setCardNumber("12345678");
        cardDto.setCardType(CREDIT_CARD);
        cardDto.setCvvNumber(1235L);
        cardDto.setAvailableCardLimit(1200);

        AccountDto accountDto = new AccountDto();
        accountDto.setCard(cardDto);
        accountDto.setCurrentBalance(BigDecimal.valueOf(1200));
        accountDto.setCustomerPrflId(111L);

        return accountDto;
    }

    private BalanceDto getBalanceDto() {
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setAccountId(123L);
        balanceDto.setCurrentBalance(BigDecimal.valueOf(1200));
        return balanceDto;
    }
}
