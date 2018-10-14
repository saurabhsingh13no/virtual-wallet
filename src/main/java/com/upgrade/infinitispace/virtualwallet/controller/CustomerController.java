package com.upgrade.infinitispace.virtualwallet.controller;


import com.upgrade.infinitispace.virtualwallet.models.Customer;
import com.upgrade.infinitispace.virtualwallet.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/api/customer")
    public List<Customer> findAllCustomers(
            @RequestParam(name="email",required=false) String email) {

        if (email!=null) {
            return (List<Customer>)customerRepository.findCustomerByEmail(email);
        }

        return (List<Customer>)customerRepository.findAll();
    }

    @GetMapping("/api/customer/{userId}")
    public Customer findCustomerById(@PathVariable("userId") int id) {

        return customerRepository.findById(id).orElse(null);
    }


    @PostMapping("/api/customer")
    public Customer createCustomer(@RequestBody Customer customer) {

        return customerRepository.save(customer);
    }

}
