package LMS;

import java.util.Date;

/**
 * The HoldRequest class represents a request made by a borrower to hold a book.
 * It contains information about the borrower, the book, and the date the request was made.
 */
public class HoldRequest {
    Book book;
    Borrower borrower;
    Date requestDate;

    /**
     * Constructs a new HoldRequest with the specified borrower, book, and request date.
     *
     * @param borrower     the borrower who made the hold request
     * @param book       the book that is being requested to hold
     * @param requestDate the date when the hold request was made
     */
    public HoldRequest(Borrower borrower, Book book, Date requestDate) {
        this.borrower = borrower;
        this.book = book;
        this.requestDate = requestDate;
    }

    /**
     * Returns the borrower who made the hold request.
     *
     * @return the borrower who made the hold request
     */
    public Borrower getBorrower() {
        return borrower;
    }

    /**
     * Returns the book that is being requested to hold.
     *
     * @return the book that is being requested to hold
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns the date when the hold request was made.
     *
     * @return the date when the hold request was made
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Prints the hold request information, including the book title, borrower name, and request date.
     */
    public void print() {
        System.out.printf("%-30s %-30s %-30s%n", book.getTitle(), borrower.getName(), requestDate);
    }
}
