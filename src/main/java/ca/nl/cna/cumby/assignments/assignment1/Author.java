package ca.nl.cna.cumby.assignments.assignment1;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents an author with their ID, first name, last name, and a list of books they have written.
 */
public class Author {
    private LinkedList<Book> bookList;
    private int authorID;
    private String firstName;
    private String lastName;

    /**
     * Constructs a new Author object.
     *
     * @param authorID  The unique ID of the author.
     * @param firstName The author's first name.
     * @param lastName  The author's last name.
     */
    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the list of books written by the author.
     * @return The {@link LinkedList} of {@link Book} objects.
     */
    public LinkedList<Book> getBookList() {
        return bookList;
    }

    /**
     * Sets the list of books written by the author.
     * @param bookList The new {@link LinkedList} of {@link Book} objects.
     */
    public void setBookList(LinkedList<Book> bookList) {
        this.bookList = bookList;
    }

    /**
     * Gets the ID of the author.
     * @return The author's ID.
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * Sets the ID of the author.
     * @param authorID The new author ID.
     */
    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    /**
     * Gets the first name of the author.
     * @return The author's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the author.
     * @param firstName The author's new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the author.
     * @return The author's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the author.
     * @param lastName The author's new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Adds a book to the author's list of books.
     * @param book The {@link Book} object to add.
     */
    public void addBookToList(Book book) {
        bookList.add(book);
    }
}
