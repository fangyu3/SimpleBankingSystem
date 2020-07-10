package banking;

import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DBHandler SQLiteDB = new DBHandler(args[1]);
        SQLiteDB.connectDB();
        SQLiteDB.createTable();
        BankSystem bank = new BankSystem(SQLiteDB);
        bank.startSystem();
        SQLiteDB.disconnectDB();
    }
}
