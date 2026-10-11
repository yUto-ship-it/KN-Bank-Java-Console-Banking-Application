# KN-Bank-Java-Console-Banking-Application
Frontend and JDBC Database Connection Banking System


KN Bank is a console-based Java banking application backed by MySQL. It demonstrates account registration and login, profile management, balance checks, account-to-account transfers, and transaction history.

Features

•
Create an account with name, age, NRC, email, and phone number.

•
Log in using an email address or account ID.

•
Create a four-digit transaction PIN.

•
View the account balance.

•
Transfer funds to another registered account, with PIN verification.

•
View sent and received transfer history.

•
Edit profile details, password, and PIN.

•
Access the password-reset option from the welcome menu.

Requirements

•
JDK 8 or newer

•
MySQL Server

•
MySQL Connector/J (included in lib/ in this project archive)

•
A terminal or Java IDE such as IntelliJ IDEA, Eclipse, or NetBeans

Database Setup

The application connects to a MySQL database named mydata on localhost:3306. Create the database and tables before running the program:

SQL


CREATE DATABASE mydata;
USE mydata;

CREATE TABLE user_db (
    Account_ID MEDIUMTEXT,
    Name VARCHAR(50),
    Age INT,
    NRC VARCHAR(50),
    Email VARCHAR(50),
    Phone VARCHAR(50)
);

CREATE TABLE acc_info (
    Account_ID MEDIUMTEXT,
    Balance DECIMAL(15, 2) DEFAULT 1.00,
    Password VARCHAR(64),
    PIN VARCHAR(4) NULL
);

CREATE TABLE tran_his (
    S_id MEDIUMTEXT,
    S_name VARCHAR(50),
    R_id MEDIUMTEXT,
    R_name VARCHAR(50),
    Amount DECIMAL(15, 2),
    Transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



The PIN column must allow NULL initially because a new account is created before the user sets a PIN. The code uses numeric account IDs and stores balances and transfer amounts as decimal values, though transfer operations currently read and write them as whole numbers.

Configure the Database Connection

Open DatabaseConnection.java and configure its MySQL JDBC URL, username, and password for your local MySQL installation. The uploaded source contains a hard-coded database password; replace it with your own local credential and do not commit real credentials to a public repository. If that password has been used outside a private local environment, rotate it.

The default URL in the source is:

Plain Text


jdbc:mysql://localhost:3306/mydata



Run the Application

Atm.java contains the main method and is the program entry point.

From the command line

Run these commands from the project directory on macOS or Linux:

Bash


javac -cp ".:lib/*" *.java
java -cp ".:lib/*" Atm



On Windows, run the application with the included MySQL Connector/J JAR:

Plain Text


javac -cp ".;lib\mysql-connector-j-26.7.0.jar" *.java
java -cp ".;lib\mysql-connector-j-26.7.0.jar" Atm



Alternatively, open the project in a Java IDE, add the JAR files in lib/ to the project classpath, and run Atm.java.

Usage

1.
Start the application by running Atm.

2.
Choose Create A New Bank Account or Login To Your Account from the welcome menu.

3.
Complete the prompts. New accounts are assigned an account ID; save it for future login.

4.
After login, use the dashboard to check your balance, transfer funds, view transfer history, edit your profile, or log out.

Security and Scope Notes

This is an educational sample, not production banking software. Passwords are hashed with plain SHA-256 in the current implementation, which is not suitable for password storage; use a password-specific algorithm such as Argon2id, bcrypt, or scrypt with salts in production. The four-digit PIN is stored in plain text in the current implementation. Use stronger protections, parameterized configuration/secrets, transaction-safe balance updates, and thorough validation before any real-world use.

The current dashboard provides balance inquiry, transfers, history, profile editing, and logout. Deposit, withdrawal, and loan operations are not implemented in the uploaded source.

Contributing

Issues and pull requests are welcome. Include a clear description of changes and test them against a local MySQL database.

License

The project description identifies the project as MIT-licensed. Add a LICENSE file with the MIT License text to the repository if you intend to distribute it under that license.

