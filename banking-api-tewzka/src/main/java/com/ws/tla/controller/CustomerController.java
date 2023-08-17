package com.ws.tla.controller;

import com.ws.tla.entity.CustomerProfile;
import com.ws.tla.exception.BankServiceApiException;
import com.ws.tla.exception.ErrorResponse;
import com.ws.tla.exception.NoDataException;
import com.ws.tla.model.dto.CustomerDto;
import com.ws.tla.model.dto.StatusResponse;
import com.ws.tla.service.IAccountService;
import com.ws.tla.service.ICustomerProfileService;
import com.ws.tla.service.helper.BankServiceApiHelper;
import com.ws.tla.util.InputValidatorUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v0/banksvc/customer")
public class CustomerController {
    @Autowired
    private ICustomerProfileService customerProfileService;

    @Autowired
    private InputValidatorUtil inputValidatorUtil;

    @Autowired
    private IAccountService accountService;

    @ApiOperation(value = "Get list of Customer profiles")
    @GetMapping(value = "/customerslist")
    public ResponseEntity<List<Object[]>> getAllCustomers() {
        return ResponseEntity.ok().body(customerProfileService.getCustomersPrflList());

    }

    @ApiOperation(value = "Get Customer by CustPrflId")
    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerProfile> getCustomerPrflsById(@PathVariable(name = "customerId") Long custPrflID) throws NoDataException {
        CustomerProfile customerProfile = customerProfileService.getCustomerPrflInfoById(custPrflID);
        if (Objects.isNull(customerProfile)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(customerProfile);
    }

    @ApiOperation(value = "Get single Customer and Account Info by CustPrflId")
    @GetMapping(value = "/getAccts/{customerId}")
    public ResponseEntity<List<Object[]>> getCustomerPrflAndAcctInfoById(@PathVariable(name = "customerId") Long custPrflID) throws NoDataException {
        return ResponseEntity.ok().body(customerProfileService.getCustomerPrflAndAcctById(custPrflID));
    }

    @ApiOperation(value = "Create new customer")
    @PutMapping(value = "/createCustomer", consumes = "application/json", produces = {"application/json", "text/xml"})
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> createCustomer(@RequestBody @Valid CustomerDto customerDto) throws NoDataException {
        inputValidatorUtil.checkNotNull(customerDto);
        customerProfileService.saveCustomerPrfl(BankServiceApiHelper.customerPrflBuilder(customerDto));
        return new ResponseEntity<>(new StatusResponse("Customer Profile Created Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a customer")
    @PutMapping(value = "/delete/{customerId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ok", response = StatusResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or Bad Request", response = ErrorResponse.class),
            @ApiResponse(code = 408, message = "Request timed out", response = ErrorResponse.class)})
    public ResponseEntity<StatusResponse> deleteCustomer(@PathVariable Long customerId) throws NoDataException, BankServiceApiException {
        customerProfileService.delete(customerId);
        return new ResponseEntity<>(new StatusResponse("Customer Id: " + customerId + " is deleted "), HttpStatus.OK);
    }

}


