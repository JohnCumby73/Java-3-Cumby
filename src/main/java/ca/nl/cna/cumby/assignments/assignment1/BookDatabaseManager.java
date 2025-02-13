package ca.nl.cna.cumby.assignments.assignment1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.sql.*;

import static ca.nl.cna.cumby.assignments.assignment1.MariaDBProperties.*;

public class BookDatabaseManager {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final String DB_NAME = "books";
    public static final String GETBOOKS_QUERY = "SELECT * FROM titles";
    public static final String GETAUTHORS_QUERY = "SELECT * FROM authors";
    public static final String BUILDLISTOFAUTHORS = "SELECT authorID FROM authorisbn WHERE isbn = ";
    public static final String BUILDLISTOFBOOKS = "SELECT isbn FROM authorisbn WHERE authorID =  ";

    Library library;

    public BookDatabaseManager() {
        library = buildUpLibraryWithoutRelationships();
        this.buildListOfAuthorsForEachBook();
        this.buildListOfBooksForEachAuthor();
    }

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

    public void sendBookTitleUpdateToDatabase(String isbn, String newTitle) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE titles SET title =? WHERE isbn =?")) {

            pstmt.setString(1, newTitle);
            pstmt.setString(2, isbn);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Book title updated successfully !!!" + ANSI_RESET);

                // Keep local version updated!
                Book book = findBookByIsbn(isbn);
                if (book!= null) {
                    book.setTitle(newTitle);
                }
            } else {
                System.out.println(ANSI_RED + "Operation Failed !!!" + ANSI_RESET);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendBookEditionNumberUpdateToDatabase(String isbn, int newEditionNumber) {
        String newEditionNumberString = String.valueOf(newEditionNumber);
        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE titles SET editionNumber =? WHERE isbn =?")) {

            pstmt.setString(1, newEditionNumberString);
            pstmt.setString(2, isbn);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Book edition number updated successfully !!!" + ANSI_RESET);

                // Keep local version updated!
                Book book = findBookByIsbn(isbn);
                if (book != null) {
                    book.setEditionNumber(newEditionNumber);
                }
            }   else {
                System.out.println(ANSI_RED + "Operation Failed !!!" + ANSI_RESET);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendBookCopyrightUpdateToDatabase(String isbn, String newCopyrightYear) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE titles SET copyright =? WHERE isbn =?")) {

            pstmt.setString(1, newCopyrightYear);
            pstmt.setString(2, isbn);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Book copyright updated successfully !!!" + ANSI_RESET);

                // Keep local version updated!
                Book book = findBookByIsbn(isbn);
                if (book != null) {
                    book.setCopyright(newCopyrightYear);
                }
            }   else {
                System.out.println(ANSI_RED + "Operation Failed !!!" + ANSI_RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendAuthorFirstNameUpdateToDatabase(int authorId, String newFirstName) {
        String authorIdString = String.valueOf(authorId);
        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE authors SET firstName =? WHERE authorID =?")) {

            pstmt.setString(1, newFirstName);
            pstmt.setString(2, authorIdString);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Author first name updated successfully !!!" + ANSI_RESET);

                // Keep local version updated!
                Author author = findAuthorById(authorId);
                if (author != null) {
                    author.setFirstName(newFirstName);
                }
            }   else {
                System.out.println(ANSI_RED + "Operation Failed !!!" + ANSI_RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendAuthorLastNameUpdateToDatabase(int authorId, String newLastName) {
        String authorIdString = String.valueOf(authorId);
        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE authors SET lastName =? WHERE authorID =?")) {

            pstmt.setString(1, newLastName);
            pstmt.setString(2, authorIdString);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Author last name updated successfully !!!" + ANSI_RESET);

                // Keep local version updated!
                Author author = findAuthorById(authorId);
                if (author != null) {
                    author.setLastName(newLastName);
                }
            }   else {
                System.out.println(ANSI_RED + "Operation Failed !!!" + ANSI_RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendNewBookToDatabase(String newIsbn, String newTitle, int newEditionNumber, String newCopyrightYear, ArrayList<Integer> listOfAssociatedAuthorIDs) {
        String titlesTableSql = "INSERT INTO titles (isbn, title, editionNumber, copyright) VALUES (?,?,?,?)";
        String authorIsbnTableSql = "INSERT INTO authorisbn (isbn, authorID) VALUES (?,?)";


        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            PreparedStatement titlesStmt = conn.prepareStatement(titlesTableSql);
            PreparedStatement authorIsbnStmt = conn.prepareStatement(authorIsbnTableSql)) {

             conn.setAutoCommit(false); // Start transaction

            // Insert into titles table
            titlesStmt.setString(1, newIsbn);
            titlesStmt.setString(2, newTitle);
            titlesStmt.setInt(3, newEditionNumber);
            titlesStmt.setString(4, newCopyrightYear);
            int rowsAffected = titlesStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Book added successfully !!!" + ANSI_RESET);

                // Insert into authorisbn table (potentially multiple times)
                for (int authorID : listOfAssociatedAuthorIDs) {
                    authorIsbnStmt.setString(1, newIsbn);
                    authorIsbnStmt.setInt(2, authorID);
                    authorIsbnStmt.addBatch(); // Add to batch for efficient execution of multiple queries
                }

                int[] authorIsbnRowsAffected = authorIsbnStmt.executeBatch(); // Execute Batch

                // Check if all author inserts were successful (rollback the transaction if any of them fail)
                boolean allAuthorInsertsSuccessfull = true;
                for (int affected : authorIsbnRowsAffected) {
                    if (affected <= 0) {
                        allAuthorInsertsSuccessfull = false;
                        break;
                    }
                }

                if (!allAuthorInsertsSuccessfull) {
                    conn.rollback(); // Cancel transaction
                    System.out.println(ANSI_RED + "Failed to add authors. Rolling back." + ANSI_RESET);
                    return;
                }

                System.out.println(ANSI_GREEN + "Author's added to book successfully !!!" + ANSI_RESET);

                conn.commit(); // Commit transaction.

                // Keep local objects updated only if everything worked!
                Book book = new Book(newIsbn, newTitle, newEditionNumber, newCopyrightYear);

                // Add book to the library
                library.addBook(book);

                // Make the author list for it
                LinkedList<Author> authors = buildListOfAuthorsByIds(listOfAssociatedAuthorIDs);
                book.setAuthorList(authors);

                // Add the book to the authors list
                for (Author author : authors) {
                    author.addBookToList(book);
                }
            } else {
                conn.rollback(); // Rollback if operation fails for some other reason.
                System.out.println(ANSI_RED + "Operation Failed !!!" + ANSI_RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendNewAuthorToDatabase(String newFirstName, String newLastName) {
        String sql = "INSERT INTO authors (firstName, lastName) VALUES (?,?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL + DB_NAME, DATABASE_USER, DATABASE_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, newFirstName);
            pstmt.setString(2, newLastName);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(ANSI_GREEN + "Author added successfully !!!" + ANSI_RESET);

                // Get the auto-generated authorID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newAuthorID = generatedKeys.getInt(1); // Get the first generated key

                        Author author = new Author(newAuthorID, newFirstName, newLastName);
                        library.addAuthor(author);
                        author.setBookList(new LinkedList<>());

                    } else {
                        System.out.println(ANSI_RED + "Could not retrieve Author ID, internal author list not accurate !!!" + ANSI_RESET);
                    }
                }
            } else {
                System.out.println(ANSI_RED + "Could not retrieve author ID" + ANSI_RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Author> buildListOfAuthorsByIds(ArrayList<Integer> listOfAuthorIDs) {
        LinkedList<Author> authors = new LinkedList<>();
        for (int authorID : listOfAuthorIDs) {
            Author author = findAuthorById(authorID);
            if (author != null) {
                authors.add(author);
            }
        }
        return authors;
    }



    public boolean isbnExists(String isbn) {
        for (Book book : library.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public boolean authorExists(int authorId) {
        for (Author author : library.getAuthors()) {
            if (author.getAuthorID() == authorId) {
                return true;
            }
        }
        return false;
    }


}
