package com.ws.tla.service.helper;

import com.ws.tla.constants.BankApiConstants;
import com.ws.tla.entity.Account;
import com.ws.tla.entity.Card;
import com.ws.tla.model.dto.AccountDto;
import com.ws.tla.model.dto.BalanceDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        account.setBalance(accountDto.getCurrentBalance().intValue());
        return account;
    }

    public static BalanceDto accountBalanceBuilder(Account account) {
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setCurrentBalance(BigDecimal.valueOf(account.getBalance()));
        balanceDto.setAccountId(account.getAcctId());
        return balanceDto;
    }
}
