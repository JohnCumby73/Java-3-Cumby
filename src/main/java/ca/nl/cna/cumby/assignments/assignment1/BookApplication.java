package ca.nl.cna.cumby.assignments.assignment1;

import java.util.Scanner;

public class BookApplication {
    public static void main(String[] args) {
        BookDatabaseManager dbm = new BookDatabaseManager();
        dbm.buildListOfAuthorsForEachBook();
        dbm.buildListOfBooksForEachAuthor();

        // Create scanner object
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            printMenu();

            if (scanner.hasNextInt()) {  // Check for valid integer input
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline character

                switch (choice) {
                    case 1:
                        // add logic
                        dbm.printAllBooks();
                        dbm.buildListOfAuthorsForEachBook();
                        break;
                    case 2:
                        dbm.printAllAuthors();
                        break;
                    case 3:
                        System.out.println("3");
                        // add logic
                        break;
                    case 4:
                        System.out.println("4");
                        // add logic
                        break;
                    case 5:
                        System.out.println("5");
                        // add logic
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Please enter an integer");
            }
        } while (choice != 5);
        scanner.close();
    }

    public static void printMenu() {
        System.out.println("\n1: Print all the books from the database (Showing the authors)");
        System.out.println("2: Print all the authors from the database (Showing the books");
        System.out.println("3: Edit a book's attributes or an authors attributes");
        System.out.println("4: Add a book to the database for existing author(s) or new author(s)");
        System.out.println("5: Quit the application");
        System.out.print("Enter your choice: ");
    }
}
