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
     * Builds a library object from the database, but without establishing
     * the relationships between books and authors. This method retrieves
     * all books and all authors separately, but does not link them.
     *
     * @return A {@link Library} object containing lists of {@link Book} and
     * {@link Author} objects.
     *
     * @throws SQLException If a database occurs during the retrieval of books or authors.
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


    /**
     * Builds the list of authors for each book in the library. This method
     * iterates through every book in the library, queries the database for
     * the authors associated with each book's ISBN, and then sets the author list
     * for each book.
     *
     * @throws SQLException If a database error occurs during the retrieval of authors for a book.
     */
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

    /**
     * Builds the list of books for author in the library. This method
     * iterates through every author in the library, queries the database
     * for the ISBN's associated with each author ID, and then sets the book list
     * for every author.
     *
     * @throws SQLException If a database error occurs during the retrieval of authors for a book.
     */
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

    /**
     * Finds an author in the library by their ID.
     * @param id The ID of the author to search for.
     * @return The {@link Author} object if found, or {@code null} if no author
     * with the given ID exists in the library.
     */

    public Author findAuthorById(int id) {
        for (Author author : library.getAuthors()) {
            if (author.getAuthorID() == id) {
                return author;
            }
        }
        return null;
    }

    /**
     * Finds a book in the library by its ISBN.
     * @param isbn The isbn of the book to search for.
     * @return The {@link Book} object if found, or {@code null} if no book
     * with the given ID exists in the library.
     */
    public Book findBookByIsbn(String isbn) {
        for (Book book : library.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }


    /**
     * Prints the attributes of every book in the library.
     */
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

    /**
     * Prints the attributes of every author in the library.
     */
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

    /**
     * Updates a book in the database with a new title. This method also updates the local library.
     * @param isbn The isbn of the book being updated.
     * @param newTitle The new title being given to the book.
     */
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

    /**
     * Updates a book in the database with a new edition number. This method also updates the local library.
     * @param isbn The isbn of the book being updated.
     * @param newEditionNumber The new edition number being given to the book.
     */
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

    /**
     * Updates a book in the database with a new edition number. This method also updates the local library.
     * @param isbn The isbn of the book being updated.
     * @param newCopyrightYear The new copyright year being given to the book.
     */
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

    /**
     * Updates an author in the database with a new first name. This method also updates the local library.
     * @param authorId The authorID of the author being updated.
     * @param newFirstName The new first name being given to the author.
     */
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

    /**
     * Updates an author in the database with a new last name. This method also updates the local library.
     * @param authorId The authorID of the author being updated.
     * @param newLastName The new last name being given to the author.
     */
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

    /**
     * Inserts a new book into the database, and updates the ( authors - books) bridge table. This method also updates the local library.
     * This is done in a transaction to ensure data integrity.
     * @param newIsbn New book isbn.
     * @param newTitle New book title.
     * @param newEditionNumber New book edition number.
     * @param newCopyrightYear New book copyright year.
     * @param listOfAssociatedAuthorIDs New book list of associated author IDs.
     */
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

    /**
     * Inserts a new author into the database. This method also updates the local library.
     * @param newFirstName New author first name.
     * @param newLastName New author last name.
     */
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

    /**
     * Builds a {@code LinkedList} of {@link Author} from given author IDs.
     * @param listOfAuthorIDs The list of author IDs.
     * @return A {@code LinkedList} object if successful, or {@code null} if no authorIDs were given.
     */
    public LinkedList<Author> buildListOfAuthorsByIds(ArrayList<Integer> listOfAuthorIDs) {
        LinkedList<Author> authors = new LinkedList<>();

        if (listOfAuthorIDs != null) {
            for (int authorID : listOfAuthorIDs) {
                Author author = findAuthorById(authorID);
                if (author != null) {
                    authors.add(author);
                }
            }
            return authors;
        } else {
            return null;
        }
    }

    /**
     * Checks if an isbn exists in the local library.
     * @param isbn The isbn to look for.
     * @return {@code true} if a book with the given ISBN exists in the library, {@code false} otherwise.
     */
    public boolean isbnExists(String isbn) {
        for (Book book : library.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an author exists in the local library.
     * @param authorId The authorID of the author to look for.
     * @return {@code true} if an author with the given authorID exists in the library, {@code false} otherwise.
     */
    public boolean authorExists(int authorId) {
        for (Author author : library.getAuthors()) {
            if (author.getAuthorID() == authorId) {
                return true;
            }
        }
        return false;
    }
}
