package com.bank.app.service;

import com.bank.app.Customer;
import java.util.List;

public interface CustomerService {
    void createCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    String updateCustomer(Customer customer, Long id);
    String deleteCustomer(Long id);

}
