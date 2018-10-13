package com.upgrade.infinitispace.virtualwallet.service;


import com.upgrade.infinitispace.virtualwallet.models.Account;
import com.upgrade.infinitispace.virtualwallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/api/account")
    public List<Account> findAllAccounts() {
        return (List<Account>)accountRepository.findAll();
    }

    @GetMapping("/api/account/{accountNumber}")
    public Account findAccountByNumber(
            @PathVariable("accountNumber") int accountNumber) {
        return accountRepository.findById(accountNumber).orElse(null);
    }

    @PostMapping("/api/account")
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }




}
