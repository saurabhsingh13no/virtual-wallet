package com.upgrade.infinitispace.virtualwallet.exception;

public class InsufficientBalanceInWallet extends Exception {

    public InsufficientBalanceInWallet(int walletId) {
        super("Wallet with walletId : "+walletId+" does not have sufficient balance");
    }
}
