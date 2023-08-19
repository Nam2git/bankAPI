package com.ws.tla.enums;

import org.junit.jupiter.api.Test;

import static com.ws.tla.enums.CardType.CREDIT_CARD;
import static com.ws.tla.enums.CardType.DEBIT_CARD;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTypeTest {

    @Test
    public void testCardTypeEnum() {
        assertEquals("CREDIT_CARD", CREDIT_CARD.name());
        assertEquals("DEBIT_CARD", DEBIT_CARD.name());
    }
}
