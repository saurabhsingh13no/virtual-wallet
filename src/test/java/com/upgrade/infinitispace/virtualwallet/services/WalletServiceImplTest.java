package com.upgrade.infinitispace.virtualwallet.services;

import com.upgrade.infinitispace.virtualwallet.exception.AccountNotAssociatedWithWalletException;
import com.upgrade.infinitispace.virtualwallet.exception.CustomerAlreadyHasWalletException;
import com.upgrade.infinitispace.virtualwallet.exception.CustomerDoesNotExistException;
import com.upgrade.infinitispace.virtualwallet.exception.InsufficientBalanceInWalletException;
import com.upgrade.infinitispace.virtualwallet.models.Account;
import com.upgrade.infinitispace.virtualwallet.models.BankTransaction;
import com.upgrade.infinitispace.virtualwallet.models.Customer;
import com.upgrade.infinitispace.virtualwallet.models.Wallet;
import com.upgrade.infinitispace.virtualwallet.repository.AccountRepository;
import com.upgrade.infinitispace.virtualwallet.repository.BankTansactionRepository;
import com.upgrade.infinitispace.virtualwallet.repository.CustomerRepository;
import com.upgrade.infinitispace.virtualwallet.repository.WalletRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class WalletServiceImplTest {


    @TestConfiguration
    static class WalletServiceConfiguration {
        @Bean
        public WalletService walletService() {
            return new WalletServiceImpl();
        }
    }

    @Autowired
    private WalletService walletService;

    @MockBean
    WalletRepository walletRepoMock;
    @MockBean
    AccountRepository accountRepoMock;
    @MockBean
    CustomerRepository customerRepoMock;
    @MockBean
    BankTansactionRepository bankTansactionRepoMock;

    /**
     * Method test successful creation of wallet
     * @throws Exception : Exception thrown if wallet does not exist or customeralready has a wallet - not tested in this
     *                                                      method
     */
    @Test
    public void createWallet()throws Exception{

        Wallet w = new Wallet();
        w.setWalletId(1);

        Customer c = new Customer();
        c.setUserId(1);
        c.setEmail("saurabh@gmail.com");
        c.setFname("Saurabh");
        c.setLname("Singh");

        when(customerRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(c));
        when(walletRepoMock.save((Wallet)anyObject())).thenReturn(w);
        assertEquals(1, walletService.createWallet(123).getWalletId());
    }

    /**
     * Method tests behaviour of CustomerDoesNotExistException.
     * @throws Exception CustomerDoesNotExistException
     */
    @Test(expected = CustomerDoesNotExistException.class)
    public void createWallet_CustomerDoesNotExistException()throws Exception{

        Wallet w = new Wallet();
        w.setWalletId(2);

        Customer c = new Customer();
        c.setUserId(2);
        c.setEmail("saurabh@gmail.com");
        c.setFname("Saurabh");
        c.setLname("Singh");

        when(customerRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(null));
        when(walletRepoMock.save((Wallet)anyObject())).thenReturn(w);
        assertEquals(1, walletService.createWallet(2).getWalletId());
    }

    /**
     * Method test Customer already has a wallet exception
     * @throws Exception CustomerAlreadyHasWalletException
     */
    @Test(expected = CustomerAlreadyHasWalletException.class)
    public void createWallet_CustomerAlreadyHasWalletException()throws Exception{

        Wallet w = new Wallet();
        w.setWalletId(3);

        Customer c = new Customer();
        c.setUserId(3);
        c.setEmail("saurabh@gmail.com");
        c.setFname("Saurabh");
        c.setLname("Singh");
        c.setWallet(w);

        when(customerRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(c));
        when(walletRepoMock.save((Wallet)anyObject())).thenReturn(w);
        assertEquals(1, walletService.createWallet(2).getWalletId());
    }


    /**
     * Method test successful checking of balance.
     */
    @Test
    public void getAccountBalanceForCurrentWallet() throws Exception {
        Wallet w = new Wallet();
        w.setWalletId(10);

        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(1000);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        when(walletRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(w));
        when(accountRepoMock.save(anyObject())).thenReturn(account);
        assertTrue(1000-(float)walletService.getAccountBalanceForCurrentWallet(10, 100)<0.01);
    }

    /**
     * Method tests for AccountNotAssociated with wallet Exception
     * @throws Exception AccountNotAssociatedWithWalletException
     */
    @Test(expected = AccountNotAssociatedWithWalletException.class)
    public void getAccountBalanceForCurrentWallet_AccountNotAssociated() throws Exception {
        Wallet w = new Wallet();
        w.setWalletId(11);

        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(1000);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        when(walletRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(w));
        assertTrue(1000-(float)walletService.getAccountBalanceForCurrentWallet(10, 101)<0.01);
    }

    /**
     * Method test successful withdrawal of money from account
     * @throws Exception
     */
    @Test
    public void withdrawFromAccount() throws Exception {
        Wallet w = new Wallet();
        w.setWalletId(12);

        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(100);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        when(walletRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(w));
        when(accountRepoMock.save(anyObject())).thenReturn(account);
        assertTrue(90-walletService.withdrawFromAccount(12, 100, 10f, "WITHDRAW").getBalance()<0.01);
    }

    /**
     * Method test for exception InsufficientBalanceInWalletException while withdrawing amount > balance
     * @throws Exception InsufficientBalanceInWalletException
     */
    @Test(expected = InsufficientBalanceInWalletException.class)
    public void withdrawFromAccount_InsufficientBalance() throws InsufficientBalanceInWalletException, Exception {
        Wallet w = new Wallet();
        w.setWalletId(14);

        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(100);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        when(walletRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(w));
        when(accountRepoMock.save(anyObject())).thenReturn(account);
        assertTrue(90-walletService.withdrawFromAccount(12, 100, 200f, "WITHDRAW").getBalance()<0.01);
    }

    /**
     * Method tests for successful deposit of money to account
     */
    @Test
    public void depositToAccount() throws Exception{
        Wallet w = new Wallet();
        w.setWalletId(13);

        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(100);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        when(walletRepoMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(w));
        when(accountRepoMock.save(anyObject())).thenReturn(account);
        assertTrue(110-walletService.depositToAccount(13, 100, 10f, "DEPOSIT").getBalance()<0.01);

    }

    /**
     * Testing method for successful transfer of money from one account to another account
     * @throws Exception 3 types of exception can be thrown : WalletIdDoesNotExistException,
     *     AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException - but they are not tested in this method
     */
    @Test
    public void transferToAccount()  throws Exception{
        Wallet w = new Wallet();
        w.setWalletId(15);
        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(100);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        Wallet w2 = new Wallet();
        w2.setWalletId(16);
        Account account2 = new Account();
        account2.setAccountNumber(200);
        account2.setBalance(200);
        List<Account> accounts2 = new ArrayList<>();
        accounts2.add(account2);
        w2.setAccountsInWallet(accounts2);

        when(walletRepoMock.findById(15)).thenReturn(java.util.Optional.ofNullable(w));
        when(walletRepoMock.findById(16)).thenReturn(java.util.Optional.ofNullable(w2));

        when(accountRepoMock.save(account)).thenReturn(account);
        when(accountRepoMock.save(account2)).thenReturn(account2);

        walletService.transferToAccount(15, 100, 16, 200,10f);
        assertEquals(90, (int)account.getBalance());
        assertEquals(210, (int)account2.getBalance());
    }

    /**
     * Testing for check statement balance - last 1 transaction. Method make a withdrawl and then check for set Transaction report
     * @throws Exception can throw WalletIdDoesNotExistException || AccountNotAssociatedWithWalletException
     */
    @Test
    public void getStatement() throws Exception{
        Wallet w = new Wallet();
        w.setWalletId(20);
        Account account = new Account();
        account.setAccountNumber(100);
        account.setBalance(100);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        w.setAccountsInWallet(accounts);

        when(walletRepoMock.findById(20)).thenReturn(java.util.Optional.ofNullable(w));
        when(accountRepoMock.save(anyObject())).thenReturn(account);
        walletService.withdrawFromAccount(20, 100, 10f, "WITHDRAW");
        BankTransaction bankTransaction =  new BankTransaction();
        bankTransaction.setPostBalance(90);
        List<BankTransaction> bankTransactions = new ArrayList<>();
        bankTransactions.add(bankTransaction);
        account.setBankTransactions(bankTransactions);

        assertEquals((int)bankTransaction.getPostBalance(), (int)walletService.getStatement(20, 100, 1).get(0).getPostBalance());

    }
}