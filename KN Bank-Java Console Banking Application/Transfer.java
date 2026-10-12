import java.util.*;
import java.sql.*;

public class Transfer {
    public void transfer(String accountId, String name, String id) {
        boolean continuetransfer=true;
        while(continuetransfer){
            Login l=new Login();
        Scanner s = new Scanner(System.in);
        try (Connection tran = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user,
                DatabaseConnection.pass)) {
            tran.setAutoCommit(false);
            String data_login = "select * from acc_info where Account_ID=?";
            PreparedStatement tran_sta = tran.prepareStatement(data_login);
            tran_sta.setString(1, id);
            ResultSet tran_query = tran_sta.executeQuery();
            if (tran_query.next()) {
                long amount;
                String re_id;
                System.out.println("\n=================================================");
                System.out.println("                 TRANSFER BALANCE                ");
                System.out.println("=================================================");
                System.out.printf(" Current Balance    : %,d MMK\n", tran_query.getLong("Balance"));
                System.out.println("-------------------------------------------------");
                while (true) {
                    System.out.print(" Enter Receiver ID (0 to cancel) : ");
                    re_id = s.nextLine().trim();
                    System.out.println("-------------------------------------------------");
                    
                    if (re_id.equals("0")) {
                        tran.rollback();
                        System.out.println(" [!] Transfer Cancelled.");
                        System.out.println("-------------------------------------------------");
                        l.showDashborad(accountId);
                        return;
                    }
                    if (re_id.equals(id)) {
                        System.out.println(" [X] You cannot transfer to yourself!");
                        System.out.println("-------------------------------------------------");
                        continue;
                    }
                    String re_find="select * from acc_info where Account_ID=?";
                    PreparedStatement find_sta=tran.prepareStatement(re_find);
                    find_sta.setString(1, re_id);
                    ResultSet find_query=find_sta.executeQuery();
                    if(!find_query.next()){
                        System.out.println(" [X] This account is not registered!");
                        System.out.println("-------------------------------------------------");
                        continue;
                    }
                    break;
                }
                
                while (true) {
                    System.out.print(" Enter Transfer Amount           : ");
                    amount = s.nextLong();
                    s.nextLine();
                    if(amount==0){
                        tran.rollback();
                        System.out.println(" [!] Transfer Cancelled.");
                        System.out.println("-------------------------------------------------");
                        l.showDashborad(accountId);
                        return;
                    }
                    if (amount > tran_query.getLong("Balance")) {
                        System.out.println(" [X] Insufficient Balance!");
                        System.out.println("-------------------------------------------------");
                        continue;
                    }
                    if (amount < 0) {
                        System.out.println(" [X] Invalid amount! Enter greater than 0.");
                        System.out.println("-------------------------------------------------");
                        continue;
                    }
                    break;
                }
                
                
                boolean isPincorrect=false;
                for(int i=0;i<3;i++){
                    System.out.print(" Enter Transaction PIN (0 to cancel): ");
                    String pin=s.nextLine();
                    if(pin.equals("0")){
                        tran.rollback();
                        System.out.println("-------------------------------------------------");
                        System.out.println(" [!] Transfer Cancelled.");
                        System.out.println("-------------------------------------------------");
                        l.showDashborad(accountId);
                        return;
                    }
                    if(pin.equals(tran_query.getString("PIN"))){
                        isPincorrect=true;
                        break;
                    }else{
                        System.out.println(" [X] Incorrect PIN! Please try again.");
                        System.out.println("-------------------------------------------------");
                    }
                }
                if(!isPincorrect){
                    tran.rollback();
                    System.out.println("=================================================");
                    System.out.println(" [X] Too many incorrect PIN attempts.");
                    System.out.println("     Transfer Cancelled!");
                    System.out.println("=================================================");
                    l.showDashborad(accountId);
                    return ;
                }
                String receiver = "select a.Balance,u.Name from user_db u join acc_info a on a.Account_ID=u.Account_ID where a.Account_ID=?";
                PreparedStatement re_sta = tran.prepareStatement(receiver);
                re_sta.setString(1, re_id);
                ResultSet re_query = re_sta.executeQuery();
                if (re_query.next()) {
                    long re_baln = re_query.getLong("Balance");
                    re_baln = re_baln + amount;
                    Long send_balan = tran_query.getLong("Balance") - amount;
                    String sender_update = "update acc_info set Balance=? where Account_ID=?";
                    PreparedStatement send_sta = tran.prepareStatement(sender_update);
                    send_sta.setLong(1, send_balan);
                    send_sta.setString(2, id);
                    int sendup_amount = send_sta.executeUpdate();
                    String receiver_update = "update acc_info set Balance=? where Account_ID=?";
                    PreparedStatement update_sta = tran.prepareStatement(receiver_update);
                    update_sta.setLong(1, re_baln);
                    update_sta.setString(2, re_id);
                    int up_amount = update_sta.executeUpdate();
                    if (up_amount > 0) {
                        String S_tran="insert into tran_his(S_id,S_name,R_id,R_name,Amount)values(?,?,?,?,?)";
                        PreparedStatement tranhis_sta=tran.prepareStatement(S_tran);
                        tranhis_sta.setString(1, id);
                        tranhis_sta.setString(2, name);
                        tranhis_sta.setString(3, re_id);
                        tranhis_sta.setString(4, re_query.getString("Name"));
                        tranhis_sta.setLong(5, amount);
                        int tran_up=tranhis_sta.executeUpdate();
                        if(tran_up>0){
                            tran.commit();
                        System.out.println("=================================================");
                        System.out.println("              [✓] TRANSFER SUCCESSFUL            ");
                        System.out.println("=================================================");
                        String up_send = "select * from acc_info where Account_ID=?";
                        PreparedStatement upsend_sta = tran.prepareStatement(up_send);
                        upsend_sta.setString(1, id);
                        ResultSet send_query = upsend_sta.executeQuery();
                        if (send_query.next()) {
                            System.out.printf(" Your Current Balance : %,d MMK\n", send_query.getLong("Balance"));
                        }
                        System.out.println("=================================================");
                        while(true){
                            System.out.println("[1] Next Transfer :\n[2] Back to Dashboard:");
                            int cho=s.nextInt();
                            if (cho==1) {
                                break;
                            }else if(cho==2){
                                l.showDashborad(accountId);
                                continuetransfer=false;
                                break;
                            }else{
                                System.out.println(" [X] Invalid choice! Please choose 1 or 2.");
                                         continue;
                                }
                            }
                        }
                        }

                    } else {
                        tran.rollback();
                        System.out.println("=================================================");
                        System.out.println("               [X] TRANSFER FAILED               ");
                        System.out.println("=================================================");
                    }

                } else {
                    tran.rollback();
                    System.out.println("=================================================");
                    System.out.println("               [X] Sender not found!             ");
                    System.out.println("=================================================");
                }

            }catch(Exception e)
    {
        System.out.println("Database Error! " + e.getMessage());
    }
        }
}
}
