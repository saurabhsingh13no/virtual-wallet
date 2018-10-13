package com.upgrade.infinitispace.virtualwallet.exception;

public class WalletIdDoesNotExist extends  Exception {
    public WalletIdDoesNotExist(int walletId) {
        super("Wallet with walletId : "+walletId+" does not exist");
    }
}
