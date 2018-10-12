package com.upgrade.infinitispace.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upgrade.infinitispace.virtualwallet.models.Account;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class BankTransaction implements Serializable {

    @Id //to set as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to set as autoincrement
    private int transactionId;
    private String type;
    private Date timestamp;
    private float amount;
    private float postBalance;
    private String description;

    @ManyToOne
    @JsonIgnore
    private Account transactionFromAccount;

    public BankTransaction() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPostBalance() {
        return postBalance;
    }

    public void setPostBalance(float postBalance) {
        this.postBalance = postBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getTransactionFromAccount() {
        return transactionFromAccount;
    }

    public void setTransactionFromAccount(Account transactionFromAccount) {
        this.transactionFromAccount = transactionFromAccount;
    }
}
