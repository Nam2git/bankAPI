package com.ws.tla.service;

import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.NoDataException;

import javax.validation.Valid;
import java.util.List;

public interface ICustomerProfileService {
    CustomerProfile getCustomerPrflInfoById(Long accId) throws NoDataException;

    Object[] getCustomerPrflAndAcctById(Long id) throws NoDataException;

    List<CustomerProfile> getCustomersPrflList();

    void saveCustomerPrfl(@Valid CustomerProfile customerProfile) throws NoDataException;

    void deleteCustPrfl(Long custId) throws BankServiceApiException;
}
