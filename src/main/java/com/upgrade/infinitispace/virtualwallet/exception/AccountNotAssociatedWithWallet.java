package com.upgrade.infinitispace.virtualwallet.exception;

public class AccountNotAssociatedWithWallet extends Exception {

    public AccountNotAssociatedWithWallet(int walletId, int accountId) {
        super("Wallet with walledId : "+walletId+" is not associated with accountId : "+accountId);
    }
}
