package ca.nl.cna.cumby.assignments.assignment1;

import java.io.PrintStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MariaDBProperties {
    public static final String DATABASE_URL = "jdbc:mariadb://localhost:3306";
    public static final String DATABASE_USER = "root";

    public static final String DATABASE_PASSWORD = "Ferrari458537539";

    public static final String DATABASE_URL_COMPLETE = "jdbc:mariadb://locahost:3306?user="+ DATABASE_USER + "&password=" + DATABASE_PASSWORD;

    public static boolean isDriverRegistered(PrintStream printStream) {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            printStream.println("Option 1: Find the class worked!");
        } catch (ClassNotFoundException ex) {
            printStream.println("Error: unable to load driver class!");
            return false;
        } catch (IllegalAccessException ex) {
            printStream.println("Error: access problem while loading!");
            return false;
        } catch (InstantiationException ex) {
            printStream.println("Error: unable to instantiate driver!");
            return false;
        }

        //Option 2: Register the Driver
        try {
            Driver myDriver = new org.mariadb.jdbc.Driver();
            DriverManager.registerDriver(myDriver);
            printStream.println("Option 2: Register the Driver worked!");
        } catch (SQLException ex) {
            printStream.println("Error: unable to load driver class!");
            return false;
        }
        return true;
    }
}
