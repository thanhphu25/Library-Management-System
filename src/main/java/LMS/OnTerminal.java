package LMS;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the terminal interface for the Library Management System (LMS).
 */
public class OnTerminal {
    public static final int lineOnScreen = 20;
    private static Boolean onTest = false;
    private static Library library = Library.getInstance();
    private static Scanner scanner = new Scanner(System.in);

    /**
     * The main entry point for the terminal application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Connection connection = initialize(library);
        if (connection == null) {
            System.out.println("\nError connecting to Database. Exiting.");
            return;
        }

        try {
            runMainLoop(library);

            if (!onTest) {
                library.fillItBack(connection);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("\nExiting...\n");
        } finally {
            cleanup(connection);
        }
    }

    /**
     * Initializes the library and establishes a database connection.
     *
     * @param lib The library instance.
     * @return The database connection.
     */
    public static Connection initialize(Library lib) {
        try {
            Connection connection = lib.makeConnection();
            if (connection == null) {
                System.out.println("\nError connecting to Database. Exiting.");
                return null;
            }
            lib.populateLibrary(connection);
            return connection;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Cleans up resources by closing the database connection.
     *
     * @param connection The database connection to close.
     */
    public static void cleanup(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Runs the main loop of the terminal application.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void runMainLoop(Library library) throws IOException {
        boolean stop = false;
        while (!stop) {
            clearScreen();
            displayMainMenu();
            int choice = takeInput(0, 4);

            switch (choice) {
                case 1:
                    handleLogin(library);
                    break;
                case 2:
                    handleAccountCreation(library);
                    break;
                case 3:
                    stop = true;
                    System.out.println("\nExiting...\n");
                    break;
            }

            System.out.println("\nPress any key to continue..\n");
            scanner.nextLine();
        }
    }

    /**
     * Displays the main menu of the terminal application.
     */
    private static void displayMainMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("\tWelcome to Library Management System");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: \n");
        System.out.println("1- Login");
        System.out.println("2- Create new account");
        System.out.println("3- Exit");
        System.out.println("-----------------------------------------\n");
    }

    /**
     * Handles the account creation process.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleAccountCreation(Library library) throws IOException {
        System.out.println("Choose work session: ");
        System.out.println("1- Borrower.");
        System.out.println("2- Librarian.");
        System.out.println("3- Back.");

        int ch = takeInput(0, 4);
        if (ch == 1) {
            library.createBorrower();
        } else if (ch == 2) {;
            System.out.print("Please enter system's password: ");
            String pass = scanner.next();
            scanner.nextLine();
            if (pass.equals(Library.LMS_PASSWORD)) {
                library.createLibrarian();
            } else {
                System.out.println("Wrong password.");
            }
        }
    }

    /**
     * Handles the login process for the user.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleLogin(Library library) throws IOException {
        Person user = library.Login();
        if (user instanceof Borrower) {
            runBorrowerPortal(user);
        } else if (user instanceof Librarian) {
            runLibrarianPortal(user);
        }
    }

    /**
     * Runs the borrower portal.
     *
     * @param user The borrower user.
     * @throws IOException If an input or output exception occurred.
     */
    private static void runBorrowerPortal(Person user) throws IOException {
        while (true) {
            clearScreen();
            displayBorrowerMenu();
            int choice = takeInput(0, 7);
            if (choice == 6) break;
            allFunctionalities(user, choice);
        }
    }

    /**
     * Runs the librarian portal.
     *
     * @param user The librarian user.
     * @throws IOException If an input or output exception occurred.
     */
    private static void runLibrarianPortal(Person user) throws IOException {
        while (true) {
            clearScreen();
            displayLibrarianMenu();
            int choice = takeInput(0, 16);
            if (choice == 15) break;
            allFunctionalities(user, choice);
        }
    }

    /**
     * Displays the borrower menu.
     */
    private static void displayBorrowerMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("\tWelcome to Borrower's Portal");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: \n");
        System.out.println("1- Check Notifications");
        System.out.println("2- Search a Book");
        System.out.println("3- Place a Book on hold");
        System.out.println("4- Check Personal Info of Borrower");
        System.out.println("5- Check Hold Requests Queue of a Book");
        System.out.println("6- Logout");
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Displays the librarian menu.
     */
    private static void displayLibrarianMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("\tWelcome to Librarian's Portal");
        System.out.println("--------------------------------------------------------");
        System.out.println("Following Functionalities are available: \n");
        System.out.println("1- Check Notifications");
        System.out.println("2- Search a Book");
        System.out.println("3- Place a Book on hold");
        System.out.println("4- Check Personal Info of Borrower");
        System.out.println("5 - Check Hold Requests Queue of a Book");
        System.out.println("6 - Check out a Book");
        System.out.println("7 - Check in a Book");
        System.out.println("8 - Renew a Book");
        System.out.println("9 - Add a new Borrower");
        System.out.println("10- Update a Borrower's Info");
        System.out.println("11- Add new Book");
        System.out.println("12- Remove a Book");
        System.out.println("13- Change a Book's Info");
        System.out.println("14- View Issued Books History");
        System.out.println("15- View All Books in Library");
        System.out.println("16- Logout");
        System.out.println("--------------------------------------------------------");
    }

    /**
     * Handles all functionalities for a given person based on the choice.
     *
     * @param person The person (Borrower or Librarian) performing the action.
     * @param choice The choice of functionality to perform.
     * @throws IOException If an input or output exception occurred.
     */
    public static void allFunctionalities(Person person, int choice) throws IOException {
        Library library = Library.getInstance();

        switch (choice) {
            case 1:
                handleNotifications(person);
                break;
            case 2:
                library.searchForBooks();
                break;
            case 3:
                handleHoldRequest(library, person);
                break;
            case 4:
                handlePersonalInfo(library, person);
                break;
            case 5:
                handleHoldRequestQueue(library);
                break;
            case 6:
                handleBookIssue(library, person);
                break;
            case 7:
                handleBookReturn(library, person);
                break;
            case 8:
                library.createBorrower();
                break;
            case 9:
                handleBorrowerInfoUpdate(library);
                break;
            case 10:
                handleBookCreation(library);
                break;
            case 11:
                handleBookRemoval(library);
                break;
            case 12:
                handleBookInfoChange(library);
                break;
            case 13:
                library.viewHistory();
                break;
            case 14:
                library.viewAllBooks();
                break;
        }

        System.out.println("\nPress Q and Enter to continue!\n");
        scanner.nextLine();
    }

    /**
     * Takes input from the user within a specified range.
     *
     * @param min The minimum valid input value.
     * @param max The maximum valid input value.
     * @return The valid input value.
     */
    public static int takeInput(int min, int max) {
        while (true) {
            System.out.print("Please enter your choice: ");
            String choice = scanner.next();
            scanner.nextLine();

            try {
                int intChoice = Integer.parseInt(choice);
                if (intChoice > min && intChoice < max) {
                    return intChoice;
                } else {
                    System.out.println("Invalid input. Please enter an integer between " + (min + 1) + " and " + (max - 1) + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }


    /**
     * Handles the creation of a new book.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleBookCreation(Library library) throws IOException {
        System.out.println("\nEnter Title:");
        String title = scanner.nextLine();

        System.out.println("\nEnter Description:");
        String description = scanner.nextLine();

        System.out.println("\nEnter Author:");
        String author = scanner.nextLine();

        library.createBook(title, description, author);
    }

    /**
     * Handles the change of book information.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleBookInfoChange(Library library) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            books.get(input).changeBookInfo();
        }
    }

    /**
     * Handles the book issue process.
     *
     * @param library The library instance.
     * @param person The librarian issuing the book.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleBookIssue(Library library, Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            Book book = books.get(input);

            Borrower borrower = library.findBorrower();
            if (borrower != null) {
                String out = book.issueBook(borrower, (Librarian) person);
                System.out.print(out);
                if (out.contains("Would you like to place the book on hold?")) {
                    System.out.print("(y/n) ");
                    String choice = scanner.next();

                    if (choice.equals("y") || choice.equals("Y")) {
                        System.out.print(book.makeHoldRequest(borrower));
                    }
                }
            }
        }
    }

    /**
     * Handles the removal of a book from the library.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleBookRemoval(Library library) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            System.out.print(library.removeBookfromLibrary(books.get(input)));
        }
    }

    /**
     * Handles the book return process.
     *
     * @param library The library instance.
     * @param person The librarian handling the return.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleBookReturn(Library library, Person person) throws IOException {
        Borrower borrower = library.findBorrower();
        if (borrower != null) {
            borrower.printBorrowedBooks();
            ArrayList<Loan> loans = borrower.getBorrowedBooks();
            if (!loans.isEmpty()) {
                int input = takeInput(-1, loans.size());
                Loan l = loans.get(input);
                System.out.print(l.getBook().returnBook(borrower, l, (Librarian) person));
            } else {
                System.out.println("\nThis borrower " + borrower.getName() + " has no book to return.");
            }
        }
    }

    /**
     * Handles the update of borrower information.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleBorrowerInfoUpdate(Library library) throws IOException {
        Borrower borrower = library.findBorrower();
        if (borrower != null) borrower.updateBorrowerInfo();
    }

    /**
     * Handles the hold request for a book.
     *
     * @param library The library instance.
     * @param person The person making the hold request.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleHoldRequest(Library library, Person person) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            Book book = books.get(input);

            if ("Librarian".equals(person.getClass().getSimpleName())) {
                Borrower borrower = library.findBorrower();
                if (borrower != null) {
                    System.out.print(book.makeHoldRequest(borrower));
                }
            } else {
                System.out.print(book.makeHoldRequest((Borrower) person));
            }
        }
    }

    /**
     * Handles the display of hold request queue for a book.
     *
     * @param library The library instance.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handleHoldRequestQueue(Library library) throws IOException {
        ArrayList<Book> books = library.searchForBooks();
        if (books != null) {
            int input = takeInput(-1, books.size());
            books.get(input).printHoldRequests();
        }
    }

    /**
     * Handles the notifications for a person.
     * Prints notifications and optionally clears them.
     *
     * @param person The person whose notifications are to be handled.
     */
    private static void handleNotifications(Person person) {
        person.printNotifications();
        if (!person.notifications.isEmpty()) {
            System.out.println("Do you want to clear notifications? (y/n)");
            String choice = scanner.next();
            scanner.nextLine();

            if (choice.equals("y") || choice.equals("Y")) {
                person.clearNotifications();
                clearScreen();
                System.out.println("Notifications cleared.");
            }
        }
    }

    /**
     * Handles the display of personal information.
     *
     * @param library The library instance.
     * @param person The person whose information is to be displayed.
     * @throws IOException If an input or output exception occurred.
     */
    private static void handlePersonalInfo(Library library, Person person) throws IOException {
        if ("Librarian".equals(person.getClass().getSimpleName())) {
            Borrower borrower = library.findBorrower();
            if (borrower != null) borrower.printInfo();
        } else {
            person.printInfo();
        }
    }

    /**
     * Clears the terminal screen by printing empty lines.
     */
    public static void clearScreen() {
        for (int i = 0; i < lineOnScreen; i++) {
            System.out.println();
        }
    }

    /**
     * Sets the onTest flag.
     *
     * @param onTest The flag to set.
     */
    public static void setOnTest(boolean onTest) {
        OnTerminal.onTest = onTest;
    }

    /**
     * Sets the scanner to the given scanner.
     *
     * @param scanner The scanner to set.
     */
    public static void setScanner(Scanner scanner) {
        OnTerminal.scanner = scanner;
    }

    /**
     * Gets the scanner.
     * @return The scanner.
     */
    public static Scanner getScanner() {
        return scanner;
    }
}
