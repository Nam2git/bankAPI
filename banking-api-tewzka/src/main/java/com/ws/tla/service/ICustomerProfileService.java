package com.ws.tla.service;

import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.NoDataException;

public interface ICustomerProfileService {
    CustomerProfile getCustomerPrflInfoById(Long accId) throws NoDataException;
}
