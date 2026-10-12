import java.sql.*;
import java.util.*;

public class Login {

    public void showDashborad(String accountId) {
        Scanner s = new Scanner(System.in);
        Check_balance c = new Check_balance();
        Transfer t = new Transfer();
        Tran_history t_h = new Tran_history();
        Atm a = new Atm();
        Editinfo edit = new Editinfo();
        String name = "", id = "";
        try (Connection dash = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                DatabaseConnection.pass)) {
            String dashout = "select Name,Account_ID from user_db where Account_ID=?";
            PreparedStatement dashsta = dash.prepareStatement(dashout);
            dashsta.setString(1, accountId);
            ResultSet dashquery = dashsta.executeQuery();
            if (dashquery.next()) {
                name = dashquery.getString("Name");
                id = dashquery.getString("Account_ID");
            }

        } catch (Exception e) {
            System.out.println("Database Error!" + e.getMessage());
        }
        while (true) {
            System.out.println("╔═══════════════════════════════════════════════════════╗");
            System.out.println("║                   KN BANK DASHBOARD                   ║");
            System.out.println("╠═══════════════════════════════════════════════════════╣");

            System.out.printf("║  %-10s : %-31s         ║\n", "Name", name);
            System.out.printf("║  %-10s : %-31s         ║\n", "Account ID", id);
            System.out.println("╠═══════════════════════════════════════════════════════╣");
            System.out.println("║[1] Check Balance                                      ║");
            System.out.println("║[2] Transfer Balance                                   ║");
            System.out.println("║[3] Transcation History                                ║");
            System.out.println("║[4] Edit Profile                                       ║");
            System.out.println("║[5] Logout                                             ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            int num = s.nextInt();
            switch (num) {
                case 1:
                    c.checkBalance(accountId, name, id);
                    break;
                case 2:
                    t.transfer(accountId, name, id);
                    break;
                case 3:
                    t_h.histroy(accountId, name, id);
                    break;
                case 4:
                    edit.edit(accountId, name, id);
                    break;
                case 5:
                    a.authen();
                    break;
                default:
                    break;
            }
        }

    }

}
