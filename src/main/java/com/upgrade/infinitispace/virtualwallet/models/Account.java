package com.upgrade.infinitispace.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Account implements Serializable {

    @Id //to set as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to set as autoincrement
    private int accountNumber;
    private float balance;

    @ManyToOne
    //@JsonIgnore
    private Customer accountHolder;

    @ManyToOne
    @JsonIgnore
    private Wallet walletHolder;

    @OneToMany(mappedBy = "transactionFromAccount")
    private List<BankTransaction> bankTransactions;

    private static final long serialVersionUID = 1L;

    public Account() {
    }


    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Customer getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(Customer accountHolder) {
        this.accountHolder = accountHolder;
    }

    public List<BankTransaction> getBankTransactions() {
        return bankTransactions;
    }

    public void setBankTransactions(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public Wallet getWalletHolder() {
        return walletHolder;
    }

    public void setWalletHolder(Wallet walletHolder) {
        this.walletHolder = walletHolder;
    }
}
