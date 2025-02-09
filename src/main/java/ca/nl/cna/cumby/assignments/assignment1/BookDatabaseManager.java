package ca.nl.cna.cumby.assignments.assignment1;

import java.util.LinkedList;
import java.sql.*;

import static ca.nl.cna.cumby.assignments.assignment1.MariaDBProperties.*;

public class BookDatabaseManager {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static final String DB_NAME = "books";
    public static final String GETBOOKS_QUERY = "SELECT * FROM titles";
    public static final String GETAUTHORS_QUERY = "SELECT * FROM authors";
    public static final String BUILDLISTOFAUTHORS = "SELECT authorID FROM authorisbn WHERE isbn = ";
    public static final String BUILDLISTOFBOOKS = "SELECT isbn FROM authorisbn WHERE authorID =  ";


    /**
     * This method queries the database and returns a Library object.
     * @return Library
     */
    Library buildUpLibraryWithoutRelationships() {
        LinkedList<Book> books = new LinkedList<>();
        LinkedList<Author> authors = new LinkedList<>();


        // Build up list of books from database without list of authors associated with each book.
        try {
            Connection conn = DriverManager.getConnection(
                    DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery(GETBOOKS_QUERY);

            while (rs1.next()) {
                // Retrieve by column name
                String isbn = rs1.getString("isbn");
                String title = rs1.getString("title");
                int editionNumber = rs1.getInt("editionNumber");
                String copyright = rs1.getString("copyright");
                Book book = new Book(isbn, title, editionNumber, copyright);
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Build up list of authors from database without list of books associated with each author.

        try {
            Connection conn = DriverManager.getConnection(
                    DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs2 = stmt.executeQuery(GETAUTHORS_QUERY);

            while (rs2.next()) {
                // Retrieve by column name
                int authorID = rs2.getInt("authorID");
                String firstName = rs2.getString("firstName");
                String lastName = rs2.getString("lastName");
                Author author = new Author(authorID, firstName, lastName);
                authors.add(author);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Library(books, authors);
    }

    Library library = buildUpLibraryWithoutRelationships();

    public void buildListOfAuthorsForEachBook() {
        // Loop through every book in the library so you can access each isbn.
        for (Book book : library.getBooks()) {
            LinkedList<Author> authors = new LinkedList<>();
            try {
                Connection conn = DriverManager.getConnection(
                        DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs3 = stmt.executeQuery(BUILDLISTOFAUTHORS + "'" + book.getIsbn() + "'");

            while (rs3.next()) {
                // Build a LinkedList of authors to add to the book object
                Author authorToAddToLinkedList = findAuthorById(rs3.getInt("authorID"));
                authors.add(authorToAddToLinkedList);
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            book.setAuthorList(authors);
        }
    }

    public void buildListOfBooksForEachAuthor() {
        // Loop through every author in the library so you can access each authorID.
        for (Author author : library.getAuthors()) {
            LinkedList<Book> books = new LinkedList<>();
            try {
                Connection conn = DriverManager.getConnection(
                        DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs4 = stmt.executeQuery(BUILDLISTOFBOOKS + "'" + author.getAuthorID() + "'");

            while (rs4.next()) {
                // Build a LinkedList of books to add to the author object.
                Book bookToAddToLinkedList = findBookByIsbn(rs4.getString("isbn"));
                books.add(bookToAddToLinkedList);
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            author.setBookList(books);
        }

    }

    public Author findAuthorById(int id) {
        for (Author author : library.getAuthors()) {
            if (author.getAuthorID() == id) {
                return author;
            }
        }
        return null;
    }

    public Book findBookByIsbn(String isbn) {
        for (Book book : library.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }


    public void printAllBooks() {
        System.out.println();
        for (Book book : library.getBooks()) {
            System.out.print(ANSI_GREEN + "ISBN: " + ANSI_RESET + book.getIsbn() +
                    ANSI_GREEN + " Title: " + ANSI_RESET + book.getTitle() + ANSI_GREEN +
                    " Edition Number: " + ANSI_RESET + book.getEditionNumber() + ANSI_GREEN +
                    " Copyright: " + ANSI_RESET + book.getCopyright() + ANSI_GREEN + " List of authors: " + ANSI_RESET);
            for (Author author : book.getAuthorList()) {
                System.out.print(author.getFirstName() + " " + author.getLastName() + ", ");
            }
            System.out.println();
        }


        System.out.println();
    }

    public void printAllAuthors() {
        System.out.println();
        for (Author author : library.getAuthors()) {
            System.out.println(ANSI_GREEN + "First Name: " + ANSI_RESET + author.getFirstName() +
                    ANSI_GREEN + " Last Name: " + ANSI_RESET + author.getLastName() + ANSI_GREEN +
                    " Author ID: " + ANSI_RESET +author.getAuthorID() + ANSI_GREEN + " List of books: " + ANSI_RESET);
            for (Book book : author.getBookList()) {
                System.out.println(book.getTitle() + ", ");
            }
        }
        System.out.println();
    }


    // Method to print all books

}
