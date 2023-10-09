## About

This project is about  *Building a Java library for online banking platform to build the virtual wallet to track users transaction account.*

This is a spring boot application with in memory database *H2*. I am using JPA(Java Persistance API) to interact with the in memory database.


## Problem Statement

Infiniti Space Bank is launching a brand new online banking platform and they want you to build a Java library to build the
virtual wallet to track users transaction account. https://en.wikipedia.org/wiki/Transaction_account .
At high level the library needs to solve the following business needs
* Track a user’s account balance
* Manage account transactions in the form of debits (withdrawals) and credits (deposits).
* Allow a user to transfer money to another user/account.
* Keep a history of last N transactions.


## UML Diagram

Refer to the follwing UML diagram indicating the database tables and their intersactions.



<a href="https://ibb.co/nJNLOp"><img src="https://preview.ibb.co/b74Zip/UML-2.png" alt="UML-2" border="0"></a>

## Running the project

Following steps illustrates the procedures you need to follow to run the code :

`Step 1` : Download the repository

```{shell}
$ git clone https://github.com/saurabhsingh13no/virtual-wallet.git
$ cd virtual-wallet
```

`Step 2` : Build the project using maven

```{shell}
$ mvn clean install
```

`Step 3` : run the project

```
$ mvn spring-boot:run
```

* Now navigate to http://localhost:8080/ . You would see something like below index.html page :

<a href="https://ibb.co/bwuH3p"><img src="https://preview.ibb.co/mykPip/landing-page.png" alt="landing-page" border="0"></a>

Congratulations. You have successfully cloned the repo.

## Functionality

Since project uses *H2* in-memory database, some sample data has already been provided to get started with. Below are snapshot of data that already exists in the table :

<a href="https://ibb.co/c4TAOp"><img src="https://preview.ibb.co/m5bapU/sample-data2.png" alt="sample-data2" border="0"></a>

* You can see the entries in the table for yourself. Navigate to `http://localhost:8080/h2-console` .You would see below screen :

<a href="https://ibb.co/cpRrUU"><img src="https://preview.ibb.co/nrHy9U/H2-starting-session.png" alt="H2-starting-session" border="0"></a>

** Make sure **  that you use `jdbc:h2:mem:testdb` as JDBC URL. Click connect.

Enter below select queries to see the output :

```{sql}
select * from customer;
select * from wallet;
select * from account;
select * from bank_transaction;
```

Press `ctrl+enter`.

Now, I think you are all set up. Lets see what this library can do :

* ### a) Create a new wallet for a user :

Provided endpoint for creating new wallet :
```
http://localhost:8080/api/wallet
```

I used `Postman` for this, since it provides easy interface for sending post request.

e.g. Lets create a wallet for ** Dan Brown** our 4th user in the database. We would **post** request to `http://localhost:8080/api/wallet/` using below JSON format :

<a href="https://ibb.co/gY5t9U"><img src="https://preview.ibb.co/coaFOp/create-wallet.png" alt="create-wallet" border="0"></a>

You can now check, that wallet 4 is now associated with user 4 :

<a href="https://ibb.co/ex6fOp"><img src="https://preview.ibb.co/fVgEip/after-create-wallet.png" alt="after-create-wallet" border="0"></a>

* ### b) Attach an account to user 4 (Dan in this case) :

Provided endpoint : `http://localhost:8080/api/account/`

use post request as :
```{JSON}
{
	"accountNumber" : 3001,
	"balance":4000,
	"accountHolder" : {
		"userId": 4,
		"fname" : "Dan",
		"lname":"Brown",
		"email" :"dan@brown"
	},
	"walletHolder" : {
		"walletId" : 4
	}
}
```

We can now check that Account 3001 is now associated with walletId 4 :

<a href="https://ibb.co/javob9"><img src="https://preview.ibb.co/b7OwUU/after-add-account.png" alt="after-add-account" border="0"></a>

* ### c) Return current account balance :

Provided endpoint : `http://localhost:8080/api/wallet/{walletId}/account/{accountId}/balance`.

e.g Let us check balance for User 1 and associated account number 1000. We would use a **GET** request to `http://localhost:8080/api/wallet/1/account/1000/balance`. Below is the output from postman :

<a href="https://ibb.co/dinvOp"><img src="https://preview.ibb.co/e8eWw9/get-balance.png" alt="get-balance" border="0"></a>


The backend JAVA code checks for all validation. If the accountId is not associated with provided walledId, an exception is thrown.

* ### d) Perform a withdrawal transaction on an account :

Provided endpoint : `http://localhost:8080/api/wallet/{walletId}/account/{accountId}/withdraw/{amount}`

Allows one to withdraw amount from account associated with wallet.

e.g. Lets withdraw $100 from accountId 1000 associated with walletId 1. I use **POST** request to `http://localhost:8080/api/wallet/1/account/1000/withdraw/100` in Postman. Following is the output :

<a href="https://ibb.co/j8eaOp"><img src="https://preview.ibb.co/c3Gdb9/withdraw.png" alt="withdraw" border="0"></a>

We can now check that $100 was actually withdrawn from the account as is reflected in the database :

<a href="https://ibb.co/khWpip"><img src="https://preview.ibb.co/mF6N3p/after-withdraw.png" alt="after-withdraw" border="0"></a>

* ### e) Perform a deposit transaction on an account :

Provided endpoint : `http://localhost:8080/api/wallet/{walletId}/account/{accountId}/deposit/{amount}`

Allows one to deposit amount into an account associated with a wallet.

e.g. Lets deposit $200 to accountId 1000 associated with walletId 1. I use **POST** request to `http://localhost:8080/api/wallet/1/account/1000/deposit/200` in Postman. Following is the output :

<a href="https://ibb.co/kJCy9U"><img src="https://preview.ibb.co/cC5J9U/deposit.png" alt="deposit" border="0"></a>

We can now check that account 1000 does have a new deposit of $200 by running select query on table account :

<a href="https://ibb.co/bVKOb9"><img src="https://preview.ibb.co/jq8Gw9/after-deposit.png" alt="after-deposit" border="0"></a>

* ### f) Perform a transfer from one account to another account :

Provided endpoint :
`http://localhost:8080/api/wallet/{walletId}/account/{trasferFromAccountId}/transfer/wallet/{toWalletId}/account/{transferToAccountId}/amount/{amount}`.

Allows one to transfer money from one account in one wallet to another account in another wallet.

e.g. Let us transfer $150 from accountId 1000 associated with walletId 1 to accountId 2000 associated with wallet 2 . I use a **POST** request to `http://localhost:8080/api/wallet/1/account/1000/transfer/wallet/2/account/2000/amount/150` in Postman. Following is the output :

<a href="https://ibb.co/e7F1UU"><img src="https://preview.ibb.co/gfrapU/transfer.png" alt="transfer" border="0"></a>

Let us confirm the transfer by checking in the database :

<a href="https://ibb.co/cbyo9U"><img src="https://preview.ibb.co/j3VeG9/after-transfer-again.png" alt="after-transfer-again" border="0"></a>

* ### g) Return last N transactions for an account :

Provoded endpoint :  `http://localhost:8080/api/wallet/{walletId}/account/{accountId}/lastNTransactions/{n}`.

Allows one to check their respect transaction statement.

e.g. Let us check for last 3 transactions for accountId 1000 associated with walletId 1. I use a **GET** request to `http://localhost:8080/api/wallet/1/account/1000/lastNTransactions/3` in Postman. Following is the output :

<a href="https://ibb.co/myKyb9"><img src="https://preview.ibb.co/jYRN3p/last-Ntransaction.png" alt="last-Ntransaction" border="0"></a>
