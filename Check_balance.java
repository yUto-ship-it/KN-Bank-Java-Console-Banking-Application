import java.sql.*;
public class Check_balance {
    public void checkBalance(String accountId, String n, String i) {
        Login l=new Login();
        try (Connection check_balan = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                DatabaseConnection.pass)) {
            String checkdata = "select Balance from acc_info where Account_ID=?";
            PreparedStatement check_sta = check_balan.prepareStatement(checkdata);
            check_sta.setString(1, accountId);
            java.sql.ResultSet check_query = check_sta.executeQuery();
            if (check_query.next()) {
                System.out.println("╔═══════════════════════════════════════════════════════╗");
                System.out.println("║                   KN BANK DASHBOARD                   ║");
                System.out.println("║                   ACCOUNT STATEMENT                   ║");
                System.out.println("╠═══════════════════════════════════════════════════════╣");

                System.out.printf("║  %-10s : %-31s         ║\n", "Name", n);
                System.out.printf("║  %-10s : %-31s         ║\n", "Account ID", i);
                System.out.println("╠═══════════════════════════════════════════════════════╣");
                System.out.printf("  %-10s : %-31s         \n", "Current Balance", check_query.getString("Balance"));
                System.out.println("╚═══════════════════════════════════════════════════════╝");
                l.showDashborad(accountId);
            }
        } catch (Exception e) {
            System.out.println("Database Error! " + e.getMessage());
        }
    }
}
