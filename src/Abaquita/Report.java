package Abaquita;

import java.util.Scanner;
import java.sql.*;

public class Report {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Clients c = new Clients();
    Loan_Transaction lt = new Loan_Transaction();
    
    public void report_type(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Report**","");
            System.out.printf("|%-5s%-95s|\n","","1. General Report");
            System.out.printf("|%-5s%-95s|\n","","2. Individual Report");
            System.out.printf("|%-5s%-95s|\n","","3. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<4){
                        break;
                    }else{
                        System.out.printf("|%-5sEnter Choice Again: ","");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.printf("|%-5sEnter Choice Again: ","");
                }
            }
            switch(choice){
                case 1:
                    generalReport();
                    break;
                case 2:
                    c.view();
                    IndividualView();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    private void generalReport(){
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Clients**","");
        c.view();
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Loaned History**","");
        lt.view2();
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Payment History**","");
        lt.view3();
        
    }
    private void IndividualView() {
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**Individual Report**", "");
        System.out.printf("|%-25s%-50s%-25s|\n", "", "**!Enter 0 in ID to Exit!**", "");
        System.out.print("|\tEnter ID to View: ");

        int id;
        while (true) {
            try {
                id = input.nextInt();
                if (doesIDexists(id, conf)) {
                    break;
                } else if (id == 0) {
                    exit = false;
                    break;
                } else {
                    System.out.print("|\tEnter ID to View Again: ");
                }
            } catch (Exception e) {
                input.next();
                System.out.print("|\tEnter ID to View Again: ");
            }
        }

        if (exit) {
            try {
                String clientSQL = "SELECT C_fname, C_mname, C_lname, C_gender, C_Contact, C_Birth_Date, C_Balance, C_Update_Date " +
                                   "FROM C_Clients WHERE C_Id = ?";
                PreparedStatement clientStmt = conf.connectDB().prepareStatement(clientSQL);
                clientStmt.setInt(1, id);
                ResultSet clientRs = clientStmt.executeQuery();

                if (clientRs.next()) {
                    System.out.println("+----------------------------------------------------------------------------------------------------+");
                    System.out.printf("|%-25s%-50s%-25s|\n", "", "Individual Client Information", "");
                    System.out.printf("|%-15s: %-83s|\n", "First Name", clientRs.getString("C_fname"));
                    System.out.printf("|%-15s: %-83s|\n", "Middle Name", clientRs.getString("C_mname"));
                    System.out.printf("|%-15s: %-83s|\n", "Last Name", clientRs.getString("C_lname"));
                    System.out.printf("|%-15s: %-83s|\n", "Gender", clientRs.getString("C_gender"));
                    System.out.printf("|%-15s: %-83s|\n", "Contact", clientRs.getString("C_Contact"));
                    System.out.printf("|%-15s: %-83s|\n", "Birth Date", clientRs.getString("C_Birth_Date"));
                    System.out.printf("|%-15s: %-83s|\n", "Balance", clientRs.getString("C_Balance"));
                    System.out.printf("|%-15s: %-83s|\n", "Updated Date", clientRs.getString("C_Update_Date"));
                    System.out.println("+----------------------------------------------------------------------------------------------------+");

                    String loanSQL = "SELECT L_Transaction_Date, L_Status, L_Amount, L_Loaned_Balance, L_Loan_Rate " +
                                     "FROM Loan_History WHERE C_Id = ?";
                    PreparedStatement loanStmt = conf.connectDB().prepareStatement(loanSQL);
                    loanStmt.setInt(1, id);
                    ResultSet loanRs = loanStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Loan History**", "");
                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-22s | %-22s | %-22s | %-23s | %-23s |\n", "Transaction Date", "Status", "Amount", "Loaned Balance", "Loan Rate");
                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");

                    boolean hasLoans = false;
                    while (loanRs.next()) {
                        hasLoans = true;
                        System.out.printf("| %-22s | %-22s | %-22s | %-23s | %-23s |\n",
                                loanRs.getString("L_Transaction_Date"),
                                loanRs.getString("L_Status"),
                                loanRs.getString("L_Amount"),
                                loanRs.getString("L_Loaned_Balance"),
                                loanRs.getString("L_Loan_Rate"));
                    }

                    if (!hasLoans) {
                        System.out.printf("|%-25s%-50s%-25s|\n", "", "!!No Loan History!!", "");
                    }

                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");

                    String paymentSQL = "SELECT P_Transaction_Date, P_Status, P_Paid_Amount, P_Change, P_Loaned_Left " +
                                        "FROM Payment_History WHERE C_Id = ?";
                    PreparedStatement paymentStmt = conf.connectDB().prepareStatement(paymentSQL);
                    paymentStmt.setInt(1, id);
                    ResultSet paymentRs = paymentStmt.executeQuery();

                    System.out.printf("|%-25s%-50s%-25s|\n", "", "**Payment History**", "");
                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");
                    System.out.printf("| %-22s | %-22s | %-22s | %-23s | %-23s |\n", "Transaction Date", "Status", "Paid Amount", "Change", "Loaned Left");
                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");

                    boolean hasPayments = false;
                    while (paymentRs.next()) {
                        hasPayments = true;
                        System.out.printf("| %-22s | %-22s | %-22s | %-23s | %-23s |\n",
                                paymentRs.getString("P_Transaction_Date"),
                                paymentRs.getString("P_Status"),
                                paymentRs.getString("P_Paid_Amount"),
                                paymentRs.getString("P_Change"),
                                paymentRs.getString("P_Loaned_Left"));
                    }

                    if (!hasPayments) {
                        System.out.printf("|%-25s%-50s%-25s|\n", "", "!!No Payment History!!", "");
                    }

                    System.out.println("+------------------------------------------------------------------------------------------------------------------------------+");

                    clientRs.close();
                    loanRs.close();
                    paymentRs.close();
                    clientStmt.close();
                    loanStmt.close();
                    paymentStmt.close();
                } else {
                    System.out.println("|\tNo record found for ID: " + id + " |");
                }

            } catch (Exception e) {
                System.out.println("|\tError retrieving data: " + e.getMessage() + " |");
            }
        }
    }



    
    private boolean doesIDexists(int id, config conf) {
        String query = "SELECT COUNT(*) FROM C_Clients Where C_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("|\tError checking Report ID: " + e.getMessage());
        }
        return false;
    }
}