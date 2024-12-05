package LMS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * The Library class represents a library in the Library Management System. It follows the Singleton
 * Design Pattern to ensure only one instance of the library exists. The library manages books,
 * borrowers, librarians, loans, and hold requests. It also provides methods to interact with these
 * entities.
 */
public class Library {

    public static final String LMS_PASSWORD = "LMS_Password";
    private static final String JDBC_URL = "jdbc:h2:file:";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private final ArrayList<Book> booksInLibrary;
    private static ArrayList<Borrower> borrowers;
    private static ArrayList<Librarian> librarians;
    private static ArrayList<Loan> loans;
    private static Library obj;
    private static Person user;
    private API googleAPI;

    /**
     * Represents a Library with a collection of books, librarians, and borrowers. This class is
     * responsible for managing the books available in the library, the loans of books to borrowers,
     * and the librarians who manage the library.
     * <p>
     * The constructor initializes the library with empty lists for librarians, borrowers, books, and
     * loans.
     */
    private Library() {
        librarians = new ArrayList<>();
        borrowers = new ArrayList<>();
        booksInLibrary = new ArrayList<>();
        loans = new ArrayList<>();
        this.googleAPI = new API();
    }

    /**
     * Returns the singleton instance of the Library class. If the instance does not exist, it creates
     * a new one.
     *
     * @return the singleton instance of the Library class
     */
    public static Library getInstance() {
        if (obj == null) {
            obj = new Library();
        }

        return obj;
    }

    //-----------------------------------------------------------------------------------------------//
    /* Library Management ---------------------------------------------------------------------------*/

    /**
     * Adds a new librarian to the list of librarians.
     *
     * @param librarian the Librarian object to be added
     * @return true if the librarian was added successfully, false otherwise
     */
    public static boolean addLibrarian(Librarian librarian) {
        if (!librarians.contains(librarian)) {
            librarians.add(librarian);
            return true;
        }

        Person.currentIdNumber--;
        return false;
    }

    /**
     * Retrieves the list of librarians.
     *
     * @return an ArrayList containing all the librarians.
     */
    public ArrayList<Librarian> getLibrarians() {
        return librarians;
    }

    /**
     * Creates a new librarian by prompting the user for various details such as name, password,
     * address, phone number, email, and salary. The method reads input from the console and handles
     * potential input mismatches and IO exceptions. After collecting the necessary information, it
     * creates a Librarian object and adds it to the library system. Finally, it confirms the creation
     * of the librarian and displays the email and password.
     * <p>
     * Input:
     * - Name: String
     * - Password: String
     * - Address: String
     * - Phone Number: int
     * - Email: String
     * - Salary: double
     * <p>
     * Exceptions:
     * - IOException: If an input or output exception occurs while reading from the console.
     * - InputMismatchException: If the input does not match the expected type for phone number, or
     * salary number.
     */
    public void createLibrarian() {
        Scanner scanner = OnTerminal.getScanner();

        System.out.println("\nEnter Name: ");
        String name = scanner.nextLine();

        System.out.println("\nEnter Password: ");
        String password = scanner.nextLine();

        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        int phone = 0;
        while (true) {
            try {
                System.out.println("Enter Phone Number: ");
                phone = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid Input. Please enter a valid phone number.");
                scanner.next();
            }
        }

        System.out.println("Enter Email: ");
        String email = scanner.nextLine();

        System.out.println("Enter Salary: ");
        double salary = 0.0;
        while (true) {
            try {
                salary = scanner.nextDouble();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid Input. Please enter a valid salary.");
                scanner.next();
            }
        }

        Librarian librarian = new Librarian(-1, name, password, address, phone, email, salary);
        if (addLibrarian(librarian)) {
            System.out.println("\nLibrarian with name " + name + " created successfully.");
            System.out.println("\nYour Email is : " + librarian.getEmail());
            System.out.println("Your Password is : " + librarian.getPassword());
        } else {
            System.out.println("This email is already in use.");
        }
    }

    //-----------------------------------------------------------------------------------------------//
    /* Borrower Management --------------------------------------------------------------------------*/

    /**
     * Adds a borrower to the list of borrowers.
     *
     * @param borrower the borrower to be added
     * @return true if the borrower was added successfully, false otherwise
     */
    public boolean addBorrower(Borrower borrower) {
        if (!borrowers.contains(borrower)) {
            borrowers.add(borrower);
            return true;
        }

        Person.currentIdNumber--;
        return false;
    }

    /**
     * Retrieves the list of borrowers.
     *
     * @return An ArrayList containing Borrower objects.
     */
    public ArrayList<Borrower> getBorrowers() {
        return borrowers;
    }

    /**
     * Finds a borrower by their ID.
     * <p>
     * Prompts the user to enter a borrower's ID and searches through the list of borrowers to find a
     * match. If a match is found, the corresponding borrower is returned. If the input is invalid or
     * no match is found, appropriate messages are displayed.
     * </p>
     *
     * @return the borrower with the matching ID, or {@code null} if no match is found
     */
    public Borrower findBorrower() {
        System.out.println("\nEnter Borrower's ID: ");

        int id = 0;
        Scanner scanner = OnTerminal.getScanner();

        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid Input");
            scanner.next();
        }

        Borrower borrower = findBorrowerById(id);

        if (borrower == null) {
            System.out.println("\nSorry this ID didn't match any Borrower's ID.");
        }

        return borrower;
    }

    /**
     * Logical method for finding a borrower by their ID. This method iterates through the list of
     * borrowers and returns the borrower with the matching ID. If no borrower with the specified ID
     * is found, it returns null.
     *
     * @param id the ID of the borrower to find
     * @return the borrower with the specified ID, or null if no such borrower exists
     */
    public Borrower findBorrowerById(int id) {
        for (Borrower borrower : borrowers) {
            if (borrower.getID() == id) {
                return borrower;
            }
        }

        return null;
    }

    /**
     * Finds a librarian by their ID.
     * @param name the name of the librarian to find
     * @return the librarian with the matching name, or {@code null} if no match is found
     */
    public Borrower findBorrowerByName(String name) {
        for (Borrower borrower : borrowers) {
            if (borrower.getName().equals(name)) {
                return borrower;
            }
        }

        return null;
    }

    /**
     * This method prompts the user to enter details for creating a new borrower. It collects the
     * borrower's name, password, address, phone number, and email. After collecting the information,
     * it creates a new Borrower object and adds it to the system.
     *
     * <p>Steps involved:</p>
     * <ul>
     *   <li>Prompts the user to enter their name, password, address, phone number, and email.</li>
     *   <li>Handles potential IOExceptions during input reading.</li>
     *   <li>Handles potential InputMismatchException for phone number input.</li>
     *   <li>Creates a new Borrower object with the collected information.</li>
     *   <li>Adds the new Borrower to the system.</li>
     *   <li>Prints confirmation messages with the borrower's email and password.</li>
     * </ul>
     *
     * <p>Exceptions handled:</p>
     * <ul>
     *   <li>IOException: If an input or output exception occurred.</li>
     *   <li>InputMismatchException: If the input for the phone number is not a valid integer.</li>
     * </ul>
     */
    public void createBorrower() {
        Scanner scanner = OnTerminal.getScanner();

        System.out.println("\nEnter Name: ");
        String name = scanner.nextLine();

        System.out.println("\nEnter Password: ");
        String password = scanner.nextLine();

        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        int phone = 0;
        while (true) {
            try {
                System.out.println("Enter Phone Number: ");
                phone = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid Input. Please enter a valid phone number.");
                scanner.next();
            }
        }

        System.out.println("Enter Email: ");
        String email = scanner.nextLine();

        Borrower borrower = new Borrower(-1, name, password, address, phone, email);

        if (addBorrower(borrower)) {
            System.out.println("\nBorrower with name " + name + " created successfully.");
            System.out.println("\nEmail : " + borrower.getEmail());
            System.out.println("Password : " + borrower.getPassword());
        } else {
            System.out.println("This email is already in use.");
        }
    }

    //-----------------------------------------------------------------------------------------------//
    /* Book Management ------------------------------------------------------------------------------*/

    /**
     * Adds a book to the library's collection.
     *
     * @param book the book to be added to the library
     */
    public boolean addBookinLibrary(Book book) {
        if (!booksInLibrary.contains(book)) {
            booksInLibrary.add(book);
            return true;
        }
        return false;
    }

    /**
     * Removes a book from the library if it is not currently borrowed by any borrower. If the book is
     * on hold by any borrower, prompts the user for deleting holdrequests existed first.
     *
     * @param book The book to be removed from the library.
     */
    public String removeBookfromLibrary(Book book) {
        StringBuilder output = new StringBuilder();
        boolean isIssued = book.getIssuedStatus();

        //Checking if this book is currently borrowed by some borrower
        if (isIssued) {
            output.append("This particular book is currently borrowed by some borrower.\n");
            output.append("Delete Unsuccessful.\n");
        } else {
            output.append("Currently this book is not borrowed by anyone.\n");
            ArrayList<HoldRequest> hRequests = book.getHoldRequests();

            if (!logicalRemoveBook(hRequests, book)) {
                output.append("This book is on hold by some borrower.\n");
                output.append("Please remove the hold request first.\nDelete Unsuccessful.\n");
            } else {
                output.append("The book is successfully removed.\n");
            }
        }

        return output.toString();
    }

    /**
     * Attempts to logically remove a book from the library.
     *
     * @param holdRequests the list of hold requests for the book
     * @param book         the book to be removed
     * @return true if the book was successfully removed, false if there are hold requests for the
     * book
     */
    public boolean logicalRemoveBook(ArrayList<HoldRequest> holdRequests, Book book) {
        if (!holdRequests.isEmpty()) {
            return false;
        } else {
            booksInLibrary.remove(book);
            return true;
        }
    }

    /**
     * Searches for books in the library based on the user's input criteria (Title, Description, or
     * Author).
     *
     * @return An ArrayList of Book objects that match the search criteria. If no books are found,
     * returns null.
     * @throws IOException If an input or output exception occurs.
     *                     <p>
     *                     The method prompts the user to choose a search criterion (Title, Description,
     *                     or Author) and then asks for the corresponding search term. It then
     *                     iterates through the list of books in the library and collects those that
     *                     match the search term. If matching books are found, they are printed and
     *                     returned. If no books match the search criteria, a message is displayed and
     *                     null is returned.
     */
    public ArrayList<Book> searchForBooks() throws IOException {
        String choice;
        String title = "", description = "", author = "";
        Scanner scanner = OnTerminal.getScanner();

        while (true) {
            System.out.println(
                    "\nEnter either '1' or '2' for search by Title or Author of Book respectively: ");
            choice = scanner.next();
            scanner.nextLine();

            if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                break;
            } else {
                System.out.println("\nWrong Input!");
            }
        }

        if (choice.equals("1")) {
            System.out.println("\nEnter the Title of the Book: ");
            title = scanner.nextLine();
        } else if (choice.equals("2")) {
            System.out.println("\nEnter the Author of the Book: ");
            author = scanner.nextLine();
        }

        ArrayList<Book> matchedBooks = new ArrayList<>();

        //Retrieving all the books which matched the user's search query
        for (Book book : booksInLibrary) {
            if (choice.equals("1")) {
                if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    matchedBooks.add(book);
                }
            } else if (choice.equals("2")) {
                if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                    matchedBooks.add(book);
                }
            }
        }

        //Printing all the matched Books
        if (!matchedBooks.isEmpty()) {
            System.out.println("\nThese books are found: \n");
            System.out.println(
                    "------------------------------------------------------------------------------");
            System.out.printf("%-5s %-40s %-30s %-30s\n", "No.", "Title", "Author", "Description");
            System.out.println(
                    "------------------------------------------------------------------------------");

            for (int i = 0; i < matchedBooks.size(); i++) {
                System.out.printf("%-5s ", i);
                matchedBooks.get(i).printInfo();
                System.out.print("\n");
            }

            return matchedBooks;
        } else {
            System.out.println("\nSorry. No Books were found related to your query.");
            return null;
        }
    }

    /**
     * Searches for a book in the library based on the book's id.
     *
     * @param id
     * @return the Book object if found, or null if no book with the specified title is found
     */
    public Book searchForBookByID(int id) {
        if (id > 0 && id <= booksInLibrary.size()) {
            return booksInLibrary.get(id - 1);
        }
        return null;
    }

    /**
     * Displays all the books available in the library. If the library has books, it prints the list
     * of books with their details including the index number, title, author, and description. If the
     * library is empty, it informs the user that there are no books currently.
     */
    public void viewAllBooks() {
        if (!booksInLibrary.isEmpty()) {
            System.out.println("\nBooks are: ");

            System.out.println(
                    "------------------------------------------------------------------------------");
            System.out.printf("%-5s %-40s %-30s %-30s\n", "No.", "Title", "Author", "Description");
            System.out.println(
                    "------------------------------------------------------------------------------");

            for (int i = 0; i < booksInLibrary.size(); i++) {
                System.out.printf("%-5d ", i + 1);
                booksInLibrary.get(i).printInfo();
                System.out.print("\n");
            }
        } else {
            System.out.println("\nCurrently, Library has no books.");
        }
    }

    /**
     * Retrieves the list of books available in the library.
     *
     * @return an ArrayList of Book objects representing the books in the library.
     */
    public ArrayList<Book> getBooks() {
        return booksInLibrary;
    }

    /**
     * Creates a new book with the given title, description, and author, and adds it to the library.
     *
     * @param title    the title of the book
     * @param description the description of the book
     * @param author   the author of the book
     */
    public void createBook(String title, String description, String author) {
        Book book = new Book(-1, title, description, author, false, "", "");

        if (addBookinLibrary(book)) {
            System.out.println("\nBook with Title " + book.getTitle() + " is successfully created.");
        } else {
            System.out.println("This book was already added before.");
        }
    }

    /**
     * Retrieves the list of books available in the library.
     * @param index the index of the book to retrieve
     * @param newBook the new book to replace the existing book
     */
    public void setBooksInLibrary(int index, Book newBook) {
        booksInLibrary.set(index, newBook);
    }

    //-----------------------------------------------------------------------------------------------//
    /* Loan Managemnet ------------------------------------------------------------------------------*/

    /**
     * Adds a loan to the list of loans.
     *
     * @param loan the loan to be added
     */
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    /**
     * Retrieves the list of loans.
     * @return an ArrayList of Loan objects representing the loans in the library.
     */
    public ArrayList<Loan> getLoans() {
        return loans;
    }

    /**
     * Displays the history of issued books.
     * <p>
     * This method prints a list of all issued books along with details such as the book's title,
     * borrower's name, issuer's name, issued date, receiver's name, returned date, and fine status.
     * If there are no issued books, it prints a message indicating that no books have been issued.
     */
    public void viewHistory() {
        if (!loans.isEmpty()) {
            System.out.println("\nIssued Books are: ");

            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-40s %-20s %-20s %-20s %-20s %-20s\n", "No.", "Book's Title",
                    "Borrower's Name", "Issuer's Name", "Issued Date", "Receiver's Name", "Returned Date");
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------");

            for (int i = 0; i < loans.size(); i++) {
                Loan loan = loans.get(i);
                String receiverName = (loan.getReceiver() != null) ? loan.getReceiver().getName() : "--";
                String returnDate = (loan.getReturnDate() != null) ? loan.getReturnDate().toString() : "--";
                System.out.printf("%-5d %-40s %-20s %-20s %-20s %-20s %-20s\n",
                        i + 1,
                        loan.getBook().getTitle(),
                        loan.getBorrower().getName(),
                        loan.getIssuer().getName(),
                        loan.getIssuedDate(),
                        receiverName,
                        returnDate);
            }
        } else {
            System.out.println("\nNo issued books.");
        }
    }

    //-----------------------------------------------------------------------------------------------//
    /* User Management ------------------------------------------------------------------------------*/

    /**
     * Retrieves the User
     *
     * @return the user
     */
    public Person getUser() {
        return user;
    }

    /**
     * Sets the User
     *
     * @param user the User using application
     */
    public void setUser(Person user) {
        this.user = user;
    }

    //-----------------------------------------------------------------------------------------------//
    /* Login ----------------------------------------------------------------------------------------*/

    /**
     * This method handles the login process for a borrower. It prompts the user to enter their email
     * and password, and then checks these credentials against the list of registered borrowers and
     * librarians.
     *
     * @return Person object if the login is successful, otherwise returns null.
     */
    public Person Login() {
        // Use the Scanner instance from OnTerminal
        Scanner scanner = OnTerminal.getScanner();
        String email;
        String password;

        System.out.println("\nEnter Email: ");
        email = scanner.next();
        System.out.println("Enter Password: ");
        password = scanner.next();

        Person person = logicalLogin(email, password);

        if (person == null) {
            System.out.println("\nSorry! Wrong ID or Password");
            scanner.nextLine();
        }

        return person;
    }

    /**
     * Authenticates a user based on their email and password.
     * <p>
     * This method iterates through the list of borrowers and librarians to find a match for the
     * provided email and password. If a match is found, the corresponding user (either a Borrower or
     * a Librarian) is returned. If no match is found, the method returns null.
     *
     * @param email    The email address of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return A Person object representing the authenticated user, or null if authentication fails.
     */
    public Person logicalLogin(String email, String password) {
        for (Borrower borrower : borrowers) {
            if (borrower.getEmail().equals(email) && borrower.getPassword().equals(password)) {
                System.out.println("\n[Borrower] Login Successful.");
                return borrower;
            }
        }

        for (Librarian librarian : librarians) {
            if (librarian.getEmail().equals(email) && librarian.getPassword().equals(password)) {
                System.out.println("\n[Librarian] Login Successful.");
                return librarian;
            }
        }

        return null;
    }

    //-----------------------------------------------------------------------------------------------//
    /* Database Operations --------------------------------------------------------------------------*/

    /**
     * Establishes a connection to the library database.
     *
     * @return a Connection object to the library database, or null if a SQLException occurs.
     * @throws UnsupportedEncodingException if the encoding is not supported.
     */
    @SuppressWarnings("exports")
    public Connection makeConnection() throws UnsupportedEncodingException {
        try {
            String dbPath = Paths.get("src/main/resources/Database/LibraryDB").toAbsolutePath().toString();
            return DriverManager.getConnection(JDBC_URL + dbPath, USER, PASSWORD);
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    /**
     * Populates the library with data from the database.
     * <p>
     * This method retrieves data from the database and populates the library with books, librarians,
     * borrowers, loans, and hold requests.
     *
     * @param connection The database connection to use for retrieving data.
     * @throws SQLException If a database access error occurs.
     * @throws IOException  If an I/O error occurs.
     */
    @SuppressWarnings("exports")
    public void populateLibrary(Connection connection) throws SQLException, IOException {
        Library library = this;
        Statement stmt = connection.createStatement();

        /* --- Populating Book ----*/
        String SQL = "SELECT * FROM BOOK";
        ResultSet resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("\nNo Books Found in Library");
        } else {
            int maxID = 0;

            do {
                if (resultSet.getInt("BOOK_ID") != 0 && resultSet.getString("TITLE") != null
                        && resultSet.getString("AUTHOR") != null && resultSet.getString("DESCRIPTION") != null) {
                    int id = resultSet.getInt("BOOK_ID");
                    String title = resultSet.getString("TITLE");
                    String author = resultSet.getString("AUTHOR");
                    String description = resultSet.getString("DESCRIPTION");
                    boolean issue = resultSet.getBoolean("IS_ISSUED");
                    String imageLink = resultSet.getString("IMAGE_LINK");
                    String previewLink = resultSet.getString("PREVIEW_LINK");
                    Book book = new Book(id, title, description, author, issue, imageLink, previewLink);
                    addBookinLibrary(book);

                    if (maxID < id) {
                        maxID = id;
                    }
                }
            } while (resultSet.next());

            // setting Book Count
            Book.setIDCount(maxID);
        }

        SQL = "SELECT ID, NAME, PASSWORD, ADDRESS, PHONE_NO, EMAIL, SALARY FROM PERSON INNER JOIN LIBRARIAN ON ID = LIBRARIAN_ID";

        resultSet = stmt.executeQuery(SQL);
        if (!resultSet.next()) {
            System.out.println("No Librarian Found in Library");
        } else {
            ArrayList<Book> books = library.getBooks();
            do {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String password = resultSet.getString("PASSWORD");
                String address = resultSet.getString("ADDRESS");
                int phoneNumber = resultSet.getInt("PHONE_NO");
                String email = resultSet.getString("EMAIL");
                double salary = resultSet.getDouble("SALARY");
                Librarian librarian = new Librarian(id, name, password, address, phoneNumber, email,
                        salary);

                Library.addLibrarian(librarian);

                // Attach librarians as observers to books
                for (Book book : books) {
                    book.attach(librarian);
                }
            } while (resultSet.next());

        }

        /*---Populating Borrowers (partially)!!!!!!--------*/

        SQL = "SELECT ID, NAME, PASSWORD, ADDRESS, PHONE_NO, EMAIL FROM PERSON INNER JOIN BORROWER ON ID = BORROWER_ID";

        resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("No Borrower Found in Library");
        } else {
            do {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String password = resultSet.getString("PASSWORD");
                String address = resultSet.getString("ADDRESS");
                int phoneNumber = resultSet.getInt("PHONE_NO");
                String email = resultSet.getString("EMAIL");

                Borrower borrower = new Borrower(id, name, password, address, phoneNumber, email);
                addBorrower(borrower);

            } while (resultSet.next());

        }

        /*----Populating Notifications----*/
        SQL = "SELECT * FROM NOTIFICATIONS";

        resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("No Notifications Found in Library");
        } else {
            do {
                String message = resultSet.getString("MESSAGE");
                int personID = resultSet.getInt("PERSON_ID");

                for (int i = 0; i < getBorrowers().size(); i++) {
                    if (getBorrowers().get(i).getID() == personID) {
                        getBorrowers().get(i).addNotification(message);
                    }
                }

                for (int i = 0; i < getLibrarians().size(); i++) {
                    if (getLibrarians().get(i).getID() == personID) {
                        getLibrarians().get(i).addNotification(message);
                    }
                }
            } while (resultSet.next());
        }

        /*----Populating Loan----*/

        SQL = "SELECT * FROM LOAN";

        resultSet = stmt.executeQuery(SQL);
        if (!resultSet.next()) {
            System.out.println("No Books Issued Yet!");
        } else {
            do {
                int borrowerId = resultSet.getInt("borrower_id");
                int bookId = resultSet.getInt("book_id");
                int issuerId = resultSet.getInt("i_librarian_id");
                Integer rid = (Integer) resultSet.getObject("r_librarian_id");
                int rd = 0;
                Date rdate;

                Date idate = new Date(resultSet.getTimestamp("issued_date").getTime()); // Updated

                if (rid != null) { // if there is a receiver
                    rdate = new Date(resultSet.getTimestamp("date_returned").getTime()); // Updated
                    rd = rid;
                } else {
                    rdate = null;
                }

                boolean set = true;
                Borrower borower = null;

                for (int i = 0; i < getBorrowers().size() && set; i++) {
                    if (getBorrowers().get(i).getID() == borrowerId) {
                        set = false;
                        borower = (getBorrowers().get(i));
                    }
                }

                set = true;
                Librarian[] s = new Librarian[2];

                for (int k = 0; k < librarians.size() && set; k++) {
                    if (getLibrarians().get(k).getID() == issuerId) {
                        set = false;
                        s[0] = librarians.get(k);
                    }
                }

                set = true;
                // If not returned yet...
                if (rid == null) {
                    s[1] = null;  // no receiver
                    rdate = null;
                } else {
                    for (int k = 0; k < librarians.size() && set; k++) {
                        if (librarians.get(k).getID() == rd) {
                            set = false;
                            s[1] = librarians.get(k);
                        }
                    }
                }

                set = true;

                ArrayList<Book> books = getBooks();

                for (int k = 0; k < books.size() && set; k++) {
                    if (books.get(k).getID() == bookId) {
                        set = false;
                        Loan loan = new Loan(borower, books.get(k), s[0], s[1], idate, rdate);
                        loans.add(loan);
                    }
                }

            } while (resultSet.next());
        }

        /*----Populating Hold Books----*/

        SQL = "SELECT * FROM HOLD_REQUEST";

        resultSet = stmt.executeQuery(SQL);
        if (!resultSet.next()) {
            System.out.println("No Books on Hold Yet!");
        } else {
            do {
                int borrowerId = resultSet.getInt("borrower_id");
                int bookId = resultSet.getInt("book_id");
                Date off = new Date(resultSet.getDate("request_date").getTime());

                boolean set = true;
                Borrower borower = null;

                ArrayList<Borrower> borrowers = library.getBorrowers();

                for (int i = 0; i < borrowers.size() && set; i++) {
                    if (borrowers.get(i).getID() == borrowerId) {
                        set = false;
                        borower = borrowers.get(i);
                    }
                }

                set = true;

                ArrayList<Book> books = library.getBooks();

                for (int i = 0; i < books.size() && set; i++) {
                    Book book = books.get(i);
                    if (book.getID() == bookId) {
                        set = false;
                        HoldRequest hbook = new HoldRequest(borower, book, off);
                        book.addHoldRequest(hbook);
                        borower.addHoldRequest(hbook);

                        // Setting subject - observer relationship
                        book.attach(borower);
                    }
                }
            } while (resultSet.next());
        }

        /* --- Populating Borrower's Remaining Info----*/

        // Borrowed Books
        SQL = "SELECT person.id, loan.book_ID FROM person " +
                "INNER JOIN borrower ON person.id = borrower.borrower_id " +
                "INNER JOIN loan ON borrower.borrower_id = loan.borrower_id";

        resultSet = stmt.executeQuery(SQL);

        if (!resultSet.next()) {
            System.out.println("No Borrower has borrowed yet from Library");
        } else {

            do {
                int id = resultSet.getInt("id");      // borrower
                int bookID = resultSet.getInt("book_ID"); // book

                Borrower borower = null;
                boolean set = true;

                for (int i = 0; i < library.getBorrowers().size() && set; i++) {
                    if (library.getBorrowers().get(i) != null) {
                        if (library.getBorrowers().get(i).getID() == id) {
                            set = false;
                            borower = library.getBorrowers().get(i);
                        }
                    }
                }

                set = true;

                for (int i = 0; i < loans.size() && set; i++) {
                    Loan loan = loans.get(i);
                    if (loan.getBook().getID() == bookID && loan.getReceiver() == null) {
                        set = false;
                        borower.addBorrowedBook(loan);
                        loans.get(i).getBook().setLoan(loan);
                    }
                }

            } while (resultSet.next());
        }

        ArrayList<Borrower> borrowers = library.getBorrowers();

        /* Setting Person ID Count */

        if (borrowers.size() > 0 || librarians.size() > 0) {
            SQL = "SELECT MAX(ID) AS max_id FROM PERSON";
            resultSet = stmt.executeQuery(SQL);
            if (resultSet.next()) {
                Person.setIDCount(resultSet.getInt("max_id"));
            }
        }
    }

    /**
     * Fills the database tables with the current state of the library.
     * <p>
     * This method performs the following steps:
     * 1. Clears the existing data from the tables: BOOK, BORROWER, HOLD_REQUEST, LIBRARIAN, LOAN, and
     * PERSON.
     * 2. Inserts the current borrowers and librarians into the PERSON table.
     * 3. Inserts the current librarians into the LIBRARIAN table.
     * 4. Inserts the current borrowers into the BORROWER table.
     * 5. Inserts the current books into the BOOK table.
     * 6. Inserts the current loans into the LOAN table.
     * 7. Inserts the current hold requests into the HOLD_REQUEST table.
     * 8. Inserts the currently borrowed books into the BORROWED_BOOK table.
     *
     * @param connection The database connection to use for executing the SQL statements.
     * @throws SQLException If any SQL error occurs during the execution of the statements.
     */
    @SuppressWarnings("exports")
    public static void fillItBack(Connection connection) throws SQLException {
        // Clear Tables
        String[] tables = {
                "HOLD_REQUEST", //xoa hold_request truoc de tranh tham chieu den !!
                "LOAN",
                "NOTIFICATIONS",
                "BOOK",
                "BORROWER",
                "LIBRARIAN",
                "PERSON"
        };

        for (String table : tables) {
            String template = "DELETE FROM " + table;
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.executeUpdate();
            }
        }

        Library library = Library.getInstance();

        // Filling Person's Table
        for (Borrower borrower : borrowers) {
            String template = "INSERT INTO PERSON (ID, NAME, PASSWORD, EMAIL, ADDRESS, PHONE_NO) values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, borrower.getID());
                stmt.setString(2, borrower.getName());
                stmt.setString(3, borrower.getPassword());
                stmt.setString(4, borrower.getEmail());
                stmt.setString(5, borrower.getAddress());
                stmt.setInt(6, borrower.getPhoneNo());
                stmt.executeUpdate();
            }
        }

        for (Librarian librarian : librarians) {
            String template = "INSERT INTO PERSON (ID, NAME, PASSWORD, EMAIL, ADDRESS, PHONE_NO) values (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, librarian.getID());
                stmt.setString(2, librarian.getName());
                stmt.setString(3, librarian.getPassword());
                stmt.setString(4, librarian.getEmail());
                stmt.setString(5, librarian.getAddress());
                stmt.setInt(6, librarian.getPhoneNo());
                stmt.executeUpdate();
            }
        }

        // Filling Librarian Table
        for (Librarian librarian : librarians) {
            String template = "INSERT INTO LIBRARIAN (LIBRARIAN_ID, SALARY) values (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, librarian.getID());
                stmt.setDouble(2, librarian.getSalary());
                stmt.executeUpdate();
            }
        }

        // Filling Borrower's Table
        for (Borrower borrower : borrowers) {
            String template = "INSERT INTO BORROWER (BORROWER_ID) values (?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, borrower.getID());
                stmt.executeUpdate();
            }
        }

        // Filling Book's Table
        for (Book book : library.getBooks()) {
            String template = "INSERT INTO BOOK (BOOK_ID, TITLE, AUTHOR, DESCRIPTION, IS_ISSUED, IMAGE_LINK, PREVIEW_LINK) values (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                stmt.setInt(1, book.getID());
                stmt.setString(2, book.getTitle());
                stmt.setString(3, book.getAuthor());
                stmt.setString(4, book.getDescription());
                stmt.setBoolean(5, book.getIssuedStatus());
                stmt.setString(6, book.getImageLink());
                stmt.setString(7, book.getPreviewLink());
                stmt.executeUpdate();
            }
        }

        // Filling Loan Book's Table
        for (int i = 0; i < loans.size(); i++) {
            String template = "INSERT INTO LOAN (LOAN_ID, BORROWER_ID, BOOK_ID, I_LIBRARIAN_ID, ISSUED_DATE, R_LIBRARIAN_ID, DATE_RETURNED) values (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(template)) {
                Loan loan = loans.get(i);
                stmt.setInt(1, i + 1);
                stmt.setInt(2, loan.getBorrower().getID());
                stmt.setInt(3, loan.getBook().getID());
                stmt.setInt(4, loan.getIssuer().getID());
                stmt.setTimestamp(5, new Timestamp(loan.getIssuedDate().getTime()));
                if (loan.getReceiver() == null) {
                    stmt.setNull(6, Types.INTEGER);
                    stmt.setDate(7, null);
                } else {
                    stmt.setInt(6, loan.getReceiver().getID());
                    stmt.setTimestamp(7, new Timestamp(loan.getReturnDate().getTime()));
                }
                stmt.executeUpdate();
            }
        }

        Set<String> processedRequests = new HashSet<>();
        int x = 1;
        for (Book book : library.getBooks()) {
            for (HoldRequest holdRequest : book.getHoldRequests()) {
                String key = book.getID() + "-" + holdRequest.getBorrower().getID();

                if (!processedRequests.contains(key)) {
                    String checkIdTemplate = "SELECT COUNT(*) FROM HOLD_REQUEST WHERE BOOK_ID = ? AND BORROWER_ID = ?";
                    try (PreparedStatement checkStmt = connection.prepareStatement(checkIdTemplate)) {
                        checkStmt.setInt(1, holdRequest.getBook().getID());
                        checkStmt.setInt(2, holdRequest.getBorrower().getID());

                        try (ResultSet rs = checkStmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) == 0) {
                                String insertTemplate = "INSERT INTO HOLD_REQUEST (HOLD_REQUEST_ID, BOOK_ID, BORROWER_ID, REQUEST_DATE) values (?, ?, ?, ?)";
                                try (PreparedStatement insertStmt = connection.prepareStatement(insertTemplate)) {
                                    insertStmt.setInt(1, x);
                                    insertStmt.setInt(2, holdRequest.getBook().getID());
                                    insertStmt.setInt(3, holdRequest.getBorrower().getID());
                                    insertStmt.setDate(4, new java.sql.Date(holdRequest.getRequestDate().getTime()));

                                    insertStmt.executeUpdate();

                                    processedRequests.add(key);

                                    x++;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Filling Notifications table
        int y = 1;
        for (Borrower borrower : borrowers) {
            for (String detail : borrower.getNotifications()) {
                String template = "INSERT INTO NOTIFICATIONS (NOTIFICATION_ID, PERSON_ID, MESSAGE) values (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(template)) {
                    stmt.setInt(1, y++);
                    stmt.setInt(2, borrower.getID());
                    stmt.setString(3, detail);
                    stmt.executeUpdate();
                }
            }
        }
        for (Librarian librarian : librarians) {
            for (String detail : librarian.getNotifications()) {
                String template = "INSERT INTO NOTIFICATIONS (NOTIFICATION_ID, PERSON_ID, MESSAGE) values (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(template)) {
                    stmt.setInt(1, y++);
                    stmt.setInt(2, librarian.getID());
                    stmt.setString(3, detail);
                    stmt.executeUpdate();
                }
            }
        }
    } // Filling Done!
}