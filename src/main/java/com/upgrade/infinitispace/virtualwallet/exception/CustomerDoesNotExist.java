package com.upgrade.infinitispace.virtualwallet.exception;

import com.upgrade.infinitispace.virtualwallet.models.Customer;

public class CustomerDoesNotExist extends Exception {

    public CustomerDoesNotExist(Customer customer) {
        super("Customer "+customer.getFname()+" "+customer.getLname()+" does not exist");
    }
}
