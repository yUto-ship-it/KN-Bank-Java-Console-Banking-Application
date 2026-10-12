import java.sql.*;
import java.util.*;
public class Tran_history {
    public void transfer(String accountId, String name, String id){
        try(Connection his=DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,DatabaseConnection.pass)){
            String tran_his="select * from tran_his where S_id=?";
            PreparedStatement tranhis_sta=his.prepareStatement(tran_his);
            tranhis_sta.setString(1, id);
            ResultSet his_query=tranhis_sta.executeQuery();
            System.out.println("\n==================================================================================================");
            System.out.println("                                         TRANSFER HISTORY                                         ");
            System.out.println("==================================================================================================");
            // Header for table
            System.out.printf("%-5s %-22s %-12s %-15s %-12s %-20s\n", "No.", "Receiver", "ID", "Amount", "Status", "Date");
            System.out.println("--------------------------------------------------------------------------------------------------");
            
            int i = 1;
            boolean hasRecord = false;
            while(his_query.next()){
                hasRecord = true;
                String re_id = his_query.getString("R_id");
                String re_name = his_query.getString("R_name");
                long re_amount = his_query.getLong("Amount");
                Timestamp time = his_query.getTimestamp("Transfer_date");
                System.out.printf("%-5d %-22s %-12s %-15s %-12s %-20s\n", i, re_name, re_id, re_amount, "Transfer", time);
                i++;
            }
            if(!hasRecord){
                System.out.println(" No transfer history found.");
                System.out.println("==================================================================================================");
                histroy(accountId, name, id);
                return ;
            }else{
                System.out.println("==================================================================================================");
                histroy(accountId, name, id);
                return;
            }
        }catch(Exception e){
            System.out.println("Database Error! "+e.getMessage());
        }
    }
    public void receive(String accountId, String name, String id){
        try(Connection his=DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,DatabaseConnection.pass)){
            String tran_his="select * from tran_his where R_id=?";
            PreparedStatement tranhis_sta=his.prepareStatement(tran_his);
            tranhis_sta.setString(1, id);
            ResultSet his_query=tranhis_sta.executeQuery();
            System.out.println("\n==================================================================================================");
            System.out.println("                                         RECEIVED HISTORY                                         ");
            System.out.println("==================================================================================================");
            // Header for table
            System.out.printf("%-5s %-22s %-12s %-15s %-12s %-20s\n", "No.", "Sender", "ID", "Amount", "Status", "Date");
            System.out.println("--------------------------------------------------------------------------------------------------");
            
            int i = 1;
            boolean hasRecord = false;
            while(his_query.next()){
                hasRecord = true;
                String s_id = his_query.getString("S_id");
                String s_name = his_query.getString("S_name");
                long s_amount = his_query.getLong("Amount");
                Timestamp time = his_query.getTimestamp("Transfer_date");
                System.out.printf("%-5d %-22s %-12s %-15s %-12s %-20s\n", i, s_name, s_id, s_amount, "Received", time);
                i++;
            }
            if(!hasRecord){
                System.out.println(" No receive history found.");
                System.out.println("==================================================================================================");
                histroy(accountId, name, id);
                return;
            }else{
                System.out.println("==================================================================================================");
                histroy(accountId, name, id);
                return;
                
            }
            
        }catch(Exception e){
            System.out.println("Database Error! "+e.getMessage());
        }
    }
    public void histroy(String accountId, String name, String id){
        Scanner s=new Scanner(System.in);
        Login l=new Login();
        System.out.println("\n===========================");
        System.out.println("      TRANSACTION HISTORY  ");
        System.out.println("===========================");
        System.out.println("[1] Transfer History");
        System.out.println("[2] Received History");
        System.out.println("[0] Back to Dashboard");
        System.out.println("===========================");
        System.out.print("Choose option: ");
        int cho=s.nextInt();
        switch (cho) {
            case 1:transfer(accountId,name,id);break;
            case 2:receive(accountId,name,id);break;
            case 0:l.showDashborad(accountId);break;
            default:break;
        }
    }
}
