package com.ws.tla.service.impl;

import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.repository.CustomerProfileRepository;
import com.ws.tla.service.ICustomerProfileService;
import com.ws.tla.util.InputValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerProfileServiceImpl implements ICustomerProfileService {

    @Autowired
    CustomerProfileRepository customerProfileRepository;

    @Autowired
    private InputValidatorUtil inputValidatorUtil;

    @Override
    public CustomerProfile getCustomerPrflInfoById(Long custPrflId) throws NoDataException {
        log.info("Getting CustomerPrflInfo for CustPrflId{}", custPrflId);
        Optional<CustomerProfile> customerProfile;
        if (custPrflId != null) {
            customerProfile = customerProfileRepository.findById(custPrflId);
        } else {
            log.error("No CustomerProfile info found as CustPrflId is null");
            return null;
        }
        return customerProfile
                .orElseThrow(() -> new NoDataException("No CustomerProfile info found"));
    }

    @Override
    public Object[] getCustomerPrflAndAcctById(Long custPrflId) {
        log.info("Getting CustomerPrflInfo And AcctInfo for CustPrflId{}", custPrflId);
        return customerProfileRepository.findCustPrflAcctAndCardInfo(custPrflId);
    }

    @Override
    public List<CustomerProfile> getCustomersPrflList() {
        return customerProfileRepository.findAll();
    }

    @Override
    public void saveCustomerPrfl(@Valid CustomerProfile customerProfile) throws NoDataException {
        inputValidatorUtil.nullCheck(customerProfile);
        customerProfileRepository.save(customerProfile);
    }

    @Override
    public void deleteCustPrfl(Long custId) {
        customerProfileRepository.deleteById(custId);
    }

}
