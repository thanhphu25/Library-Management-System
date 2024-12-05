package LMS;

import java.util.Date;

/**
 * Represents a Loan in the library management system (LMS).
 */
public class Loan {

  private Borrower borrower;
  private Book book;
  private Librarian issuer;
  private Date issuedDate;
  private Date dateReturned;
  private Librarian receiver;

  /**
   * Constructs a Loan object with the given parameters.
   *
   * @param borrower    The borrower of the book.
   * @param book        The book being borrowed.
   * @param iLibrarrian The librarian who issued the book.
   * @param rLibrarrian The librarian who received the returned book.
   * @param iDate       The date the book was issued.
   * @param rDate       The date the book was returned.
   */
  public Loan(Borrower borrower, Book book, Librarian iLibrarrian, Librarian rLibrarrian,
      Date iDate, Date rDate) {
    this.borrower = borrower;
    this.book = book;
    this.issuer = iLibrarrian;
    this.receiver = rLibrarrian;
    this.issuedDate = iDate;
    this.dateReturned = rDate;
  }

  /**
   * Gets the book associated with this loan.
   *
   * @return The book.
   */
  public Book getBook() {
    return book;
  }

  /**
   * Gets the librarian who issued the book.
   *
   * @return The librarian who issued the book.
   */
  public Librarian getIssuer() {
    return issuer;
  }

  /**
   * Gets the librarian who received the returned book.
   *
   * @return The librarian who received the returned book.
   */
  public Librarian getReceiver() {
    return receiver;
  }

  /**
   * Sets the librarian who received the returned book.
   *
   * @param librarian The librarian who received the returned book.
   */
  public void setReceiver(Librarian librarian) {
    receiver = librarian;
  }

  /**
   * Gets the date the book was issued.
   *
   * @return The issued date.
   */
  public Date getIssuedDate() {
    return issuedDate;
  }

  /**
   * Gets the date the book was returned.
   *
   * @return The return date.
   */
  public Date getReturnDate() {
    return dateReturned;
  }

  /**
   * Gets the borrower of the book.
   *
   * @return The borrower.
   */
  public Borrower getBorrower() {
    return borrower;
  }

  /**
   * Sets the date the book was returned.
   *
   * @param dReturned The return date.
   */
  public void setReturnedDate(Date dReturned) {
    dateReturned = dReturned;
  }
}
