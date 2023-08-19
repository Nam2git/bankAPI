package com.ws.tla.controller;

import com.ws.tla.entity.Account;
import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.model.dto.*;
import com.ws.tla.repository.CustomerProfileRepository;
import com.ws.tla.service.ICustomerProfileService;
import com.ws.tla.service.helper.BankServiceApiHelper;
import com.ws.tla.util.InputValidatorUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ws.tla.enums.CardType.CREDIT_CARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerControllerTest {
    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private ICustomerProfileService customerProfileService;

    @Mock
    private InputValidatorUtil inputValidatorUtil;

    @Mock
    private BankServiceApiHelper bankServiceApiHelper;

    @Before
    public void Setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testGetAllCustomers() {
        List<CustomerProfile> customerProfiles = new ArrayList<>();
        customerProfiles.add(getCustomerDetails());
        when(customerProfileService.getCustomersPrflList()).thenReturn(customerProfiles);
        ResponseEntity<List<CustomerProfile>> actualResponse = customerController.getAllCustomers();
        assertNotNull(actualResponse);
    }

    @Test
    public void testGetCustomerPrflsById() throws NoDataException {
        when(customerProfileRepository.findById(any(Long.class))).thenReturn(Optional.of(getCustomerDetails()));
        when(customerProfileService.getCustomerPrflInfoById(any(Long.class))).thenReturn(getCustomerDetails());
        ResponseEntity<CustomerProfile> actualResponse = customerController.getCustomerPrflsById(111L);
        assertNotNull(actualResponse);
        assertEquals(111L, actualResponse.getBody().getCustProfileId());
    }

    @Test
    public void testGetCustomerPrflsByIdFailures() throws NoDataException {
        when(customerProfileRepository.findById(any(Long.class))).thenReturn(Optional.of(getCustomerDetails()));
        when(customerProfileService.getCustomerPrflInfoById(any(Long.class))).thenReturn(null);
        ResponseEntity<CustomerProfile> actualResponse = customerController.getCustomerPrflsById(111L);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }

    @Test
    public void testGetCustomerPrflAndAcctInfoById() throws NoDataException {
        when(customerProfileRepository.findCustPrflAcctAndCardInfo(any(Long.class))).thenReturn(new Object[] {getCustomerDetails()});
        when(customerProfileService.getCustomerPrflAndAcctById(any(Long.class))).thenReturn(new Object[] {getCustomerDetails()});
        ResponseEntity<Object[]> actualResponse = customerController.getCustomerPrflAndAcctInfoById(111L);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test()
    public void testCreateCustomer() throws NoDataException {
        when(bankServiceApiHelper.customerPrflBuilder(getCustomerDto())).thenReturn(getCustomerDetails());
        doNothing().when(inputValidatorUtil).nullCheck(any(Object.class));
        when(customerProfileService.getCustomerPrflInfoById(any(Long.class))).thenReturn(new CustomerProfile());
        when(customerProfileRepository.save(any(CustomerProfile.class))).thenReturn(getCustomerDetails());
        doNothing().when(customerProfileService).saveCustomerPrfl(any(CustomerProfile.class));
        ResponseEntity<StatusResponse> actualResponse = customerController.createCustomer(getCustomerDto());
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(new StatusResponse("Customer Profile Created Successfully"), actualResponse.getBody());
    }

    @Test
    public void testDeleteCustomer() throws NoDataException, BankServiceApiException {
        doNothing().when(customerProfileRepository).deleteById(any(Long.class));
        doNothing().when(customerProfileService).deleteCustPrfl(any(Long.class));
        ResponseEntity<StatusResponse> actualResponse = customerController.deleteCustomer(getCustomerDetails().getCustProfileId());
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(new StatusResponse("Customer Id: 111 is deleted "), actualResponse.getBody());
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

    private CustomerDto getCustomerDto() {
        CardDto cardDto = new CardDto();
        cardDto.setCardNumber("12345678");
        cardDto.setCardType(CREDIT_CARD);
        cardDto.setCvvNumber(1235L);
        cardDto.setAvailableCardLimit(1200);

        List<CustPrflAcctDto> custPrflAcctDtos = new ArrayList<>();
        CustPrflAcctDto custPrflAcctDto = new CustPrflAcctDto();
        custPrflAcctDto.setCard(cardDto);
        custPrflAcctDto.setCurrentBalance(BigDecimal.valueOf(1200L));
        custPrflAcctDtos.add(custPrflAcctDto);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCity("Chicago");
        customerDto.setFirstName("ABC");
        customerDto.setLastName("Test");
        customerDto.setPhoneNumber("122334455");
        customerDto.setCustPrflAcctDtos(custPrflAcctDtos);

        return customerDto;
    }
}
