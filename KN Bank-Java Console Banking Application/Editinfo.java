import java.util.*;
import java.util.regex.Pattern;
import java.sql.*;

/**
 * Editinfo
 */
public class Editinfo {
    public void editemail(String email, String id) {
        Scanner s = new Scanner(System.in);
        System.out.println("\n===========================================");
        System.out.println("               EDIT EMAIL                  ");
        System.out.println("===========================================");
        System.out.println(" [!] Account ID    : " + id);
        System.out.println(" [!] Current Email : " + email);
        System.out.println("-------------------------------------------");
        String new_e;
        while (true) {
            while (true) {
                System.out.print(" Enter New Email(0 to cancel)   : ");
                new_e = s.nextLine().trim();
                if (new_e.equals("0")) {
                    return ;
                }
                if (!Pattern.matches("^[A-Za-z0-9._$#-]+@[A-Za-z]+\\.[A-Za-z]{2,6}$", new_e)) {
                    System.out.println("-------------------------------------------");
                    System.out.println(" [X] Invalid email format! Please try again.");
                    System.out.println("-------------------------------------------");
                    continue;
                }
                if (new_e.equals(email)) {
                    System.out.println("-------------------------------------------");
                    System.out.println(" [X]This email is already used.Try another!");
                    System.out.println("-------------------------------------------");
                    continue;
                }
                break;
            }
            String emailsql = "select Email from user_db where Email=? and Account_ID!=?";
            boolean isDupicate = false;
            try (Connection email_con = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass); PreparedStatement email_sta = email_con.prepareStatement(emailsql)) {
                email_sta.setString(1, new_e);
                email_sta.setString(2, id);
                try (ResultSet email_query = email_sta.executeQuery();) {
                    if (email_query.next()) {
                        isDupicate = true;
                    }
                }

            } catch (Exception e) {
                System.out.println("-----------------------------------------------");
                System.out.println("[X]Database Error: " + e.getMessage());
                System.out.println("-----------------------------------------------");
            }
            if (isDupicate) {
                System.out.println("-------------------------------------------");
                System.out.println("[X]This email is already used other person!");
                System.out.println("-------------------------------------------");
                continue;
            }
            while (true) {
                System.out.print(" Confirm New Email : ");
                String renew_e = s.nextLine().trim();
                if (renew_e.equals("0")) {
                    return ;
                }
                if (!new_e.equals(renew_e)) {
                    System.out.println("-----------------------------------------------");
                    System.out.println("[X] Error: Emails do not match or invalid format!");
                    System.out.println("-----------------------------------------------");
                    continue;
                }
                break;
            }
            String up_data = "update user_db set Email=? where Account_ID=?";
            try (Connection email_con = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass); PreparedStatement up_sta = email_con.prepareStatement(up_data)) {
                up_sta.setString(1, new_e);
                up_sta.setString(2, id);
                int uprespon = up_sta.executeUpdate();
                if (uprespon > 0) {
                    System.out.println(" [✔] Email updated successfully!");
                    break;
                } else {
                    System.out.println("        [X] Email update failed!");
                    System.out.println("===========================================");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("-------------------------------------------");
                System.out.println("[X] Database Error! " + e.getMessage());
                System.out.println("-------------------------------------------");
                continue;
            }
        }
    }

    public void editph(String ph, String id) {
        Scanner s = new Scanner(System.in);
        System.out.println("\n===========================================");
        System.out.println("             EDIT PHONE NUMBER             ");
        System.out.println("===========================================");
        System.out.println(" [!] Account ID     : " + id);
        System.out.println(" [!] Current Phone  : " + ph);
        System.out.println("-------------------------------------------");
        String new_ph;
        while (true) {
            while (true) {
                System.out.print(" Enter New Phone(0 to cancel)    : ");
                new_ph = s.nextLine().trim();
                if (new_ph.equals("0")) {
                    return;
                }
                if (!Pattern.matches("^(09)[2468][0-9]{6,8}$", new_ph)) {
                    System.out.println("---------------------------------------------------");
                    System.out.println(" [X] Invalid Phone number format! Please try again.");
                    System.out.println("---------------------------------------------------");
                    continue;
                }

                if (new_ph.equals(ph)) {
                    System.out.println("-------------------------------------------");
                    System.out.println(" [X]This Phone is already used.Try another!");
                    System.out.println("-------------------------------------------");
                    continue;
                }
                break;
            }
            System.out.println("===========================================");
            String edit_data = "select Phone from user_db where Account_ID!=? and Phone=?";
            boolean isDupicate = false;
            try (Connection edit = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass); PreparedStatement edit_sta = edit.prepareStatement(edit_data)) {

                edit_sta.setString(1, id);
                edit_sta.setString(2, new_ph);
                try (ResultSet edit_query = edit_sta.executeQuery()) {
                    if (edit_query.next()) {
                        isDupicate = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Database Error! " + e.getMessage());
                continue;
            }
            if (isDupicate) {
                System.out.println("-------------------------------------------");
                System.out.println(" [X]This Phone is already used other person!");
                System.out.println("-------------------------------------------");
                continue;
            }
            String phupdate = "update user_db set Phone=? where Account_ID=?";
            try (Connection edit = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass); PreparedStatement edit_sta = edit.prepareStatement(phupdate)) {
                edit_sta.setString(1, new_ph);
                edit_sta.setString(2, id);
                int ph_excu = edit_sta.executeUpdate();
                if (ph_excu > 0) {
                    System.out.println(" [✔] Phone Number updated successfully!");
                    return;
                }
            } catch (Exception e) {
                System.out.println("-------------------------------------------");
                System.out.println(" [X] Database Error!" + e.getMessage());
                System.out.println("-------------------------------------------");
                continue;
            }
            break;
        }
    }

    public void editpass(String pass, String id) {
        Scanner s = new Scanner(System.in);
        Authenticate au = new Authenticate();
        System.out.println("\n===========================================");
        System.out.println("               CHANGE PASSWORD             ");
        System.out.println("===========================================");
        System.out.println(" [!] Account ID     : " + id);
        System.out.println("-------------------------------------------");
        String currentPass;
        String newPass;
        while (true) {
            System.out.print(" Enter Current Password(0 to cancel) : ");
            currentPass = s.nextLine().trim();
            if (currentPass.equals("0")) {
                return;
            }
            if (!pass.equals(au.encrypString(currentPass))) {
                System.out.println(" [X] Error: Current password is incorrect!");
                System.out.println("-------------------------------------------");
                continue;
            }
            break;
        }
        while (true) {
            System.out.print(" Enter New Password(0 to cancel)     : ");
            newPass = s.nextLine().trim();
            if (newPass.equals("0")) {
                return;
            }
            if (newPass.length() < 8) {
                System.out.println("---------------------------------------------------");
                System.out.println("[X] Invalid! Password must be at least 8 characters.");
                System.out.println("---------------------------------------------------");
                continue;
            }
            if (newPass.equals(currentPass)) {
                System.out.println("----------------------------------------------");
                System.out.println(" [X]This Password is already used.Try another!");
                System.out.println("----------------------------------------------");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print(" Confirm New Password(0 to cancel)   : ");
            String confirmPass = s.nextLine().trim();
            if (confirmPass.equals("0")) {
                return;
            }
            if (!confirmPass.equals(newPass)) {
                System.out.println("----------------------------------------------");
                System.out.println("       [X] Error: Passwords do not match!");
                System.out.println("----------------------------------------------");
                continue;
            }
            break;
        }
        System.out.println("===========================================");
        String passsql = "update acc_info set Password=? where Account_ID=?";
        try (Connection edit = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                DatabaseConnection.pass); PreparedStatement edit_pass = edit.prepareStatement(passsql)) {
            edit_pass.setString(1, au.encrypString(newPass));
            edit_pass.setString(2, id);
            int uprespon = edit_pass.executeUpdate();
            if (uprespon > 0) {
                System.out.println(" [✔] Password updated successfully!");
            } else {
                System.out.println("        [X] Password update failed!");
                System.out.println("===========================================");
            }

        } catch (

        Exception e) {
            System.out.println("Database Error! " + e.getMessage());
        }
    }

    public void editpin(String pin, String id) {
        Scanner s = new Scanner(System.in);
        Authenticate au = new Authenticate();
        System.out.println("\n===========================================");
        System.out.println("               CHANGE PIN            ");
        System.out.println("===========================================");
        System.out.println(" [!] Account ID     : " + id);
        System.out.println("-------------------------------------------");
        String new_pin;
        while (true) {
            while (true) {
                System.out.println("Enter new PIN(0 to cancel) :");
                new_pin = s.nextLine().trim();
                if (new_pin.equals("0")) {
                    return;
                }
                if (!Pattern.matches("^[0-9]{4}$", new_pin)) {
                    System.out.println("---------------------------------------------------");
                    System.out.println(" [X] Invalid PIN! PIN must be 4 Digits.");
                    System.out.println("---------------------------------------------------");
                    continue;
                }
                if (new_pin.equals(pin)) {
                    System.out.println("---------------------------------------------------");
                    System.out.println(" [X] This PIN is already used.Try another!");
                    System.out.println("---------------------------------------------------");
                    continue;
                }
                break;
            }
            String pin_sql = "update acc_info set PIN=? where Account_ID=?";
            try (Connection pin_con = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass); PreparedStatement pin_sta = pin_con.prepareStatement(pin_sql)) {
                pin_sta.setString(1, new_pin);
                pin_sta.setString(2, id);
                int pin_re = pin_sta.executeUpdate();
                if (pin_re > 0) {
                    System.out.println("---------------------------------------------------");
                    System.out.println(" []PIN editing is successfull.");
                    System.out.println("---------------------------------------------------");
                    return;
                }

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("---------------------------------------------------");
                System.out.println(" [X] Database Error!" + e.getMessage());
                System.out.println("---------------------------------------------------");
                continue;
            }
            break;
        }

    }

    public void edit(String accountId, String name, String id) {
        Scanner s = new Scanner(System.in);
        int cho;
       while (true) {
        while (true) {
            System.out.println("\n===========================================");
            System.out.println("               EDIT PROFILE                ");
            System.out.println("===========================================");
            System.out.println(" [!] Account ID   : " + id);
            System.out.println(" [!] Account Name : " + name);
            System.out.println("-------------------------------------------");
            System.out.println(" [1] Edit Email");
            System.out.println(" [2] Edit Phone Number");
            System.out.println(" [3] Edit Password");
            System.out.println(" [4] Edit PIN");
            System.out.println(" [0] Back to Dashboard");
            System.out.println("===========================================");
            System.out.print("Choose an option: ");
            try {
                cho = Integer.parseInt(s.nextLine().trim());
            } catch (NumberFormatException e) {
                // TODO: handle exceptions
                System.out.println("Error! "+ e.getMessage());
                continue;
            }
            if (cho == 0) {
                return;
            } else if (cho < 1 || cho > 4) {
                System.out.println("[X} Invalid Choice!");
                continue;
            }
            break;
        }
        String email = "", ph = "", pass = "", pin = "";
        String edit_data = "select u.Email,u.Phone,a.Password,a.PIN from user_db u join acc_info a on a.Account_ID=u.Account_ID where a.Account_ID=?";
        try (Connection edit = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                DatabaseConnection.pass); PreparedStatement edit_sta = edit.prepareStatement(edit_data)) {
            edit_sta.setString(1, id);
            try (ResultSet edit_query = edit_sta.executeQuery()) {
                if (edit_query.next()) {
                    email = edit_query.getString("Email");
                    ph = edit_query.getString("Phone");
                    pass = edit_query.getString("Password");
                    pin = edit_query.getString("PIN");
                }
            }
        } catch (Exception e) {
            System.out.println("Database Error! " + e.getMessage());
        }
        switch (cho) {
            case 1:
                editemail(email, id);
                break;
            case 2:
                editph(ph, id);
                break;
            case 3:
                editpass(pass, id);
                break;
            case 4:
                editpin(pin, id);
                break;
            default:
                break;
        }
       }
    }
}