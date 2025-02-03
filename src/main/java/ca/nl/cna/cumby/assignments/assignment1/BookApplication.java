package ca.nl.cna.cumby.assignments.assignment1;

public class BookApplication {
    public static void main(String[] args) {
        Book book = BookDatabaseManager.getAllBooksFromDatabase();
        System.out.println(book);

    }
}
