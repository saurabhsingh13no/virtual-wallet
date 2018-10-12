package com.upgrade.infinitispace.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Customer implements Serializable {

    @Id //to set as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to set as autoincrement
    private int userId;
    private String fname;
    private String lname;
    private String email;

    @OneToOne(mappedBy = "walletOfCustomer")
    @JsonIgnore
    private Wallet wallet;

    @OneToMany(mappedBy = "accountHolder")
    private List<Account> customerAccounts;

    private static final long serialVersionUID = 1L;

    public Customer() {
        super();
    }

    public Customer(String fname, String lname, String email) {
        super();
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Account> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(List<Account> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }
}
