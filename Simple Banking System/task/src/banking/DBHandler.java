package banking;

import java.sql.*;

public class DBHandler {

    private static String URL = "jdbc:sqlite:";
    private Connection connection;

    public DBHandler(String database) {
        this.URL = URL + database;
        this.connection = null;
    }

    public void connectDB() {
        try {
            this.connection = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite established!");
        }
        catch (java.sql.SQLException e){
            System.out.println("Cannot connect to SQLite");
        }
    }

    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists card");
            statement.executeUpdate("create table card (id INTEGER PRIMARY KEY, " +
                                                            "number TEXT, " +
                                                            "pin TEXT, " +
                                                            "balance INTEGER DEFAULT 0)"
                                    );

            System.out.println("card table successfully created!");
        }
        catch (java.sql.SQLException e){
            System.out.println("Cannot create table");
        }
    }

    public void insertToTable(int id,Account newAccount) {

        String query = "INSERT INTO card(id,number,pin,balance) VALUES(?,?,?,?)";
        try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                statement.setString(2, newAccount.getCardId());
                statement.setString(3, newAccount.getPassword());
                statement.setInt(4, newAccount.getBalance());
                statement.executeUpdate();
        }
        catch (java.sql.SQLException e){
            System.out.println("Unable to insert card");
        }

    }

    public Account queryTable(String cardId) {
        String query = "SELECT * FROM card WHERE number = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,cardId);
            ResultSet rs = statement.executeQuery();
            return new Account(rs.getString("number"),
                                rs.getString("PIN"),
                                rs.getInt("balance"));
        }
        catch (java.sql.SQLException e) {
            System.out.println("Cannot perform query");
        }
        return null;
    }

    public void deleteAccount(String cardID) {
        String query = "DELETE FROM card WHERE number = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,cardID);
            statement.executeUpdate();
        }
        catch (java.sql.SQLException e){
            System.out.println("Cannot perform account deletion");
        }
    }

    public void disconnectDB() {
        try {
            this.connection.close();
        }
        catch(java.sql.SQLException e) {
            System.out.println("Cannot disconnect from SQLite");
        }
    }
}
