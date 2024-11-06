package Abaquita;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do {
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-25s%-50s%-25s|\n", "", "**Loan Management System**", "");
            System.out.printf("|%-5s%-95s|\n", "", "1. Clients");
            System.out.printf("|%-5s%-95s|\n", "", "2. Loan Transaction");
            System.out.printf("|%-5s%-95s|\n", "", "3. Reports");
            System.out.printf("|%-5s%-95s|\n", "", "4. Exit");
            System.out.printf("|%-5sEnter Choice: ", "");
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
            switch (choice) {
                case 1:
                    Clients c = new Clients();
                    c.client();
                    break;
                case 2:
                    Loan_Transaction lt = new Loan_Transaction();
                    lt.Manage();
                    break;
                case 3:
                    Report r = new Report();
                    r.report_type();
                    break;
                default:
                    exit = false;
                    break;
            }
        } while (exit);
    }
}
