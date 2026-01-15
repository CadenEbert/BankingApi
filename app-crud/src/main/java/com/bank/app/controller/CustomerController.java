package com.bank.app.controller;

import com.bank.app.Customer;
import com.bank.app.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {


    private CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/admin/customers")
    public String createCustomer(@RequestBody Customer customer) {
        customerService.createCustomer(customer);
        return "Customer created successfully";
    }

    @GetMapping("/public/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PutMapping("/public/customers/{customerId}")
    public String updateCustomer(@RequestBody Customer customer, @PathVariable Long customerId) {
        customerService.updateCustomer(customer,  customerId);
        return "Customer updated successfully";
    }

    @DeleteMapping("/admin/customers/{customerId}")
    public String deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return "Customer deleted successfully";
    }



}
