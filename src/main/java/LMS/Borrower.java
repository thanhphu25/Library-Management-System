package LMS;

import Interface.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * The Borrower class represents a person who can borrow books and place hold requests in the LMS.
 * It extends the Person class and provides functionality for managing borrowed books and hold
 * requests.
 */
public class Borrower extends Person implements Observer {
    private ArrayList<Loan> borrowedBooks;
    private ArrayList<HoldRequest> onHoldBooks;

    /**
     * Constructs a Borrower object with the given parameters.
     *
     * @param idNumber ID for the borrower, if -1 is passed, an ID will be auto-generated.
     * @param name     Name of the borrower.
     * @param password Password the borrower.
     * @param address  Address of the borrower.
     * @param email    Email address of the borrower.
     * @param phoneNum Phone number of the borrower.
     */
    public Borrower(int idNumber, String name, String password, String address, int phoneNum, String email) {
        super(idNumber, name, password, address, phoneNum, email);
        borrowedBooks = new ArrayList<>();
        onHoldBooks = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    /**
     * Prints the borrower's information, including borrowed and on-hold books.
     */
    @Override
    public void printInfo() {
        super.printInfo();
        printBorrowedBooks();
        printOnHoldBooks();
    }

    /**
     * Prints the list of books borrowed by the borrower.
     */
    public void printBorrowedBooks() {
        if (!borrowedBooks.isEmpty()) {
            System.out.println("\nBorrowed Books are: ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-5s %-50s %-30s%n", "No.", "Title", "Author");
            System.out.println("-----------------------------------------------------");

            for (int i = 0; i < borrowedBooks.size(); i++) {
                System.out.printf("%-5s ", i + "-");
                borrowedBooks.get(i).getBook().printInfo();
                System.out.println();
            }
        } else {
            System.out.println("No borrowed Books");
        }
    }

    /**
     * Prints the list of books on hold by the borrower.
     */
    public void printOnHoldBooks() {
        if (!onHoldBooks.isEmpty()) {
            System.out.println("\nOn Hold Books are: ");
            System.out.println("-----------------------------------------------------");
            System.out.printf("%-5s %-50s %-30s%n", "No.", "Title", "Author");
            System.out.println("-----------------------------------------------------");

            for (int i = 0; i < onHoldBooks.size(); i++) {
                System.out.printf("%-5s ", i + "-");
                onHoldBooks.get(i).getBook().printInfo();
                System.out.println();
            }
        } else {
            System.out.println("No On Hold Books");
        }
    }

    /**
     * Updates the borrower's name if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the name.
     * @throws IOException if an I/O error occurs.
     */
    private void updateBorrowerName(String choice) throws IOException {
        Scanner scanner = OnTerminal.getScanner();
        if (choice.equals("y")) {
            System.out.println("\nType New Name: ");
            setName(scanner.nextLine());
            System.out.println("\nThe name is successfully updated.");
        }
    }

    /**
     * Updates the borrower's address if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the address.
     * @throws IOException if an I/O error occurs.
     */
    private void updateBorrowerAddress(String choice) throws IOException {
        Scanner scanner = OnTerminal.getScanner();
        if (choice.equals("y")) {
            System.out.println("\nType New Address: ");
            setAddress(scanner.nextLine());
            System.out.println("\nThe address is successfully updated.");
        }
    }

    /**
     * Updates the borrower's email if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the email.
     * @throws IOException if an I/O error occurs.
     */
    private void updateBorrowerEmail(String choice) throws IOException {
        Scanner scanner = OnTerminal.getScanner();
        if (choice.equals("y")) {
            System.out.println("\nType New Email: ");
            setEmail(scanner.nextLine());
            System.out.println("\nThe email is successfully updated.");
        }
    }

    /**
     * Updates the borrower's phone number if the user chooses to do so.
     *
     * @param choice the user's input for whether to update the phone number.
     */
    private void updateBorrowerPhoneNumber(String choice) {
        Scanner scanner = OnTerminal.getScanner();
        if (choice.equals("y")) {
            System.out.println("\nType New Phone Number: ");
            setPhoneNo(scanner.nextInt());
            System.out.println("\nThe phone number is successfully updated.");
        }
    }

    /**
     * Updates the borrower's personal information based on user input.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void updateBorrowerInfo() throws IOException {
        String choice;

        Scanner sc = OnTerminal.getScanner();

        System.out.println("\nDo you want to update " + getName() + "'s Name ? (y/n)");
        choice = sc.next();
        sc.nextLine();
        updateBorrowerName(choice);

        System.out.println("\nDo you want to update " + getName() + "'s Address ? (y/n)");
        choice = sc.next();
        sc.nextLine();
        updateBorrowerAddress(choice);

        System.out.println("\nDo you want to update " + getName() + "'s Email ? (y/n)");
        choice = sc.next();
        sc.nextLine();
        updateBorrowerEmail(choice);

        System.out.println("\nDo you want to update " + getName() + "'s Phone Number ? (y/n)");
        choice = sc.next();
        sc.nextLine();
        updateBorrowerPhoneNumber(choice);

        System.out.println("\nBorrower is successfully updated.");
    }

    /**
     * Adds a book to the list of borrowed books.
     *
     * @param ibook the Loan object representing the borrowed book.
     */
    public void addBorrowedBook(Loan ibook) {
        borrowedBooks.add(ibook);
    }

    /**
     * Removes a book from the list of borrowed books.
     *
     * @param ibook the Loan object representing the borrowed book to be removed.
     */
    public void removeBorrowedBook(Loan ibook) {
        borrowedBooks.remove(ibook);
    }

    /**
     * Adds a hold request to the list of books on hold.
     * INFORMATION_SCHEMA
     * @param holdRequest the HoldRequest object representing the book on hold.
     */
    public void addHoldRequest(HoldRequest holdRequest) {
        onHoldBooks.add(holdRequest);
    }

    /**
     * Removes a hold request from the list of books on hold.
     *
     * @param holdRequest the HoldRequest object representing the book on hold to be removed.
     */
    public void removeHoldRequest(HoldRequest holdRequest) {
        onHoldBooks.remove(holdRequest);
    }

    /**
     * Returns the list of borrowed books.
     *
     * @return the ArrayList of Loan objects representing borrowed books.
     */
    public ArrayList<Loan> getBorrowedBooks() {
        return new ArrayList<>(borrowedBooks);
    }

    /**
     * Returns the list of books on hold.
     *
     * @return the ArrayList of HoldRequest objects representing books on hold.
     */
    public ArrayList<HoldRequest> getOnHoldBooks() {
        return new ArrayList<>(onHoldBooks);
    }

    /**
     * Compares this Borrower object to the specified object for equality.
     * Two Borrower objects are considered equal if they have the same name,
     * email, and phone number.
     *
     * @param o the object to compare with this Borrower
     * @return true if the specified object is equal to this Borrower; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Borrower borrower) {
            return borrower.getEmail().equals(getEmail());
        }
        return false;
    }

    /**
     * Returns a hash code value for this Borrower object.
     * The hash code is computed based on the name, email, and phone number
     * of the Borrower.
     *
     * @return a hash code value for this Borrower
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    /**
     * Updates this Borrower with the specified message.
     * This method adds the message to the notifications list for this Borrower.
     *
     * @param message the message to be added to the notifications
     */
    @Override
    public void update(String message) {
        // Add the message to the top of the notifications list
        notifications.add(0, message);
    }

}
