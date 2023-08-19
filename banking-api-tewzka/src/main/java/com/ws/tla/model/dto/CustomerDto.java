package com.ws.tla.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String firstName;
    private String lastName;
    private String city;
    private String phoneNumber;
    private List<CustPrflAcctDto> custPrflAcctDtos;
}
