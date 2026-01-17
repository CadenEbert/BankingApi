package com.bank.app.repository;

import com.bank.app.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {


    Customer findByFirstName(String firstName);
}
