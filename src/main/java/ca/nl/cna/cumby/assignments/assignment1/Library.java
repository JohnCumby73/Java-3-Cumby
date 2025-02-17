package ca.nl.cna.cumby.assignments.assignment1;

import java.util.LinkedList;

/**
 * Represents a library containing a collection of books and authors.
 */
public class Library {
    // Create an ArrayList of books and authors.
    LinkedList<Book> books;
    LinkedList<Author> authors;

    /**
     * Constructs a new Library object.
     * @param books A {@code LinkedList} of {@link Book} objects.
     * @param authors A {@code LinkedList} of {@link Author} objects.
     */
    public Library(LinkedList<Book> books, LinkedList<Author> authors) {
        this.books = books;
        this.authors = authors;
    }

    /**
     * Gets the list of authors.
     * @return The {@link LinkedList} of {@link Author} objects.
     */
    public LinkedList<Author> getAuthors() {
        return authors;
    }

    /**
     * Sets the list of authors.
     * @param authors The new {@link LinkedList} of {@link Author} objects.
     */
    public void setAuthors(LinkedList<Author> authors) {
        this.authors = authors;
    }

    /**
     * Gets the list of books.
     * @return The {@link LinkedList} of {@link Book} objects.
     */
    public LinkedList<Book> getBooks() {
        return books;
    }

    /**
     * Sets the list of books.
     * @param books The new {@link LinkedList} of {@link Book} objects.
     */
    public void setBooks(LinkedList<Book> books) {
        this.books = books;
    }

    /**
     * Adds a book to the library.
     * @param book The new book to add to the library.
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Adds a author to the library.
     * @param author The new author to add to the library.
     */
    public void addAuthor(Author author) {
        authors.add(author);
    }
}
