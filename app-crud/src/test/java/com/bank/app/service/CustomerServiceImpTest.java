package com.bank.app.service;

import com.bank.app.Customer;
import com.bank.app.exceptions.ResourceNotFoundException;
import com.bank.app.payload.CustomerDTO;
import com.bank.app.payload.CustomerResponse;
import com.bank.app.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerServiceImp Tests")
class CustomerServiceImpTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerServiceImp customerService;

    private CustomerDTO customerDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO(null, "John", "Doe", "john@example.com", "555-0101");
        customer = new Customer(1L, "John", "Doe", "john@example.com", "555-0101");
    }

    @Test
    @DisplayName("Should create customer when valid data is provided")
    void testCreateCustomer_Success() {
        when(customerRepository.findByFirstName("John")).thenReturn(null);
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when customer already exists")
    void testCreateCustomer_DuplicateCustomer() {
        when(customerRepository.findByFirstName("John")).thenReturn(customer);
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);

        assertThatThrownBy(() -> customerService.createCustomer(customerDTO))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("already exists");

        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve all customers with pagination")
    void testGetAllCustomers_Success() {
        Customer customer2 = new Customer(2L, "Jane", "Smith", "jane@example.com", "555-0102");
        Page<Customer> customerPage = new PageImpl<>(List.of(customer, customer2));

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(customerDTO);
        when(modelMapper.map(customer2, CustomerDTO.class)).thenReturn(
            new CustomerDTO(2L, "Jane", "Smith", "jane@example.com", "555-0102")
        );

        CustomerResponse result = customerService.getAllCustomers(0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
        verify(customerRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should throw exception when no customers found")
    void testGetAllCustomers_Empty() {
        Page<Customer> emptyPage = new PageImpl<>(List.of());

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        assertThatThrownBy(() -> customerService.getAllCustomers(0, 10))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should retrieve customer by id")
    void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(customerDTO);

        CustomerDTO result = customerService.getCustomerById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when customer not found by id")
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(999L))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should update existing customer")
    void testUpdateCustomer_Success() {
        CustomerDTO updateDTO = new CustomerDTO(null, "John", "Smith", "john.smith@example.com", "555-0111");
        Customer existingCustomer = new Customer(1L, "John", "Doe", "john@example.com", "555-0101");
        Customer updatedCustomer = new Customer(1L, "John", "Smith", "john.smith@example.com", "555-0111");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(modelMapper.map(updatedCustomer, CustomerDTO.class)).thenReturn(
            new CustomerDTO(1L, "John", "Smith", "john.smith@example.com", "555-0111")
        );

        CustomerDTO result = customerService.updateCustomer(updateDTO, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("Smith");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent customer")
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(customerDTO, 999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Customer");
    }

    @Test
    @DisplayName("Should delete customer successfully")
    void testDeleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(customerDTO);

        CustomerDTO result = customerService.deleteCustomer(1L);

        assertThat(result).isNotNull();
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent customer")
    void testDeleteCustomer_NotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteCustomer(999L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Customer");

        verify(customerRepository, never()).delete(any());
    }
}
