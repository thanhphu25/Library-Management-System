package LMS;

import Interface.Observer;

import java.util.Objects;

/**
 * The Librarian class represents a librarian in the library management system. It extends the
 * Person class and includes additional attributes such as salary.
 *
 * <p>This class provides methods to get the salary of the librarian,
 * as well as a method to print the librarian's information.</p>
 *
 * @see Person
 */
public class Librarian extends Person implements Observer {

  protected double salary;

  /**
   * Constructs a new Librarian object with the specified details.
   *
   * @param idNumber the unique identifier for the librarian
   * @param name     the name of the librarian
   * @param password the password for the librarian's account
   * @param address  the address of the librarian
   * @param phoneNum the phone number of the librarian
   * @param email    the email address of the librarian
   * @param salary   the salary of the librarian
   */
  public Librarian(int idNumber, String name, String password, String address, int phoneNum,
      String email, double salary) {
    super(idNumber, name, password, address, phoneNum, email);
    this.salary = salary;
  }

  /**
   * Overrides the printInfo method to include additional information specific to the Librarian.
   * This method prints the salary of the librarian. It first calls the superclass's printInfo
   * method to print the common information.
   */
  @Override
  public void printInfo() {
    super.printInfo();
    System.out.println("Salary: " + salary + "\n");
  }

  /**
   * Retrieves the salary of the librarian.
   *
   * @return the salary of the librarian.
   */
  public double getSalary() {
    return salary;
  }

  /**
   * Checks if this librarian is equal to another object.
   *
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Librarian && o != null) {
      Librarian librarian = (Librarian) o;
      return Objects.equals(getEmail(), librarian.getEmail());
    }

    return false;
  }

  /**
   * Returns the hash code value for this librarian.
   *
   * @return the hash code value
   */
  @Override
  public int hashCode() {
    return Objects.hash(getEmail());
  }

  /**
   * Updates this Librarian with the specified message. This method adds the message to the
   * notifications list for this Librarian.
   *
   * @param message the message to be added to the notifications
   */
  @Override
  public void update(String message) {
    // Add the message to the top of the notifications list
    notifications.add(0, message);
  }
}
