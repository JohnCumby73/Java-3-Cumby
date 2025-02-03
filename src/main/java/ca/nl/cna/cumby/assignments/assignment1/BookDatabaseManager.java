package ca.nl.cna.cumby.assignments.assignment1;

import java.util.List;
import java.sql.*;

public class BookDatabaseManager {
    private List<Book> booksList;
    private List<Author> authorsList;

    public static final String DB_NAME = "books.db";
    public static final String GETBOOKS_QUERY = "SELECT * FROM books LIMIT 1";
    public static final String GETAUTHORS_QUERY = "SELECT * FROM authors";



    public void addBookToDatabase(Book book) {

    }
    public void addAuthorToDatabase(Author author) {

    }

    public void createBook(String isbn, String title, int editionNumber, String copyright) {

    }

    public void createAuthor(int authorID, String firstName, String lastName) {

    }

    public static Book getAllBooksFromDatabase() {
        Book book = null;
        try {
            Connection conn = DriverManager.getConnection(
                    DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(GETBOOKS_QUERY);

            // Extract data from result set
            while (rs.next()) {
                book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
}
