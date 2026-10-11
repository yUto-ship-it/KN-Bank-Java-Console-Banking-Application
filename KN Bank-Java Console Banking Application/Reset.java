import java.util.*;
import java.sql.*;

public class Reset {

    public void adminreset() {
        Scanner in = new Scanner(System.in);
        Authenticate au = new Authenticate();

        while (true) {
            System.out.println("┌─────────────────────────────────┐");
            System.out.println("|           ADMIN PAGE            |");
            System.out.println("└─────────────────────────────────┘");

            System.out.print("Enter NRC to reset(0 to cancel)      :");
            String reset_nrc = in.nextLine();
            if(reset_nrc.equals("0")){
                System.exit(0);
            }
            System.out.println("+---------------------------+");
            System.out.print("Enter Email to reset    :");
            String reset_email = in.nextLine();
            if(reset_email.equals("0")){
                System.exit(0);
            }
            System.out.println("+---------------------------+");
            System.out.print("Enter Phone number to reset     :");
            String reset_ph = in.nextLine();
            if(reset_ph.equals("0")){
                System.exit(0);
            }
            try (Connection reset = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass)) {
                String reset_db = "select  a.Password,a.Account_ID from user_db u join acc_info a on a.Account_ID=u.Account_ID where u.NRC=? and u.Email=? and u.Phone=?";
                PreparedStatement reset_sta = reset.prepareStatement(reset_db);
                reset_sta.setString(1, reset_nrc);
                reset_sta.setString(2, reset_email);
                reset_sta.setString(3, reset_ph);
                java.sql.ResultSet reset_query = reset_sta.executeQuery();
                if (reset_query.next()) {
                    String newpass, hashpass,id;
                    PreparedStatement updatepass;
                    String oldpass = reset_query.getString("Password");
                    id = reset_query.getString("Account_ID");
                    System.out.println("+---------------------------+");
                    while (true) {
                        while (true) {
                            System.out.print("Enter new password for the user(at least 8 characters (or) 0 to cancel)  :");
                            newpass = in.nextLine();
                            if(newpass.equals("0")){
                                System.exit(0);
                            }
                            hashpass = au.encrypString(newpass);

                            if (newpass.length() < 8) {
                                System.out.println("[X] Password must be at least 8 characters! Reset cancelled.");
                                continue;
                            }
                            if (hashpass.equals(oldpass)) {
                                System.out.println("This password is alreay in this account.Please try another");
                                continue;
                            }
                            break;

                        }

                        String set_new = "update acc_info set Password=? where Account_ID=?";
                        updatepass = reset.prepareStatement(set_new);

                        updatepass.setString(1, hashpass);
                        updatepass.setString(2, id);

                        int respon = updatepass.executeUpdate();
                        if (respon > 0) {
                            System.out.println("┌──────────────────────────────────┐");
                            System.out.println("  [✓]Password reset is successfull  ");
                            System.out.println("└──────────────────────────────────┘");
                            break;
                        } else {
                            System.out.println("[X] Password reset is unsuccessfull.");
                        }
                    }
                } else {
                    System.out.println("[X] This account is not found.");
                    while (true) {
                        int cho;
                        while (true) {
                            System.out.println("[1] Retry");
                            System.out.println("[2] Exit");
                            try {
                                cho = Integer.parseInt(in.nextLine().trim());
                            } catch (NumberFormatException e) {
                                // TODO: handle exception
                                System.out.println("Error!" + e.getMessage());
                                continue;
                            }
                            break;
                        }
                        if (cho == 2) {
                            return;
                        } else if (cho == 1) {
                            break;
                        } else {
                            System.out.println("┌──────────────────────────────────────────┐");
                            System.out.println("  [X] Invalid choice! Please choose 1 or 2 ");
                            System.out.println("└──────────────────────────────────────────┘");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error!" + e.getMessage());
            }
        }
    }
}
