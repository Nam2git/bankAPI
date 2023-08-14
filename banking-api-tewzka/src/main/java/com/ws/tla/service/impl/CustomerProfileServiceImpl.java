package com.ws.tla.service.impl;

import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.repository.CustomerProfileRepository;
import com.ws.tla.service.ICustomerProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomerProfileServiceImpl implements ICustomerProfileService {

    @Autowired
    CustomerProfileRepository customerProfileRepository;

    @Override
    public CustomerProfile getCustomerPrflInfoById(Long custPrflId) throws NoDataException {
        log.info("Getting CustomerPrflInfo for CustPrflId{}", custPrflId);
        Optional<CustomerProfile> customerProfile = Optional.of(new CustomerProfile());
        if (custPrflId != null) {
            customerProfile = customerProfileRepository.findById(custPrflId);
        } else {
            log.error("No CustomerProfile info found as CustPrflId is null");
            return null;
        }
        return customerProfile
                .orElseThrow(() -> new NoDataException("No CustomerProfile info found"));
    }

}
