package com.upgrade.infinitispace.virtualwallet.exception;

import com.upgrade.infinitispace.virtualwallet.models.Customer;
import com.upgrade.infinitispace.virtualwallet.models.Wallet;

public class WalletDoesNotBelongToCustomer extends Exception {
    public WalletDoesNotBelongToCustomer(Customer customer, Wallet wallet) {
        super("Customer with id"+customer.getUserId()+" does not have associated walletId : "+wallet.getWalletId());
    }
}
