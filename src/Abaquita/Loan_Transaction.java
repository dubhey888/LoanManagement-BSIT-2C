package Abaquita;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.*;

public class Loan_Transaction {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    Clients c = new Clients();
    
    public void Manage(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Managing Clients Loan**","");    
            System.out.printf("|%-5s%-95s|\n","","1. Loan");
            System.out.printf("|%-5s%-95s|\n","","2. Pay Loan");
            System.out.printf("|%-5s%-95s|\n","","3. View History");
            System.out.printf("|%-5s%-95s|\n","","4. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<5){
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
                    c.view();
                    loan();
                    break;
                case 2:
                    view();
                    payloan();
                    break;
                case 3:
                    view2();
                    view3();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    
    private void loan(){
        boolean exit = true;
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bdate;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Make a Loan**","");    
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");    
        System.out.print("|\tEnter ID of Loaner: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            String stat = "Loaned";
            double cbalance = getCurrentBalance(id);
            double cash;
            System.out.print("|\tEnter Amount to Loan: ");
            while(true){
                try{
                    cash = input.nextDouble();
                    if(cash >= 0){
                        break;
                    }else{
                        System.out.print("|\tEnter Amount to Loan Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("|\tEnter Amount to Loan Again: ");
                }
            }

            // Prompt for loan rate
            double loanRate;
            System.out.print("|\tEnter Loan Rate (in %): ");
            while(true) {
                try {
                    loanRate = input.nextDouble();
                    if (loanRate >= 0) {
                        break;
                    } else {
                        System.out.print("|\tEnter Loan Rate Again: ");
                    }
                } catch (Exception e) {
                    input.next();
                    System.out.print("|\tEnter Loan Rate Again: ");
                }
            }
            
            double interest = (loanRate / 100) * cash;
            double totalLoan = cash + interest;
            cbalance += totalLoan;
            String lrate = loanRate+"%";
            
            String SQL = "INSERT INTO Loan_History (C_Id, L_Transaction_Date, L_Status, L_Amount, L_Loaned_Balance, L_Loan_Rate) Values (?,?,?,?,?,?)";
            conf.addRecord(SQL, id, cdate, stat, cash, cbalance, lrate);
            String SQL2 = "UPDATE C_Clients SET C_Balance = ?, C_Update_Date = ? Where C_Id = ?";
            conf.updateRecord(SQL2, cbalance, cdate, id);
            exit = false;
        }
    }
    
    private void payloan(){
        boolean exit = true;
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bdate;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Pay Loaned**","");    
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");    
        System.out.print("|\tEnter ID of Loaner: ");
        int id;
        while(true){
            try{
                id = input.nextInt();
                if(doesIDexists(id, conf)){
                    break;
                }else if(id == 0){
                    exit = false;
                    break;
                }else{
                    System.out.print("|\tEnter ID Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID Again: ");
            }
        }
        while(exit){
            String stat = "Paid";
            double cbalance = getCurrentBalance(id);
            double cash;
            System.out.print("|\tEnter Amount to Pay: ");
            while(true){
                try{
                    cash = input.nextDouble();
                    if(cash >= 0){
                        break;
                    }else{
                        System.out.print("|\tEnter Amount to Pay Again: ");
                    }
                }catch(Exception e){
                    input.next();
                    System.out.print("|\tEnter Amount to Pay Again: ");
                }
            }
            double Gcash;
            if(cbalance >= cash){
                cbalance -= cash;
                String SQL = "INSERT INTO Payment_History (C_Id, P_Transaction_Date, P_Status, P_Paid_Amount, P_Change, P_Loaned_Left) Values (?,?,?,?,?,?)";
                conf.addRecord(SQL, id, cdate, stat, cash, 0.0, cbalance);
                String SQL2 = "UPDATE C_Clients SET C_Balance = ?, C_Update_Date = ? Where C_Id = ?";
                conf.updateRecord(SQL2, cbalance, cdate, id);
            }else{
                Gcash = cash - cbalance;
                String SQL = "INSERT INTO Payment_History (C_Id, P_Transaction_Date, P_Status, P_Paid_Amount, P_Change, P_Loaned_Left) Values (?,?,?,?,?,?)";
                conf.addRecord(SQL, id, cdate, stat, cash, Gcash, 0.0);
                String SQL2 = "UPDATE C_Clients SET C_Balance = ?, C_Update_Date = ? Where C_Id = ?";
                conf.updateRecord(SQL2, 0.0, cdate, id);
            }
            exit = false;
        }
    }
    
    public void view(){
        String tbl_view = "SELECT * FROM C_Clients";
        String[] tbl_Headers = {"ID", "First Name", "Last Name", "Balance"};
        String[] tbl_Columns = {"C_Id", "C_fname", "C_lname", "C_Balance"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    public void view2(){
        String tbl_view = "SELECT * FROM Loan_History";
        String[] tbl_Headers = {"ID", "Client ID", "Status", "Loaned Date"};
        String[] tbl_Columns = {"L_Id", "C_Id", "L_Status", "L_Transaction_Date"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    public void view3(){
        String tbl_view = "SELECT * FROM Payment_History";
        String[] tbl_Headers = {"ID", "Client ID", "Status", "Payment Date"};
        String[] tbl_Columns = {"P_Id", "C_Id", "P_Status", "P_Transaction_Date"};
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    
    // validation tanan ubos
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

    private double getCurrentBalance(int id) {
        double balance = 0.0;
        String query = "SELECT C_Balance FROM C_Clients WHERE C_Id = ?";
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("C_Balance");
            }
        } catch (SQLException e) {
            System.out.println("|\tError retrieving balance: " + e.getMessage());
        }
        return balance;
    }
}
