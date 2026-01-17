package com.bank.app.controller;

import com.bank.app.Customer;
import com.bank.app.payload.CustomerDTO;
import com.bank.app.payload.CustomerResponse;
import com.bank.app.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {


    @Autowired
    private CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/admin/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO newCustomerDTO = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(newCustomerDTO, HttpStatus.OK);
    }

    @GetMapping("/public/customers")
    public ResponseEntity<CustomerResponse> getAllCustomers(
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        CustomerResponse customerResponse = customerService.getAllCustomers(pageNumber, pageSize);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @PutMapping("/public/customers/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable Long customerId) {
        CustomerDTO savedCustomer = customerService.updateCustomer(customerDTO,  customerId);
        return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/admin/customers/{customerId}")
    public ResponseEntity<CustomerDTO> deleteCustomer(@PathVariable Long customerId) {
        CustomerDTO customerDTO = customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }



}
