package com.upgrade.infinitispace.virtualwallet.exception;

import com.upgrade.infinitispace.virtualwallet.models.Customer;

public class CustomerAlreadyHasWalletException extends Exception {
    public CustomerAlreadyHasWalletException(Customer customer) {
        super("Customer "+customer.getFname()+" "+customer.getLname()+" already owns a wallet : "+customer.getWallet().getWalletId());
    }
}
