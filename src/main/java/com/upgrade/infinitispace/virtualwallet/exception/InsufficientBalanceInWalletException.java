package com.upgrade.infinitispace.virtualwallet.exception;

public class InsufficientBalanceInWalletException extends Exception {

    public InsufficientBalanceInWalletException(int walletId) {
        super("Wallet with walletId : "+walletId+" does not have sufficient balance");
    }
}
