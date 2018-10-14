package com.upgrade.infinitispace.virtualwallet.controller;

import com.upgrade.infinitispace.virtualwallet.comparator.BankTransactionSortingComparator;
import com.upgrade.infinitispace.virtualwallet.constant.Constants;
import com.upgrade.infinitispace.virtualwallet.exception.*;
import com.upgrade.infinitispace.virtualwallet.models.*;
import com.upgrade.infinitispace.virtualwallet.repository.AccountRepository;
import com.upgrade.infinitispace.virtualwallet.repository.CustomerRepository;
import com.upgrade.infinitispace.virtualwallet.repository.WalletRepository;
import com.upgrade.infinitispace.virtualwallet.repository.BankTansactionRepository;
import com.upgrade.infinitispace.virtualwallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class WalletController {

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BankTansactionRepository bankTansactionRepository;


    @Autowired
    WalletService walletService;


   /* @GetMapping("/api/wallet")
    public List<Wallet> findAllWallets(){

        return (List<Wallet>)walletRepository.findAll();
    }

    @GetMapping("/api/wallet/{walletId}")
    public Wallet findWalletById(@PathVariable("walletId") int id) {

        return walletRepository.findById(id).orElse(null);
    }*/

    // Create a new wallet for a user. Constraint : A user can have only one wallet
    @PostMapping("/api/wallet/{customerId}")
    public ResponseEntity<ServiceResponse> createWallet(@PathVariable("customerId") int customerId) throws CustomerAlreadyHasWalletException {

        ServiceResponse response = new ServiceResponse();

        try {
            Wallet newWallet = walletService.createWallet(customerId);
            response.setStatus("200");
            response.setDescription("Wallet created successfully!");
            response.setData(newWallet);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(CustomerDoesNotExistException e) {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch(CustomerAlreadyHasWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }

    }

    /*// Return all account listed
    @GetMapping("/api/wallet/{walletId}/accounts")
    public List<Account> findAccountsListed(
            @PathVariable("walletId") int walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElse(null);
        return wallet.getAccountsInWallet();
    }*/

    // Return current account balance - balance in wallet (wallet can have multiple accounts)
    @GetMapping("/api/wallet/{walletId}/account/{accountId}/balance")
    public  ResponseEntity<ServiceResponse>  getAccountBalance (
            @PathVariable("walletId") int walletId,
            @PathVariable("accountId") int accountId){



        ServiceResponse response = new ServiceResponse();

        try {
            Float balance = walletService.getAccountBalanceForCurrentWallet(walletId, accountId);
            response.setStatus("200");
            response.setDescription("Balance fetched successfully!!");
            response.setData(balance);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(WalletIdDoesNotExistException e) {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch(AccountNotAssociatedWithWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @PostMapping("/api/wallet/{walletId}/account/{accountId}/withdraw/{amount}")
    public ResponseEntity<ServiceResponse> withdraw(
            @PathVariable("walletId") int walletId,
            @PathVariable("accountId") int accountId,
            @PathVariable("amount") float amount) {

        ServiceResponse response = new ServiceResponse();

        try {
            Account ac = walletService.withdrawFromAccount(walletId, accountId, amount,"WITHDRAW");
            response.setStatus("200");
            response.setDescription("Amount "+ amount+ " withdrawn successfully!!");
            response.setData(ac);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(WalletIdDoesNotExistException e) {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch(AccountNotAssociatedWithWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
        catch(InsufficientBalanceInWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @PostMapping("/api/wallet/{walletId}/account/{accountId}/deposit/{amount}")
    public ResponseEntity<ServiceResponse> deposit(@PathVariable("walletId") int walletId,
                         @PathVariable("accountId") int accountId,
                         @PathVariable("amount") float amount) {


        ServiceResponse response = new ServiceResponse();

        try {
            Account ac = walletService.depositToAccount(walletId, accountId, amount, "DEPOSIT");
            response.setStatus("200");
            response.setDescription("Amount "+ amount+ " deposited successfully!!");
            response.setData(ac);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(WalletIdDoesNotExistException e) {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch(AccountNotAssociatedWithWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }



    /**
     * Method is used to make entry into BankTransaction table for the appropriate transaction - deposit, withdrawl or transfer
     * @param amount : Amount to be deposited || withdrawn || transferred
     * @param postBalance : Balance in account after transaction has occurred
     * @param description : Custom String description associated with deposit || withdrawl || transfer
     * @param associatedAccount : Account associated with the transaction
     */
    private void makeEntryInTransaction(String typeOfTransaction, float amount, float postBalance, String description, Account associatedAccount) {
        BankTransaction bankTransaction = new BankTransaction(typeOfTransaction, new Date(), amount, postBalance, description, associatedAccount);

        bankTansactionRepository.save(bankTransaction);
    }

    @PostMapping("/api/wallet/{fromWalletId}/account/{fromAccountId}/transfer/wallet/{toWalletId}/account/{toAccountId}/amount/{amount}")
    public ResponseEntity<ServiceResponse> transfer(@PathVariable ("fromWalletId") int fromWalletId,
                          @PathVariable ("fromAccountId") int fromAccountId,
                          @PathVariable ("toWalletId") int toWalletId,
                          @PathVariable ("toAccountId") int toAccountId,
                          @PathVariable ("amount") float amount) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException {

        ServiceResponse response = new ServiceResponse();

        try {
            walletService.transferToAccount(fromWalletId, fromAccountId,toWalletId, toAccountId, amount);
            response.setStatus("200");
            response.setDescription("Amount "+ amount+ " transferred successfully!!");
            response.setData(amount);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(WalletIdDoesNotExistException e) {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch(AccountNotAssociatedWithWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
        catch(InsufficientBalanceInWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }


    }

    @GetMapping("/api/wallet/{walletId}/account/{accountId}/lastNTransactions/{n}")
    public ResponseEntity<ServiceResponse> getStatement(@PathVariable ("walletId") int walletId,
                                        @PathVariable ("accountId") int accountId,
                                        @PathVariable ("n") int n) {

        ServiceResponse response = new ServiceResponse();

        try {
            List<BankTransaction> lb = walletService.getStatement(walletId, accountId, n);
            response.setStatus("200");
            response.setDescription("Statement fetched successfully!!");
            response.setData(lb);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch(WalletIdDoesNotExistException e) {
            response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch(AccountNotAssociatedWithWalletException e) {
            response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }

    }

}
