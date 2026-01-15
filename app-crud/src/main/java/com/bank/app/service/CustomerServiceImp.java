package com.bank.app.service;

import com.bank.app.Customer;
import com.bank.app.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public String updateCustomer(Customer customer, Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        Customer savedCustomer = customerOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        customer.setFirstName(savedCustomer.getFirstName());
        customer.setLastName(savedCustomer.getLastName());
        customer.setEmail(savedCustomer.getEmail());
        customer.setPhoneNumber(String.valueOf(savedCustomer.getPhoneNumber()));
        customerRepository.save(customer);
        return "Customer with id: " + id + " updated successfully";
    }

    @Override
    public String deleteCustomer(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            customerRepository.deleteById(id);
        }
        return "Customer with Id: " + id + " deleted";
    }

}
