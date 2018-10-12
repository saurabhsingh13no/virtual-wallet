package com.upgrade.infinitispace.virtualwallet.service;

import com.upgrade.infinitispace.virtualwallet.exception.CustomerAlreadyHasWallet;
import com.upgrade.infinitispace.virtualwallet.models.Account;
import com.upgrade.infinitispace.virtualwallet.models.Customer;
import com.upgrade.infinitispace.virtualwallet.models.Wallet;
import com.upgrade.infinitispace.virtualwallet.repository.AccountRepository;
import com.upgrade.infinitispace.virtualwallet.repository.CustomerRepository;
import com.upgrade.infinitispace.virtualwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WalletService {

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;


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

    // Return current account balance - balance in wallet (wallet can have multiple accounts
    @GetMapping("/api/wallet/{walletId}/balance")
    public float getAccountBalance(
            @PathVariable("walletId") int walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        if (wallet!=null) {
            List<Account> accounts = wallet.getAccountsInWallet();
            float balance = 0.0f;
            for (Account account : accounts) {
                balance += account.getBalance();
            }
            return balance;
        }
        else return 0.0f;
    }



}
