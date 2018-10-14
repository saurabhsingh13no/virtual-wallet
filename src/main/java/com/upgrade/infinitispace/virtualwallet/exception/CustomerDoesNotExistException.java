package com.upgrade.infinitispace.virtualwallet.exception;

import com.upgrade.infinitispace.virtualwallet.models.Customer;

public class CustomerDoesNotExistException extends Exception {

    public CustomerDoesNotExistException(Integer customerId) {
        super("Customer with customer ID:" + customerId + " does not exist");
    }
}
