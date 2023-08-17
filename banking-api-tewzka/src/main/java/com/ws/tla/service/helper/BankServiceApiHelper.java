package com.ws.tla.service.helper;

import com.ws.tla.constants.BankApiConstants;
import com.ws.tla.entity.Account;
import com.ws.tla.entity.Card;
import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.model.dto.AccountDto;
import com.ws.tla.model.dto.BalanceDto;
import com.ws.tla.model.dto.CustomerDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class BankServiceApiHelper {
    public static Account accountBuilder(AccountDto accountDto) {
        Account account = new Account();
        Card card = new Card();
        card.setCardNumber(accountDto.getCard().getCardNumber());
        card.setCardType(accountDto.getCard().getCardType());
        card.setCvvNumber(accountDto.getCard().getCvvNumber());
        card.setTotalLimit(accountDto.getCard().getAvailableCardLimit());
        account.setCard(card);
        account.setAcctNum(String.valueOf(new Random().nextInt(100000000)));
        account.setAcctStat(BankApiConstants.ACTIVE_ACCT);
        account.setBalance((long) accountDto.getCurrentBalance().intValue());
        return account;
    }

    public static BalanceDto accountBalanceBuilder(Account account) {
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setCurrentBalance(BigDecimal.valueOf(account.getBalance()));
        balanceDto.setAccountId(account.getAcctId());
        return balanceDto;
    }

    public static CustomerProfile customerPrflBuilder(CustomerDto customerDto) {
        CustomerProfile customerProfile = new CustomerProfile();
        List<Account> accountList = new ArrayList<>();
        customerDto.getCustPrflAcctDtos().forEach(acct -> {
            Account account = new Account();
            Card card = new Card();
            card.setCardNumber(acct.getCard().getCardNumber());
            card.setCardType(acct.getCard().getCardType());
            card.setCvvNumber(acct.getCard().getCvvNumber());
            card.setTotalLimit(acct.getCard().getAvailableCardLimit());
            account.setCard(card);
            account.setAcctNum(String.valueOf(new Random().nextInt(100000000)));
            account.setAcctStat(BankApiConstants.ACTIVE_ACCT);
            account.setBalance((long) acct.getCurrentBalance().intValue());
            accountList.add(account);
        });
        customerProfile.setFirstname(customerDto.getFirstName());
        customerProfile.setLastname(customerDto.getLastName());
        customerProfile.setCity(customerDto.getCity());
        customerProfile.setPhone(String.valueOf(Long.valueOf(customerDto.getPhoneNumber())));
        customerProfile.setAccounts(accountList);
        return customerProfile;
    }

}
