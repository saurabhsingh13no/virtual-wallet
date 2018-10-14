package com.upgrade.infinitispace.virtualwallet.exception;

public class WalletIdDoesNotExistException extends  Exception {
    public WalletIdDoesNotExistException(int walletId) {
        super("Wallet with walletId : "+walletId+" does not exist");
    }
}
