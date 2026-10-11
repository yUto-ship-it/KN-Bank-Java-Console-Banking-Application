import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.naming.spi.DirObjectFactory;
import javax.naming.spi.DirStateFactory.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;

public class Authenticate {
    String name, nrc, phone, password, repassword, email, final_pass;
    int age;
    long accountId;

    public void signUp(Scanner s) {
        Random r = new Random();
        System.out.println("+===========================+");
        System.out.println(" CREATE NEW KN BANK ACCOUNT");
        System.out.println("+===========================+");

        System.out.println("=====================================================");
        System.out.print("Enter your Name            :");
        this.name = s.nextLine();
        System.out.println("=====================================================");

        while (true) {
            System.out.print("Enter your Age          : ");
            String ageinput = s.nextLine();
            if (Pattern.matches("^[0-9]+$", ageinput)) {
                this.age = Integer.parseInt(ageinput);
                if (this.age >= 18 && this.age < 100) {
                    System.out.println("=====================================================");
                    break;
                } else {
                    System.out.println("[X] Age must be between 18 and 100 years old [X]");
                    System.out.println("+---------------------------+");
                }
            } else {
                System.out.println("Invalid Age! Please enter numeric value of age.");
                System.out.println("+---------------------------+");
            }
        }

        while (true) {
            try (Connection sign = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass)) {
                while (true) {
                    System.out.print("Enter your NRC : ");
                    this.nrc = s.nextLine();
                    String getdata = "select 1 from user_db where NRC=?";
                    if (!Pattern.matches("^([1-9]|1[0-4])/[A-Za-z\\s]+\\([NnCc]\\)[0-9]{6}$", this.nrc)) {

                        System.out.println("=====================================================");
                        System.out.println("    Invalid NRC format! Please Check your NRC!");
                        System.out.println("=====================================================");
                        continue;
                    }
                    try (PreparedStatement data_sta = sign.prepareStatement(getdata);) {
                        data_sta.setString(1, nrc);
                        try (ResultSet data_query = data_sta.executeQuery()) {
                            if (data_query.next()) {
                                System.out.println("=================================");
                                System.out.println("   This NRC is already used!");
                                System.out.println("=================================");
                                continue;

                            }
                        }
                    }
                    System.out.println("=====================================================");
                    break;
                }

                while (true) {
                    System.out.print("Enter your Email :");
                    this.email = s.nextLine();
                    String emailsql = "select 1 from user_db where Email=?";
                    if (!Pattern.matches("^[A-Za-z0-9.*%&-]+@[A-Za-z]+\\.[A-Za-z]{2,6}$", this.email)) {
                        System.out.println("=====================================================");
                        System.out.println("    Invalid Email format! Please Check your email!");
                        System.out.println("=====================================================");
                        continue;
                    }
                    try (PreparedStatement email_sta = sign.prepareStatement(emailsql)) {
                        email_sta.setString(1, this.email);
                        try (ResultSet email_query = email_sta.executeQuery()) {
                            if (email_query.next()) {
                                System.out.println("=================================");
                                System.out.println("   This Email already used!");
                                System.out.println("=================================");
                                continue;
                            }
                        }

                    }
                    System.out.println("=====================================================");
                    break;
                }
                while (true) {
                    System.out.print("Enter your Phone Number : ");
                    this.phone = s.nextLine();
                    String phsql = "select 1 from user_db where Phone=?";
                    if (!Pattern.matches("(09)[2468][0-9]{6,8}", this.phone)) {
                        System.out.println("=====================================================");
                        System.out.println("    Invalid Phone number format! Please try again");
                        System.out.println("=====================================================");
                        continue;
                    }
                    try (PreparedStatement ph_sta = sign.prepareStatement(phsql)) {
                        ph_sta.setString(1, this.phone);
                        try (ResultSet ph_query = ph_sta.executeQuery()) {
                            if (ph_query.next()) {
                                System.out.println("=================================");
                                System.out.println("   This Phone Number already used!");
                                System.out.println("=================================");
                                continue;
                            }
                        }
                    }
                    System.out.println("=====================================================");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Database Error! " + e.getMessage());
                continue;
            }
            break;
        }
        while (true) {
            while (true) {
                System.out.print("Enter password : ");
                this.password = s.nextLine();
                if (this.password.length() < 8) {
                    System.out.println("=====================================================");
                    System.out.println("        Password must be at least 8 characters");
                    System.out.println("=====================================================");
                    continue;
                }
                break;
            }
            System.out.print("Enter Confirmed password : ");
            this.repassword = s.nextLine();
            if (!this.repassword.equals(this.password)) {
                System.out.println("=====================================================");
                System.out.println("        Passwords do not match! Try again");
                System.out.println("=====================================================");
                continue;
            } else {
                final_pass = this.encrypString(this.password);
                break;
            }
        }

        accountId = 10000000L + r.nextInt(90000000);
        try (Connection con = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                DatabaseConnection.pass)) {

            String sql = "insert into user_db( Account_ID, Name, Age, NRC, Email, Phone)values(?,?,?,?,?,?)";
            String sql_db2 = "insert into acc_info( Account_ID, Password)values(?,?)";
            PreparedStatement db2_sta = con.prepareStatement(sql_db2);
            db2_sta.setLong(1, accountId);
            db2_sta.setString(2, final_pass);
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, accountId);
            pstmt.setString(2, this.name);
            pstmt.setInt(3, this.age);
            pstmt.setString(4, this.nrc);
            pstmt.setString(5, this.email);
            pstmt.setString(6, this.phone);
            pstmt.executeUpdate();
            db2_sta.executeUpdate();
            System.out.println("+========================================+");
            System.out.println("       Account Created Successfully!   ");
            System.out.println("+========================================+");
        } catch (Exception e) {
            System.out.println("Database Error!" + e.getMessage());
        }

        System.out.printf("%-20s: %s\n", "Name", this.name);
        System.out.printf("%-20s: %d\n", "Age", this.age);
        System.out.printf("%-20s: %s\n", "NRC", this.nrc);
        System.out.printf("%-20s: %s\n", "Phone", this.phone);
        System.out.printf("%-20s: %s\n", "Email", this.email);
        System.out.println("+---------------------------------------+");

        System.out.printf("%-20s: %d\n", "Account ID", accountId);
        System.out.println("+---------------------------------------+");
        System.out.println("\n\n\n");
        Pincreation p = new Pincreation();
        String acc_ID=Long.toString(accountId);
        p.pin(acc_ID);
    }

    public void login(Scanner s) {
        Login l = new Login();
        System.out.println("+===========================+");
        System.out.println("    LOGIN TO YOUR ACCOUNT");
        System.out.println("+===========================+");
        String loginInput;
        while (true) {
            System.out.println("+===========================+");
            System.out.print("Enter your email(or)Accound ID       :");
            loginInput = s.nextLine();
            if (!Pattern.matches("^[A-Za-z0-9._$#-]+@[A-Za-z]+\\.[A-Za-z]{2,6}$", loginInput)
                    && !Pattern.matches("^[0-9]+$", loginInput)) {

                System.out.println("Invalid format email (or) Account ID");
                System.out.println("+---------------------------+");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("+===========================+");
            System.out.print("Enter your password                   :");
            String loginpassword = s.nextLine();
            try (Connection log = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass);) {

                String loginsql = "select a.Password,u.Account_ID from user_db u join acc_info a on a.Account_ID = u.Account_ID where u.Email=? or a.Account_ID=? ";
                PreparedStatement logstmt = log.prepareStatement(loginsql);
                logstmt.setString(1, loginInput);
                try {
                    logstmt.setLong(2, Long.parseLong(loginInput));
                } catch (NumberFormatException e) {
                    logstmt.setLong(2, 0);
                }
                java.sql.ResultSet logquery = logstmt.executeQuery();
                if (logquery.next()) {
                    String dbpassword = logquery.getString("Password");
                    String accid = logquery.getString("Account_ID");
                    String hashpassword = encrypString(loginpassword);
                    if (dbpassword.equals(hashpassword)) {
                        System.out.println("+===========================+");
                        System.out.println("      LOGIN SUCCESSFULL");
                        System.out.println("     Welcom From KN Bank");
                        System.out.println("+===========================+");

                        l.showDashborad(accid);
                        break;
                    } else {
                        System.out.println("Invalid passwrod! Please try again");
                    }
                } else {
                    System.out.println("Your account not found!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error!" + e.getMessage());
            }
        }
    }

    public String encrypString(String password) {
        try {
            MessageDigest ms = MessageDigest.getInstance("SHA-256");
            byte[] encry = ms.digest(password.getBytes());
            StringBuilder en = new StringBuilder();
            for (byte b : encry) {
                en.append(String.format("%02x", b));
            }
            return en.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
