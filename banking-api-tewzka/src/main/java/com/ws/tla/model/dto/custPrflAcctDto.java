package com.ws.tla.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class custPrflAcctDto {

    private BigDecimal currentBalance;
    private CardDto card;
}
