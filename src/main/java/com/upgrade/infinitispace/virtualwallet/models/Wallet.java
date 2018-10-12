package com.upgrade.infinitispace.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Wallet implements Serializable {

    @Id //to set as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to set as autoincrement
    private int walletId;

    @OneToMany(mappedBy = "walletNumber")
    @JsonIgnore
    private List<Account> accountsInWallet;

    @OneToOne
    @JsonIgnore
    private int customerId;

    private static final long serialVersionUID = 1L;

    public Wallet() {
    }


    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public List<Account> getAccountsInWallet() {
        return accountsInWallet;
    }

    public void setAccountsInWallet(List<Account> accountsInWallet) {
        this.accountsInWallet = accountsInWallet;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
