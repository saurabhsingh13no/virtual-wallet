package com.upgrade.infinitispace.virtualwallet.controller;


import com.upgrade.infinitispace.virtualwallet.models.Account;
import com.upgrade.infinitispace.virtualwallet.models.Customer;
import com.upgrade.infinitispace.virtualwallet.repository.AccountRepository;
import com.upgrade.infinitispace.virtualwallet.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

/*    @GetMapping("/api/account")
    public List<Account> findAllAccounts() {
        return (List<Account>)accountRepository.findAll();
    }*/

    /*@GetMapping("/api/account/{accountNumber}")
    public Account findAccountByNumber(
            @PathVariable("accountNumber") int accountNumber) {
        return accountRepository.findById(accountNumber).orElse(null);
    }*/

    @PostMapping("/api/account")
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

}
