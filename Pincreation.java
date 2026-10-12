import java.util.*;
import java.sql.*;
import java.util.regex.Pattern;

public class Pincreation {
    public void pin(String accountId) {

        Login l = new Login();
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("╔═══════════════════════════════════════════════════════╗");
            System.out.println("║                      SECURE PIN                       ║");
            System.out.println("╠═══════════════════════════════════════════════════════╣");
            System.out.println("║  Please create a 4-digit security PIN for your account ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.print("Create 4-digit PIN : ");
            String pin = s.nextLine();
            System.out.println("════════════════════════════════════════");

            if (!Pattern.matches("^[0-9]{4}$", pin)) {
                System.out.println("┌──────────────────────────────────────────┐");
                System.out.println("  [X] PIN must be 4-digits (0-9).         ");
                System.out.println("└──────────────────────────────────────────┘");
                continue;
            }
            String pin_add = "update acc_info set PIN=? where Account_ID=?";
            try (Connection p_in = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                    DatabaseConnection.pass); PreparedStatement pin_sta = p_in.prepareStatement(pin_add);) {
                pin_sta.setString(1, pin);
                pin_sta.setString(2, accountId);
                int pinup = pin_sta.executeUpdate();
                if (pinup > 0) {
                    System.out.println("+=================================+");
                    System.out.println("|    PIN creation is successful.  |");
                    System.out.println("+=================================+");
                    l.showDashborad(accountId);
                    break;
                } else {
                    System.out.println("┌──────────────────────────────────────────┐");
                    System.out.println("  [X] PIN creation is unsuccessful.       ");
                    System.out.println("└──────────────────────────────────────────┘");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Database Error! " + e.getMessage());
                continue;
            }
        }
    }
}