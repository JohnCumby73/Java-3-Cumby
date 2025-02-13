package ca.nl.cna.cumby.assignments.assignment1;

import java.util.LinkedList;

public class Library {
    // Create an ArrayList of books and authors.
    LinkedList<Book> books;
    LinkedList<Author> authors;

    public Library(LinkedList<Book> books, LinkedList<Author> authors) {
        this.books = books;
        this.authors = authors;
    }

    public LinkedList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(LinkedList<Author> authors) {
        this.authors = authors;
    }

    public LinkedList<Book> getBooks() {
        return books;
    }

    public void setBooks(LinkedList<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
    }
    public void addAuthor(Author author) {
        authors.add(author);
    }
}
