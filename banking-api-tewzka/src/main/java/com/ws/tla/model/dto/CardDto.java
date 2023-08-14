package com.ws.tla.model.dto;

import com.ws.tla.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private CardType cardType;
    private String cardNumber;
    private Long cvvNumber;
    private Long availableCardLimit;
}
