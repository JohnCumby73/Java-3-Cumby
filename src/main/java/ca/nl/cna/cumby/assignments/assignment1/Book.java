package ca.nl.cna.cumby.assignments.assignment1;

import java.util.ArrayList;
import java.util.LinkedList;

public class Book {
    private LinkedList<Author> authorList;
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;

    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
    }

    public LinkedList<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(LinkedList<Author> authorList) {
        this.authorList = authorList;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
