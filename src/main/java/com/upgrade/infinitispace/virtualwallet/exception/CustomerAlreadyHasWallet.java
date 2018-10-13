package com.upgrade.infinitispace.virtualwallet.exception;

import com.upgrade.infinitispace.virtualwallet.models.Customer;

public class CustomerAlreadyHasWallet extends Exception {
    public CustomerAlreadyHasWallet(Customer customer) {
        super("Customer "+customer.getFname()+" "+customer.getLname()+" already owns a wallet : "+customer.getWallet().getWalletId());
    }
}
