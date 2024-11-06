package Abaquita;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.*;

public class Clients {
    Scanner input = new Scanner(System.in);
    config conf = new config();
    
    public void client(){
        boolean exit = true;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Client's List**","");
            System.out.printf("|%-5s%-95s|\n","","1. Add");
            System.out.printf("|%-5s%-95s|\n","","2. Edit");
            System.out.printf("|%-5s%-95s|\n","","3. Delete");
            System.out.printf("|%-5s%-95s|\n","","4. View");
            System.out.printf("|%-5s%-95s|\n","","5. Exit");
            System.out.printf("|%-5sEnter Choice: ","");
            int choice;
            while(true){
                try{
                    choice = input.nextInt();
                    if(choice>0 && choice<6){
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
                    add();
                    break;
                case 2:
                    view();
                    edit();
                    view();
                    break;
                case 3:
                    view();
                    delete();
                    view();
                    break;
                case 4:
                    view();
                    break;
                default:
                    exit = false;
                    break;
            }
        }while(exit);
    }
    
    private void add(){
        boolean exit = true;
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bdate;
        do{
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n","","**Add Client**","");
            System.out.print("|\tEnter First Name: ");
            String fname = input.next();
            System.out.print("|\tEnter Middle Name: ");
            String mname = input.next();
            System.out.print("|\tEnter Last Name: ");
            String lname = input.next();
            String bdate2;
            while(true){
                System.out.print("|\tEnter Birth Date (YYYY-MM-DD): ");
                try{
                    bdate2 = input.next();
                    bdate = LocalDate.parse(bdate2,dateFormat);
                    if(bdate.isBefore(cdate.minusYears(18))&&bdate.isAfter(cdate.minusYears(120))){
                        break;
                    }else{
                        System.out.printf("|%-10s%-80s%-10s|\n","","**Client Must be 18 Years Old, and Should not be Older than 120**","");
                    }
                }catch(Exception e){
                    System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
                }
            }
            String gender;
            while(true){
                System.out.print("|\tGender (Male/Female): ");
                try{
                    gender = input.next();
                    if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                        break;
                    }
                }catch(Exception e){
                    
                }
            }
            long number;
            while(true){
                System.out.print("|\tEnter Contat#: +63 ");
                try{
                    number = input.nextLong();
                    if(number>9000000000L && number<=9999999999L){
                        break;
                    }
                }catch(Exception e){
                    input.next();
                }
            }
            String Cnum = "+63 "+number;
            String SQL = "INSERT INTO C_Clients (C_fname, C_mname, C_lname, C_gender, C_Contact, C_Birth_Date, C_Update_Date) Values (?,?,?,?,?,?,?)";
            conf.addRecord(SQL, fname, mname, lname, gender, Cnum, bdate, cdate);
            exit=false;
        }while(exit);
    }
    
    private void edit(){
        boolean exit = true;
        LocalDate cdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bdate;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Edit Client Information**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID to Edit: ");
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
                    System.out.print("|\tEnter ID to Edit Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Edit Again: ");
            }
        }
        while(exit){
            System.out.print("|\tEnter New First Name: ");
            String fname = input.next();
            System.out.print("|\tEnter New Middle Name: ");
            String mname = input.next();
            System.out.print("|\tEnter New Last Name: ");
            String lname = input.next();
            String bdate2;
            while(true){
                System.out.print("|\tEnter New Birth Date (YYYY-MM-DD): ");
                try{
                    bdate2 = input.next();
                    bdate = LocalDate.parse(bdate2,dateFormat);
                    if(bdate.isBefore(cdate.minusYears(18))&&bdate.isAfter(cdate.minusYears(120))){
                        break;
                    }else{
                        System.out.printf("|%-10s%-80s%-10s|\n","","**Client Must be 18 Years Old, and Should not be Older than 120**","");
                    }
                }catch(Exception e){
                    System.out.printf("|%-20s%-60s%-20s|\n","","**Follow (YYYY-MM-DD) example (2003-01-05)**","");
                }
            }
            String gender;
            while(true){
                System.out.print("|\tNew Gender (Male/Female): ");
                try{
                    gender = input.next();
                    if(gender.equalsIgnoreCase("Male")||gender.equalsIgnoreCase("Female")){
                        break;
                    }
                }catch(Exception e){
                    
                }
            }
            long number;
            while(true){
                System.out.print("|\tEnter New Contat#: +63 ");
                try{
                    number = input.nextLong();
                    if(number>9000000000L && number<=9999999999L){
                        break;
                    }
                }catch(Exception e){
                    input.next();
                }
            }
            String Cnum = "+63 "+number;
            String SQL = "UPDATE C_Clients SET C_fname = ?, C_mname = ?, C_lname = ?, C_gender = ?, C_Contact = ?, C_Birth_Date = ?, C_Update_Date = ? Where C_Id = ?";
            conf.updateRecord(SQL, fname, mname, lname, gender, Cnum, bdate2, cdate, id);
            exit=false;
        }
    }
    public void view(){
        String tbl_view = "SELECT * FROM C_Clients";
        String[] tbl_Headers = {"ID", "First Name", "Last Name"};
        String[] tbl_Columns = {"C_Id", "C_fname", "C_lname"};
        config conf = new config();
        conf.viewRecords(tbl_view, tbl_Headers, tbl_Columns);
    }
    private void delete(){
        boolean exit = true;
        System.out.println("+----------------------------------------------------------------------------------------------------+");
        System.out.printf("|%-25s%-50s%-25s|\n","","**Delete Client Information**","");
        System.out.printf("|%-25s%-50s%-25s|\n","","**!Enter 0 in ID to Exit!**","");
        System.out.print("|\tEnter ID to Delete: ");
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
                    System.out.print("|\tEnter ID to Delete Again: ");
                }
            }catch(Exception e){
                input.next();
                System.out.print("|\tEnter ID to Delete Again: ");
            }
        }
        while(exit){
            String SQL = "DELETE FROM C_Clients Where C_Id = ?";
            conf.deleteRecord(SQL, id);
            exit = false;
        }
    }
    //validation tanan sa ubos
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
