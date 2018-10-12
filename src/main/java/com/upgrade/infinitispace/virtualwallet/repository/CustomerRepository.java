package com.upgrade.infinitispace.virtualwallet.repository;

import com.upgrade.infinitispace.virtualwallet.models.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email=:email")
    Iterable<Customer> findCustomerByEmal(@Param("email") String email);


}
