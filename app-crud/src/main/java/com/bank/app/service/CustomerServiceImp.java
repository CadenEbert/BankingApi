package com.bank.app.service;

import com.bank.app.Customer;
import com.bank.app.exceptions.ResourceNotFoundException;
import com.bank.app.payload.CustomerDTO;
import com.bank.app.payload.CustomerResponse;
import com.bank.app.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        Customer customerFromDB = customerRepository.findByFirstName(customer.getFirstName());
        if (customerFromDB != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer already exists");
        }

        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    public CustomerResponse getAllCustomers(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<Customer> customers = customerPage.getContent();

        if (customers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class)).toList();

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setContent(customerDTOS);
        customerResponse.setPageNumber(page);
        customerResponse.setPageSize(size);
        customerResponse.setTotalElements(customerPage.getTotalElements());

        return customerResponse;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO, Long id) {
        Customer customerFromDB = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", id));
        customerFromDB.setFirstName(customerDTO.getFirstName());
        customerFromDB.setLastName(customerDTO.getLastName());
        customerFromDB.setEmail(customerDTO.getEmail());
        customerFromDB.setPhoneNumber(customerDTO.getPhoneNumber());
        customerFromDB = customerRepository.save(customerFromDB);



        return modelMapper.map(customerFromDB, CustomerDTO.class);
    }

    @Override
    public CustomerDTO deleteCustomer(Long id) {
        Customer savedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", id));
        customerRepository.delete(savedCustomer);

        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

}
