package com.upgrade.infinitispace.virtualwallet.services;

import com.upgrade.infinitispace.virtualwallet.exception.*;
import com.upgrade.infinitispace.virtualwallet.models.Account;
import com.upgrade.infinitispace.virtualwallet.models.BankTransaction;
import com.upgrade.infinitispace.virtualwallet.models.Wallet;

import java.util.List;

public interface WalletService {

    /**
     * Create a new wallet for a user - I assume user is already in the system (in my case Customer table, in H2 database)
     * @param customerId : Customer which has requested creation of new wallet
     * @return : A newly created wallet object
     * @throws CustomerDoesNotExistException - if customer does not exist in the Customer table
     * @throws CustomerAlreadyHasWalletException - if customer already has a wallet. In our case one customer can only have one wallet
     */
    public Wallet createWallet(Integer customerId) throws CustomerDoesNotExistException, CustomerAlreadyHasWalletException;

    /**
     * Method to Return current account balance
     * @param walledId : Wallet from which balance is requested
     * @param accountId : Account associated with walletId from which balance is requested. Wallet can have multiple accounts
     * @return : Balance in Float
     * @throws WalletIdDoesNotExistException - if wallet does not exist
     * @throws AccountNotAssociatedWithWalletException - if account is not associated with the walletId parameter
     */
    public Float getAccountBalanceForCurrentWallet(Integer walledId, Integer accountId) throws WalletIdDoesNotExistException, AccountNotAssociatedWithWalletException;

    /**
     * A withdrawal transaction is one that reduces the account balance with the given transaction amount
     * @param walletId : Wallet from which withdrawal is to be made
     * @param accountId : account associated with above walletId from which withdrawal is to be made
     * @param amount : Amount to be withdrawn
     * @param type : Can be "DEPOSIT" || "WITHDRAW" || "TRANSFER"
     * @return Account details from which transaction was made
     * @throws WalletIdDoesNotExistException - if wallet does not exist
     * @throws AccountNotAssociatedWithWalletException - if account is not associated with the walletId parameter
     * @throws InsufficientBalanceInWalletException - if amount to be withdrawn > balance (Assumes you can also withdraw 1 cents)
     */
    public Account withdrawFromAccount(Integer walletId, Integer accountId, Float amount, String type) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException;


    /**
     * A deposit transaction is one that increases the account balance with the given transaction amount.
     * @param walletId : Wallet to which deposit is to be made
     * @param accountId : account associated with walletId to which amount is to be deposited
     * @param amount : Amount to be deposited
     * @param type : Can be "DEPOSIT" || "WITHDRAW" || "TRANSFER"
     * @return Account details from which transaction was made
     * @throws WalletIdDoesNotExistException - if wallet does not exist
     * @throws AccountNotAssociatedWithWalletException- if account is not associated with the walletId parameter
     */
    public Account depositToAccount(Integer walletId, Integer accountId, Float amount, String type) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException;


    /**
     * Method to Perform a transfer from one account to another account
     * @param fromWalletId : Wallet from which amount is to be transferred
     * @param fromAccountId : Account associated with fromWalletID from which amount is to transferred
     * @param toWalletId : Wallet to which amount is to transferred
     * @param toAccountId : Account associated with toWalletId to which amount is to be transferred
     * @param amount : Amount to be transferred
     * @throws WalletIdDoesNotExistException - if wallet does not exist
     * @throws AccountNotAssociatedWithWalletException - if account is not associated with the walletId parameter
     * @throws InsufficientBalanceInWalletException - if amount to be withdrawn > balance (Assumes you can also withdraw 1 cents)
     */
    public void transferToAccount(Integer fromWalletId, Integer fromAccountId, Integer toWalletId, Integer toAccountId, Float amount) throws WalletIdDoesNotExistException,
    AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException;

    /**
     * Method Return last N transactions for an account
     * @param walletId : Wallet from which statement balance is requested
     * @param accountId : Account associated with above walletId of which the transaction statement is requested
     * @param n : Number of transactions record requested
     * @return List of all bank transactions made
     * @throws WalletIdDoesNotExistException - if wallet does not exist
     * @throws AccountNotAssociatedWithWalletException - if account is not associated with the walletId parameter
     */
    public List<BankTransaction> getStatement(Integer walletId, Integer accountId, Integer n) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException;

}
