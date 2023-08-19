package com.ws.tla.controller;

import com.ws.tla.entity.Account;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.ErrorResponse;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.model.dto.AccountDto;
import com.ws.tla.model.dto.BalanceDto;
import com.ws.tla.model.dto.StatusResponse;
import com.ws.tla.service.IAccountService;
import com.ws.tla.service.helper.BankServiceApiHelper;
import com.ws.tla.util.InputValidatorUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v0/banksvc/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private InputValidatorUtil inputValidatorUtil;

    @Autowired
    private BankServiceApiHelper bankServiceApiHelper;

    @ApiOperation(value = "Get All Accounts Details")
    @GetMapping(value = "/accountslist")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok().body(accountService.getAccountsInfoList());
    }

    @ApiOperation(value = "Get Account Details by AcctId")
    @GetMapping(value = "/{acctID}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long acctID) throws NoDataException {
        Account account = accountService.getAccountById(acctID);
        if (Objects.isNull(account)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(account);
    }

    @ApiOperation(value = "Create new account for existing customer")
    @PutMapping(value = "/createAccount", consumes = "application/json", produces = {"application/json", "text/xml"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> createAccount(@RequestBody @Valid AccountDto accountDto) throws NoDataException {
        inputValidatorUtil.nullCheck(accountDto);
        accountService.saveAccount(accountDto.getCustomerPrflId(), bankServiceApiHelper.accountBuilder(accountDto));
        return new ResponseEntity<>(new StatusResponse("Account Created Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get available balance by account Id")
    @GetMapping(value = "/balance/{acctID}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable Long acctID) throws NoDataException {
        Account account = accountService.getAccountById(acctID);
        if (Objects.isNull(account)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(bankServiceApiHelper.accountBalanceBuilder(account));
    }

    @ApiOperation(value = "Deposit amount to an account")
    @PutMapping(value = "/{acctID}/deposit/{amount}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> depositAmt(@PathVariable Long acctID, @PathVariable int amount) throws NoDataException {
        BalanceDto balanceDto = getBalance(acctID).getBody();
        if (Objects.isNull(balanceDto)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal initialBalance = balanceDto.getCurrentBalance();
        int totalBalance = initialBalance.intValue() + amount;
        accountService.deposit(acctID, amount);
        return new ResponseEntity<>(new StatusResponse("Amount " + amount + " is deposited to AccountId " + acctID + " and total current balance is: " + totalBalance), HttpStatus.OK);
    }

    @ApiOperation(value = "Withdraw amount from an account")
    @PutMapping(value = "/{acctID}/withdraw/{amount}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> withdrawAmt(@PathVariable Long acctID, @PathVariable int amount) throws NoDataException, BankServiceApiException {
        int totalBalance;
        BalanceDto balanceDto = getBalance(acctID).getBody();
        if (Objects.isNull(balanceDto)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal initialBalance = balanceDto.getCurrentBalance();
        totalBalance = initialBalance.intValue() - amount;
        accountService.withdraw(acctID, amount, initialBalance.intValue());
        return new ResponseEntity<>(new StatusResponse("Amount " + amount + " is withdrawn from AccountId " + acctID + " and Total Balance is: " + totalBalance), HttpStatus.OK);
    }

    @ApiOperation(value = "Transfer funds between two accounts")
    @PutMapping(value = "/{srcAcctId}/transfer/{destAcctId}/{amount}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> transferAmt(@PathVariable Long srcAcctId, @PathVariable Long destAcctId, @PathVariable int amount) throws NoDataException, BankServiceApiException {
        BalanceDto balanceDtoForSrcAcctId = getBalance(srcAcctId).getBody();
        BalanceDto balanceDtoForDestAcctId = getBalance(destAcctId).getBody();
        if (Objects.isNull(balanceDtoForSrcAcctId) || Objects.isNull(balanceDtoForDestAcctId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BigDecimal srcAcctTotalBalance = balanceDtoForSrcAcctId.getCurrentBalance();
        BigDecimal destAcctTotalBalance = balanceDtoForDestAcctId.getCurrentBalance();
        accountService.transfer(srcAcctId, destAcctId, amount, srcAcctTotalBalance.intValue(), destAcctTotalBalance.intValue());
        return new ResponseEntity<>(new StatusResponse("Amount " + amount + " is transferred from AccountId " + srcAcctId + " to Account Id " + destAcctId), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an account")
    @PutMapping(value = "/delete/{acctId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> deleteAcct(@PathVariable Long acctId) throws NoDataException, BankServiceApiException {
        BalanceDto balanceDto = getBalance(acctId).getBody();
        if (Objects.isNull(balanceDto)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long acctIdToDelete = balanceDto.getAccountId();
        accountService.delete(acctIdToDelete);
        return new ResponseEntity<>(new StatusResponse("Account " + acctId + " is deleted "), HttpStatus.OK);
    }
}
