package LMS;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OnTerminalTest {
    @Mock
    private ByteArrayInputStream inputStream;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static Connection connection = null;
    private static String output;
    private static final Library libraryMock = Library.getInstance();
    private static final String PRESS_ANY_KEY = TC.PRESS_ANY_KEY + "\n";
    private static final String EXIT = TC.MenuOption.EXIT + "\n" + PRESS_ANY_KEY;
    private static final String createBorrower = TC.SignupOption.BORROWER + "\n"
            + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
            + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
            + TC.Borrower.EMAIL + "\n";
    private static final String createLibrarian = TC.SignupOption.LIBRARIAN + "\n"
            + TC.LIBRARY_PASSWORD + "\n"
            + TC.Librarian.NAME + "\n" + TC.Librarian.PASS + "\n"
            + TC.Librarian.ADDR + "\n" + TC.Librarian.PHONE + "\n"
            + TC.Librarian.EMAIL + "\n" + TC.Librarian.SALARY + "\n";

    /**
     * Reset the output stream and input stream before each test.
     * Also, reset the connection and output variables.
     * This method is executed before each test.
     */
    @AfterEach
    public void tearDown() {
        OnTerminal.cleanup(connection);
        OnTerminal.setOnTest(false);
        connection = null;
    }

    /**
     * Set up the input stream and output stream before each test.
     * This method is executed before each test.
     *
     * @param input The input string to be passed to the input stream.
     */
    private void setup(String input) {
        inputStream = new ByteArrayInputStream(input.getBytes());
        OnTerminal.setScanner(new Scanner(inputStream));
        System.setOut(new PrintStream(new TeeOutputStream(System.out, outputStreamCaptor)));
        connection = OnTerminal.initialize(libraryMock);
    }

    @Test
    @Order(1)
    public void testInitialize() {
        Connection conn = OnTerminal.initialize(libraryMock);
        assertNotNull(conn, "Connection should be established");
        OnTerminal.cleanup(conn);
    }

    @Test
    @Order(2)
    public void testCleanup() {
        Connection conn = OnTerminal.initialize(libraryMock);
        assertNotNull(conn, "Connection should be established");
        OnTerminal.cleanup(conn);
        try {
            assertTrue(conn.isClosed(), "Connection should be closed");
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    @Order(3)
    public void testCreateNewBorrower() {
        setup(createBorrower);
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue((output.contains("Borrower with name") && output.contains("created successfully.")));
    }

    @Test
    @Order(4)
    public void testCreateAddedBorrower() {
        setup(createBorrower + createBorrower);
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue((output.contains("This email is already in use.")));
    }

    @Test
    @Order(5)
    public void testCreateNewLibrarian() {
        setup(createLibrarian);
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue((output.contains("Librarian with name") && output.contains("created successfully.")));
    }

    @Test
    @Order(6)
    public void testCreateAddedLibrarian() {
        setup(createLibrarian + createLibrarian);
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue((output.contains("This email is already in use.")));
    }

    @Test
    @Order(7)
    public void testBorrowerLogin() {
        setup(createBorrower + TC.Borrower.EMAIL + "\n" + TC.Borrower.PASS + "\n");
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            Method handleLogin = Library.class.getDeclaredMethod("Login");
            handleLogin.setAccessible(true);
            handleLogin.invoke(libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("[Borrower] Login Successful."));
    }

    @Test
    @Order(8)
    public void testLibrarianLogin() {
        setup(createLibrarian + TC.Librarian.EMAIL + "\n" + TC.Librarian.PASS + "\n");
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            Method handleLogin = Library.class.getDeclaredMethod("Login");
            handleLogin.setAccessible(true);
            handleLogin.invoke(libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("[Librarian] Login Successful."));
    }

    @Test
    @Order(9)
    public void testWrongLogin() {
        setup(createBorrower + TC.Borrower.EMAIL + "\n" + TC.Librarian.PASS + "\n");
        try {
            // Create a new borrower for testing with reflection
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            Method handleLogin = Library.class.getDeclaredMethod("Login");
            handleLogin.setAccessible(true);
            handleLogin.invoke(libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Sorry! Wrong ID or Password"));
    }

    @Test
    @Order(10)
    public void testHandlePersonalInfo() {
        setup(TC.SignupOption.BORROWER + "\n" + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n" + TC.Borrower.EMAIL + "\n");
        List<Borrower> borrowers = libraryMock.getBorrowers();
        try {
            // Create a new borrower for testing
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            for (Borrower borrower : borrowers) {
                Method handlePersonalInfo = OnTerminal.class.getDeclaredMethod("handlePersonalInfo", Library.class, Person.class);
                handlePersonalInfo.setAccessible(true);
                handlePersonalInfo.invoke(OnTerminal.class, libraryMock, borrower);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The details of the person are: ")
                && output.contains("Name: " + TC.Borrower.NAME)
                && output.contains("Address: " + TC.Borrower.ADDR)
                && output.contains("Phone No: " + TC.Borrower.PHONE)
                && output.contains("Email: " + TC.Borrower.EMAIL));
    }

    @Test
    @Order(11)
    public void testHandleBorrowerUpdate() {
        setup(createBorrower + "1\n"                    // Update the first borrower
                + "y\n" + "new" + TC.Borrower.NAME + "\n"  // Update the name
                + "y\n" + "new" + TC.Borrower.ADDR + "\n"  // Update the address
                + "y\n" + "new" + TC.Borrower.EMAIL + "\n" // Update the email
                + "y\n" + TC.Borrower.PHONE + "\n");       // Update the phone number
        try {
            // Create a new borrower for testing
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            Method handleBorrowerInfoUpdate = OnTerminal.class.getDeclaredMethod("handleBorrowerInfoUpdate", Library.class);
            handleBorrowerInfoUpdate.setAccessible(true);
            handleBorrowerInfoUpdate.invoke(OnTerminal.class, libraryMock);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Borrower is successfully updated."));
    }

    @Test
    @Order(12)
    public void testHandleBookCreation() {
        setup(TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n");
        try {
            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Book with Title " + TC.Book.TITLE + " is successfully created."));
    }

    @Test
    @Order(13)
    public void testHandleBookRemoval() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option);
        try {
            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

            Method handleBookRemoval = OnTerminal.class.getDeclaredMethod("handleBookRemoval", Library.class);
            handleBookRemoval.setAccessible(true);
            handleBookRemoval.invoke(OnTerminal.class, libraryMock);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book is successfully removed."));
    }

    @Test
    @Order(14)
    public void testPrintNotifications() {
        String createSecondBorrower = TC.SignupOption.BORROWER + "\n"
                + "Second" + TC.Borrower.NAME + "\n" + TC.Borrower.PASS + "\n"
                + TC.Borrower.ADDR + "\n" + TC.Borrower.PHONE + "\n"
                + "Second" + TC.Borrower.EMAIL + "\n";
        String addBook = TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";

        // Ensure sufficient input for all Scanner.next() calls
        setup(createLibrarian + createBorrower + createSecondBorrower
                + addBook + searchBook + option + "1\n" // Issue the book to the first borrower
                + searchBook + option                   // Hold the book for the second borrower
                + "1\n" + option                        // Return the book
                + "1\n");                               // Additional input for takeInput

        List<Borrower> borrowers = libraryMock.getBorrowers();
        Borrower firstBorrower = borrowers.get(0);
        Borrower secondBorrower = borrowers.get(1);
        try {
            // Create a new librarian and 2 new borrowers for issuing the book
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock); // Create librarian
            handleAccountCreation.invoke(OnTerminal.class, libraryMock); // Create first borrower
            handleAccountCreation.invoke(OnTerminal.class, libraryMock); // Create second borrower

            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

            Method handleBookIssue = OnTerminal.class.getDeclaredMethod("handleBookIssue", Library.class, Person.class);
            handleBookIssue.setAccessible(true);
            handleBookIssue.invoke(OnTerminal.class, libraryMock, libraryMock.getLibrarians().get(0));

            Method handleHoldRequest = OnTerminal.class.getDeclaredMethod("handleHoldRequest", Library.class, Person.class);
            handleHoldRequest.setAccessible(true);
            handleHoldRequest.invoke(OnTerminal.class, libraryMock, secondBorrower);

            Method handleBookReturn = OnTerminal.class.getDeclaredMethod("handleBookReturn", Library.class, Person.class);
            handleBookReturn.setAccessible(true);
            handleBookReturn.invoke(OnTerminal.class, libraryMock, libraryMock.getLibrarians().get(0));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        secondBorrower.printNotifications();
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("is now available."));
    }

    @Test
    @Order(15)
    public void testHandleBookCheckoutAndCheckin() {
        String addBook = "Check " + TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + "Check " + TC.Book.TITLE + "\n";
        String option = "0\n";

        setup(createLibrarian + createBorrower + addBook + searchBook + option + "1\n" + "1\n" + option); // Issue the book to and take it back from the first borrower
        Librarian librarian = libraryMock.getLibrarians().get(0); // Get the first librarian
        Borrower borrower = libraryMock.getBorrowers().get(0); // Get the first borrower

        try {
            // Create a new librarian and a new borrower for issuing the book
            Method handleAccountCreation = OnTerminal.class.getDeclaredMethod("handleAccountCreation", Library.class);
            handleAccountCreation.setAccessible(true);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);
            handleAccountCreation.invoke(OnTerminal.class, libraryMock);

            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

            // Issue the book
            Method handleBookIssue = OnTerminal.class.getDeclaredMethod("handleBookIssue", Library.class, Person.class);
            handleBookIssue.setAccessible(true);
            handleBookIssue.invoke(OnTerminal.class, libraryMock, librarian);

            // Return the book
            Method handleBookReturn = OnTerminal.class.getDeclaredMethod("handleBookReturn", Library.class, Person.class);
            handleBookReturn.setAccessible(true);
            handleBookReturn.invoke(OnTerminal.class, libraryMock, librarian);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + "Check " + TC.Book.TITLE + " is successfully issued to " + borrower.getName()));
        assertTrue(output.contains("The book " + "Check " + TC.Book.TITLE + " is successfully returned by " + borrower.getName() + ".")
                && output.contains("Received by: " + librarian.getName()));
    }

    @Test
    @Order(16)
    public void testHandleBookUpdate() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option
                + "y\n" + "new " + TC.Book.AUTHOR + "\n" // Update the author
                + "y\n" + "new " + TC.Book.DESCRIPTION + "\n"// Update the DESCRIPTION
                + "y\n" + "new " + TC.Book.TITLE + "\n"  // Update the title
                + searchBook);
        try {
            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

            Method handleBookInfoChange = OnTerminal.class.getDeclaredMethod("handleBookInfoChange", Library.class);
            handleBookInfoChange.setAccessible(true);
            handleBookInfoChange.invoke(OnTerminal.class, libraryMock);

            libraryMock.searchForBooks();

        } catch (IOException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Book is successfully updated."));
    }

    @Test
    @Order(17)
    public void testHandleHoldRequest() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option);
        List<Borrower> borrowers = libraryMock.getBorrowers();
        Borrower borrower = borrowers.get(new Random().nextInt(borrowers.size()));
        try {
            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

            Method handleHoldRequest = OnTerminal.class.getDeclaredMethod("handleHoldRequest", Library.class, Person.class);
            handleHoldRequest.setAccessible(true);
            handleHoldRequest.invoke(OnTerminal.class, libraryMock, borrower);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + TC.Book.TITLE + " has been successfully placed on hold by borrower"));
    }

    @Test
    @Order(18)
    public void testHandleHoldRequestQueue() {
        String addBook = TC.Book.TITLE + "\n" + TC.Book.DESCRIPTION + "\n" + TC.Book.AUTHOR + "\n";
        String searchBook = TC.PortalOption.SEARCH_BY_TITLE + "\n" + TC.Book.TITLE + "\n";
        String option = "0\n";
        setup(addBook + searchBook + option + searchBook + option);
        List<Borrower> borrowers = libraryMock.getBorrowers();
        Borrower borrower = borrowers.get(new Random().nextInt(borrowers.size()));
        try {
            // Create a new book for testing with reflection
            Method handleBookCreation = OnTerminal.class.getDeclaredMethod("handleBookCreation", Library.class);
            handleBookCreation.setAccessible(true);
            handleBookCreation.invoke(OnTerminal.class, libraryMock);

            Method handleHoldRequest = OnTerminal.class.getDeclaredMethod("handleHoldRequest", Library.class, Person.class);
            handleHoldRequest.setAccessible(true);
            handleHoldRequest.invoke(OnTerminal.class, libraryMock, borrower);

            Method handleHoldRequestQueue = OnTerminal.class.getDeclaredMethod("handleHoldRequestQueue", Library.class);
            handleHoldRequestQueue.setAccessible(true);
            handleHoldRequestQueue.invoke(OnTerminal.class, libraryMock);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            fail("NoSuchMethodException should not be thrown: " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail("IllegalAccessException should not be thrown: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            fail("InvocationTargetException should not be thrown: " + e.getMessage());
        }

        output = outputStreamCaptor.toString();
        assertTrue(output.contains("The book " + TC.Book.TITLE + " has been successfully placed on hold by borrower"));

    }

    @Test
    @Order(19)
    public void testSearchForBook() {
        setup(TC.PortalOption.SEARCH_BY_TITLE + "\n" + "" + "\n");
        try {
            libraryMock.searchForBooks();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }

        output = outputStreamCaptor.toString();
        // assertTrue(output.contains("Sorry. No Books were found related to your query."));
        assertTrue(output.contains("These books are found: "));
    }

    @Test
    @Order(20)
    public void testExit() {
        inputStream = new ByteArrayInputStream(EXIT.getBytes());
        System.setIn(inputStream);
        System.setOut(new PrintStream(new TeeOutputStream(System.out, outputStreamCaptor)));
        OnTerminal.setOnTest(true);
        OnTerminal.main(new String[]{});
        output = outputStreamCaptor.toString();
        assertTrue(output.contains("Exiting"));
    }
}