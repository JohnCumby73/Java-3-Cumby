package ca.nl.cna.cumby.assignments.assignment1;

import java.util.LinkedList;
import java.sql.*;

import static ca.nl.cna.cumby.assignments.assignment1.MariaDBProperties.*;

public class BookDatabaseManager {

    public static final String DB_NAME = "books.db";
    public static final String GETBOOKS_QUERY = "SELECT * FROM books LIMIT 1";
    public static final String GETAUTHORS_QUERY = "SELECT * FROM authors";

    LinkedList<Book> getBooksListFromDatabase() {
        LinkedList<Book> books = null;
        try {
            Connection conn = DriverManager.getConnection(
                    DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(GETBOOKS_QUERY);

            while (rs.next()) {
                // Retrieve by column name
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                int editionNumber = rs.getInt("editionNumber");
                String copyright = rs.getString("copyright");
                Book book = new Book(isbn, title, editionNumber, copyright);
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
