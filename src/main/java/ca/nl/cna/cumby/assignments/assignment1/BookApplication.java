package ca.nl.cna.cumby.assignments.assignment1;

import java.util.Scanner;

public class BookApplication {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        BookDatabaseManager dbm = new BookDatabaseManager();

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
                        System.out.println("Enter the isbn of the book you would like to update: ");
                        String isbn = scanner.nextLine();
                        if(!dbm.isbnExists(isbn)) {
                            System.out.println(ANSI_RED + "Book does not exist !!!" + ANSI_RESET);
                        } else {
                            System.out.println(ANSI_GREEN + "Book found !!!" + ANSI_RESET);
                            System.out.println("Which attribute would you like to change:\n1. Title\n2. EditionNumber\n3. Copyright");
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            switch (choice) {
                                case 1:
                                    System.out.println("Enter new title: ");
                                    String newTitle = scanner.nextLine();
                                    dbm.sendBookTitleUpdateToDatabase(isbn, newTitle);
                                    break;
                                case 2:
                                    System.out.println("Enter new edition number: ");
                                    int newEditionNumber = scanner.nextInt();
                                    scanner.nextLine();
                                    dbm.sendBookEditionNumberUpdateToDatabase(isbn, newEditionNumber);
                                    break;
                                case 3:
                                    System.out.println("Enter new copyright: ");
                                    String newCopyright = scanner.nextLine();
                                    dbm.sendBookCopyrightUpdateToDatabase(isbn, newCopyright);
                                    break;
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Enter the id of the author you would like to update: ");
                        int authorId = scanner.nextInt();
                        scanner.nextLine();
                        if(!dbm.authorExists(authorId)) {
                            System.out.println(ANSI_RED + "Author does not exist !!!" + ANSI_RESET);
                        } else {
                            System.out.println(ANSI_GREEN + "Author found !!!" + ANSI_RESET);
                            System.out.println("Which attribute would you like to change:\n1. First Name\n2. Last Name");
                            choice = scanner.nextInt();
                            scanner.nextLine();
                            switch (choice) {
                                case 1:
                                    System.out.println("Enter new first name: ");
                                    String newFirstName = scanner.nextLine();
                                    dbm.sendAuthorFirstNameUpdateToDatabase(authorId, newFirstName);
                                    break;
                                case 2:
                                    System.out.println("Enter new last name: ");
                                    String newLastName = scanner.nextLine();
                                    dbm.sendAuthorLastNameUpdateToDatabase(authorId, newLastName);
                                    break;
                            }
                        }
                        break;
                    case 5:
                        System.out.println("5");
                        // add logic
                        break;
                    case 6:
                        System.out.println("6");
                        break;
                    case 7:
                        System.out.println("7");
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
        System.out.println("2: Print all the authors from the database (Showing the books)");
        System.out.println("3: Edit a book's attributes.");
        System.out.println("4: Edit an author's attributes.");
        System.out.println("5: Add a new book to the database.");
        System.out.println("6: Add a new author to the database.");
        System.out.println("7: Quit the application");
        System.out.print("Enter your choice: ");
    }
}
