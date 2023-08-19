package com.ws.tla.enums;

public enum CardType {
    DEBIT_CARD("DEBIT_CARD"),
    CREDIT_CARD("CREDIT_CARD");

    private final String cardType;

    private CardType(String cardType) {
        this.cardType = cardType;
    }
}
