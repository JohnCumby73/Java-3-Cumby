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

    public LinkedList<Book> getBookList() {
        return bookList;
    }

    public void setBookList(LinkedList<Book> bookList) {
        this.bookList = bookList;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

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
