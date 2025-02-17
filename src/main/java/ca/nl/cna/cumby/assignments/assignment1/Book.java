package ca.nl.cna.cumby.assignments.assignment1;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a bok with its ISBN, title, edition number, copyright, and a list of authors.
 */
public class Book {
    private LinkedList<Author> authorList;
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;

    /**
     * Constructs a new Book object.
     *
     * @param isbn          The book's ISBN.
     * @param title         The book's title.
     * @param editionNumber The book's edition number.
     * @param copyright     The book's copyright information.
     */
    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
    }

    /**
     * Gets the list of authors associated with a book.
     * @return The {@link LinkedList} of {@link Author} objects.
     */
    public LinkedList<Author> getAuthorList() {
        return authorList;
    }

    /**
     * Sets the list of authors associated with a book.
     * @param authorList The new list of authors.
     */
    public void setAuthorList(LinkedList<Author> authorList) {
        this.authorList = authorList;
    }

    /**
     * Gets the ISBN of the book.
     * @return The ISBN of the book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     * @param isbn The new ISBN of the book.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the title of the book.
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     * @param title The new title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the edition number of the book.
     * @return The edition number of the book.
     */
    public int getEditionNumber() {
        return editionNumber;
    }

    /**
     * Sets the edition number of the book.
     * @param editionNumber The new edition number of the book.
     */
    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    /**
     * Gets the copyright of the book.
     * @return The copyright of the book.
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the copyright of the book.
     * @param copyright The new copyright of the book.
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

}
