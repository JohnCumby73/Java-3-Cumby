package ca.nl.cna.cumby.assignments.assignment1;

import java.util.List;

public class Book {
    private List<Author> authorList;
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
}
