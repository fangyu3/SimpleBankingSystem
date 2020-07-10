package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankSystem {
    private int accountCount;
    private Scanner scanner;
    private boolean programEnd;
    private DBHandler SQLiteDB;

    public BankSystem(DBHandler DB) {
        scanner = new Scanner(System.in);
        accountCount = 0;
        programEnd = false;
        SQLiteDB = DB;
    }

    public boolean isProgramEnd(){
        return programEnd;
    }

    public void setProgramEnd(boolean programEnd) {
        this.programEnd = programEnd;
    }

    public void startSystem() {
        while(!this.isProgramEnd()) {
            System.out.println(
                    "1. Create an account\n" +
                            "2. Log into account\n" +
                            "0. Exit");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    this.createAccount();
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    String cardId = scanner.nextLine();

                    System.out.println("Enter your PIN:");
                    String PIN = scanner.nextLine();
                    this.loginAccount(cardId,PIN);
                    break;
                case 0:
                    this.setProgramEnd(true);
                    System.out.println("Bye!");
                    break;
            }
        }
        scanner.close();
    }

    public boolean createAccount() {
        Account newAccount = new Account();

        if (SQLiteDB.queryTable(newAccount.getCardId()) != null) {
            System.out.println("Account already exists");
            return false;
        }

        System.out.println("Your card has been created\n" +
                "Your card number:\n" +
                newAccount.getCardId() + "\n" +
                "Your card PIN:\n" +
                newAccount.getPassword());

        accountCount++;
        SQLiteDB.insertToTable(accountCount,newAccount);
        return true;
    }

    public void loginAccount(String cardId, String PIN) {

        Account retrievedAccount = SQLiteDB.queryTable(cardId);
        if (retrievedAccount != null && retrievedAccount.getPassword().equals(PIN)) {
            System.out.println("You have successfully logged in!");
            ManageAccount(retrievedAccount);
        }
        else
            System.out.println("Wrong card number or PIN");
    }

    public void deleteAccount(Account account) {
        if(SQLiteDB.queryTable(account.getCardId()) != null){
            SQLiteDB.deleteAccount(account.getCardId());
            System.out.println("The account has been closed");
        }
        else
            System.out.println("Account does not exist. Cannot delete.");
    }

    public void ManageAccount(Account account) {
        boolean exit = false;

        while(!exit){
            System.out.println ("1. Balance\n" +
                                "2. Add income\n" +
                                "3. Do transfer\n" +
                                "4. Close account\n" +
                                "5. Log out\n" +
                                "0. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch(option){
                case 1:
                    System.out.println("Balance: " + account.getBalance());
                    break;
                case 2:
                    updateIncome(account);
                    break;
                case 3:
                    performTransfer(account);
                    break;
                case 4:
                    deleteAccount(account);
                    break;
                case 5:
                    System.out.println("You have successfully logged out");
                    exit = true;
                    break;
                case 0:
                    System.out.println("Bye!");
                    setProgramEnd(true);
                    exit = true;
                    break;
            }
        }
        scanner.close();
    }

    public void updateIncome(Account account) {
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        scanner.nextLine();
        if (account.updateBalance(income))
            System.out.println("Income was added!");
        else
            System.out.println("Not enough money!");
    }

    public void performTransfer(Account account) {
        System.out.println("Enter card number:");
        String targetCardId = scanner.nextLine();

        if(LuhnAlgorithmTest(targetCardId)){
            Account targetAccount = SQLiteDB.queryTable(targetCardId);
            if(targetAccount != null){
                System.out.println("Enter how much money you want to transfer:");
                int transferAmount = scanner.nextInt();
                scanner.nextLine();
                if(account.updateBalance(-transferAmount)){
                    targetAccount.updateBalance(transferAmount);
                    System.out.println("Success!");
                }
                else
                    System.out.println("Not enough money!");
            }
            else{
                System.out.println("Such a card does not exist.");
            }
        }
        else{
            System.out.println("Probably you made mistake in the card number. " +
                                "Please try again!");
        }

    }

    public boolean LuhnAlgorithmTest(String cardId) {
        List<Integer> nums = new ArrayList<>();
        for(int i=0;i<cardId.length()-1;i++){
            nums.add(Integer.parseInt("" + cardId.charAt(i)));
        }
        if(Account.LuhnAlgorithm(nums) == Integer.parseInt("" + cardId.charAt(cardId.length()-1)))
            return true;

        return false;
    }
}
