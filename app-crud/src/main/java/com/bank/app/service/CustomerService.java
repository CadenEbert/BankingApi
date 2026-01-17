package com.bank.app.service;

import com.bank.app.Customer;
import com.bank.app.payload.CustomerDTO;
import com.bank.app.payload.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerResponse getAllCustomers(Integer page, Integer size);
    CustomerDTO getCustomerById(Long id);
    CustomerDTO updateCustomer(CustomerDTO customerDTO, Long id);
    CustomerDTO deleteCustomer(Long id);

}
