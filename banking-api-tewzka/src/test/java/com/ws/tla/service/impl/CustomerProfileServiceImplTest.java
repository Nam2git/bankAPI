package com.ws.tla.service.impl;

import com.ws.tla.entity.Account;
import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.repository.CustomerProfileRepository;
import com.ws.tla.util.InputValidatorUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerProfileServiceImplTest {

    @InjectMocks
    private CustomerProfileServiceImpl customerProfileServiceImpl;

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private InputValidatorUtil inputValidatorUtil;

    @Before
    public void Setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCustomerPrflInfoById() throws NoDataException {
        when(customerProfileRepository.findById(any(Long.class))).thenReturn(Optional.of(getCustomerDetails()));
        CustomerProfile actualResponse = customerProfileServiceImpl.getCustomerPrflInfoById(any(Long.class));
        assertNotNull(actualResponse);
        verify(customerProfileRepository, Mockito.times(1)).findById(any(Long.class));

    }

    @Test
    public void testGetCustomerPrflAndAcctById() {
        when(customerProfileRepository.findCustPrflAcctAndCardInfo(getCustomerDetails().getCustProfileId())).thenReturn(new Object[] {getCustomerDetails()});
        Object[] actualResponse = customerProfileServiceImpl.getCustomerPrflAndAcctById(getCustomerDetails().getCustProfileId());
        verify(customerProfileRepository, Mockito.times(1)).findCustPrflAcctAndCardInfo(getCustomerDetails().getCustProfileId());
    }

    @Test
    public void testGetCustomersPrflList() {
        when(customerProfileRepository.findAllCustsPrflsAcctsAndCardInfo()).thenReturn(new Object[]{getCustomerDetails()});
        customerProfileServiceImpl.getCustomersPrflList();
        verify(customerProfileRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testSaveCustomerPrfl() throws NoDataException {
        CustomerProfile customerProfile = getCustomerDetails();
        doNothing().when(inputValidatorUtil).nullCheck(any(Object.class));
        doReturn(customerProfile).when(customerProfileRepository).save(customerProfile);
        customerProfileServiceImpl.saveCustomerPrfl(customerProfile);
        verify(customerProfileRepository, Mockito.times(1)).save(customerProfile);
    }

    @Test
    public void testDeleteCustPrfl() {
        CustomerProfile customerProfile = getCustomerDetails();
        doNothing().when(customerProfileRepository).deleteById(customerProfile.getCustProfileId());
        customerProfileServiceImpl.deleteCustPrfl(customerProfile.getCustProfileId());
        verify(customerProfileRepository, Mockito.times(1)).deleteById(customerProfile.getCustProfileId());
    }

    private CustomerProfile getCustomerDetails() {
        List<Account> accountList = new ArrayList<>();

        Account account = new Account();
        account.setAcctId(123L);
        accountList.add(account);

        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setCustProfileId(111L);
        customerProfile.setPhone("12445566");
        customerProfile.setAccounts(accountList);
        customerProfile.setCity("Chicago");
        customerProfile.setFirstname("ABC");
        customerProfile.setLastname("Test");
        return customerProfile;
    }
}
