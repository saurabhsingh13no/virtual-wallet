package com.upgrade.infinitispace.virtualwallet.service;

import com.upgrade.infinitispace.virtualwallet.comparator.BankTransactionSortingComparator;
import com.upgrade.infinitispace.virtualwallet.constant.Constants;
import com.upgrade.infinitispace.virtualwallet.exception.*;
import com.upgrade.infinitispace.virtualwallet.models.Account;
import com.upgrade.infinitispace.virtualwallet.models.BankTransaction;
import com.upgrade.infinitispace.virtualwallet.models.Customer;
import com.upgrade.infinitispace.virtualwallet.models.Wallet;
import com.upgrade.infinitispace.virtualwallet.repository.AccountRepository;
import com.upgrade.infinitispace.virtualwallet.repository.CustomerRepository;
import com.upgrade.infinitispace.virtualwallet.repository.WalletRepository;
import com.upgrade.infinitispace.virtualwallet.repository.BankTansactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class WalletService {

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BankTansactionRepository bankTansactionRepository;


    @GetMapping("/api/wallet")
    public List<Wallet> findAllWallets(){

        return (List<Wallet>)walletRepository.findAll();
    }

    @GetMapping("/api/wallet/{walletId}")
    public Wallet findWalletById(@PathVariable("walletId") int id) {

        return walletRepository.findById(id).orElse(null);
    }

    // Create a new wallet for a user. Constraint : A user can have only one wallet
    @PostMapping("/api/wallet")
    public Wallet createWallet(@RequestBody Wallet wallet) throws CustomerAlreadyHasWallet{

        Customer customer = wallet.getWalletOfCustomer();
        Customer existingCustomer = ((List<Customer>)customerRepository.findCustomerByEmail(customer.getEmail())).get(0);

        // Customer already has a wallet
        if (existingCustomer.getWallet()!=null) {
            throw new CustomerAlreadyHasWallet(existingCustomer);
        }

        return walletRepository.save(wallet);
    }

    // Return all account listed
    @GetMapping("/api/wallet/{walletId}/accounts")
    public List<Account> findAccountsListed(
            @PathVariable("walletId") int walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        return wallet.getAccountsInWallet();
    }

    // Return current account balance - balance in wallet (wallet can have multiple accounts)
    @GetMapping("/api/wallet/{walletId}/account/{accountId}/balance")
    public float getAccountBalance (
            @PathVariable("walletId") int walletId,
            @PathVariable("accountId") int accountId) throws WalletIdDoesNotExist, AccountNotAssociatedWithWallet {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);

        // handle walletId does not exist
        if (wallet==null) {
            throw new WalletIdDoesNotExist(walletId);
        }
        else {
            List<Account> accounts = wallet.getAccountsInWallet();
            float balance = 0.0f;

            // handle: account is not linked to wallet
            List<Integer> associatedAccounts = new ArrayList<>();
            for (Account account : wallet.getAccountsInWallet()) {
                associatedAccounts.add(account.getAccountNumber());
            }
            if (!associatedAccounts.contains(accountId)) {
                throw new AccountNotAssociatedWithWallet(walletId, accountId);
            }

            for (Account account : accounts) {
                if (account.getAccountNumber()==accountId) balance += account.getBalance();
            }
            return balance;
        }
    }

    @PostMapping("/api/wallet/{walletId}/account/{accountId}/withdraw/{amount}")
    public float withdraw(
            @PathVariable("walletId") int walletId,
            @PathVariable("accountId") int accountId,
            @PathVariable("amount") float amount) throws WalletIdDoesNotExist,
            InsufficientBalanceInWallet, AccountNotAssociatedWithWallet {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);

        handleWalletAccountException(wallet, walletId, accountId);

        // handle account in wallet does not sufficient balance
        if (getAccountBalance(walletId, accountId) <= amount) {
            throw new InsufficientBalanceInWallet(walletId);
        }

        // withdraw amount
        Account associateAccount = ((List<Account>) accountRepository.findAccountByNumber(accountId)).get(0);
        float currentBalance = associateAccount.getBalance();
        associateAccount.setBalance(currentBalance - amount);
        accountRepository.save(associateAccount);

        // Make Entry in Transaction table
        BankTransaction bankTransaction = new BankTransaction(Constants.WITHDRAW, new Date(), amount, currentBalance - amount, Constants.WITHDRAW_DESCRIPTION, associateAccount);
        bankTansactionRepository.save(bankTransaction);

        return amount;
    }

    // Handle exception if :
    //   a) Wallet does not exist
    //   b) Account is not linked to wallet
    private void handleWalletAccountException(Wallet wallet, int walletId, int accountId) throws WalletIdDoesNotExist,
            AccountNotAssociatedWithWallet {
        // handle: wallet does not exist
        if (wallet == null) {
            throw new WalletIdDoesNotExist(walletId);
        }

        // handle: account is not linked to wallet
        List<Integer> associatedAccounts = new ArrayList<>();
        for (Account account : wallet.getAccountsInWallet()) {
            associatedAccounts.add(account.getAccountNumber());
        }
        if (!associatedAccounts.contains(accountId)) {
            throw new AccountNotAssociatedWithWallet(walletId, accountId);
        }
    }

    @PostMapping("/api/wallet/{walletId}/account/{accountId}/deposit/{amount}")
    public float deposit(@PathVariable("walletId") int walletId,
                         @PathVariable("accountId") int accountId,
                         @PathVariable("amount") float amount) throws WalletIdDoesNotExist, AccountNotAssociatedWithWallet {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);

        handleWalletAccountException(wallet, walletId, accountId);

        // deposit amount
        Account associateAccount = ((List<Account>) accountRepository.findAccountByNumber(accountId)).get(0);
        float currentBalance = associateAccount.getBalance();
        associateAccount.setBalance(currentBalance + amount);
        accountRepository.save(associateAccount);

        // Make Entry in Transaction table
        BankTransaction bankTransaction = new BankTransaction(Constants.DEPOSIT, new Date(), amount, currentBalance + amount, Constants.DEPOSIT_DESCRIPTION, associateAccount);
        bankTansactionRepository.save(bankTransaction);

        return amount;
    }

    @PostMapping("/api/wallet/{walletId}/account/{trasferFromAccountId}/transfer/wallet/{toWalletId}/account/{transferToAccountId}/amount/{amount}")
    public float transfer(@PathVariable ("walletId") int walletId,
                          @PathVariable ("trasferFromAccountId") int trasferFromAccountId,
                          @PathVariable ("toWalletId") int toWalletId,
                          @PathVariable ("transferToAccountId") int transferToAccountId,
                          @PathVariable ("amount") float amount) throws WalletIdDoesNotExist,
            AccountNotAssociatedWithWallet, InsufficientBalanceInWallet{

        withdraw(walletId, trasferFromAccountId, amount);

        deposit(toWalletId, transferToAccountId, amount);

        return amount;
    }

    @GetMapping("/api/wallet/{walletId}/account/{accountId}/lastNTransactions/{n}")
    public List<BankTransaction> getStatement(@PathVariable ("walletId") int walletId,
                                        @PathVariable ("accountId") int accountId,
                                        @PathVariable ("n") int n)
        throws  WalletIdDoesNotExist, AccountNotAssociatedWithWallet {

        Wallet wallet = walletRepository.findById(walletId).orElse(null);

        handleWalletAccountException(wallet, walletId, accountId);

        List<BankTransaction> bankTransactions = ((List<Account>)wallet.getAccountsInWallet()).get(0).getBankTransactions();

        Collections.sort(bankTransactions, new BankTransactionSortingComparator());

        return bankTransactions.subList(0, n);
    }





}
