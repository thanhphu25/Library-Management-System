package LMS;

import java.util.ArrayList;

/**
 * Represents a Person in the library management system (LMS). This is an abstract class that
 * contains shared attributes for all persons related to the library such as id, name, password,
 * address, email, and phone number.
 */
public abstract class Person {

  static int currentIdNumber = 0; // Static counter to assign unique IDs automatically
  protected ArrayList<String> notifications;
  protected int id;           // ID of every person related to library
  protected String name;      // Name of the person
  protected String password;  // Password, defaulted to person's ID
  protected String email;     // Email address of the person
  protected String address;   // Address of the person
  protected int phoneNo;      // Phone number of the person

  /**
   * Constructor to initialize a Person object.
   *
   * @param idNumber ID for the person, if -1 is passed, an ID will be auto-generated
   * @param address  Address of the person
   * @param name     Name of the person
   * @param password Password of the person
   * @param email    Email address of the person
   * @param phoneNum Phone number of the person
   */
  public Person(int idNumber, String name, String password, String address, int phoneNum,
      String email) {
    currentIdNumber++;

    if (idNumber == -1) {
      id = currentIdNumber;
    } else {
      id = idNumber;
    }

    this.password = password;
    this.name = name;
    this.address = address;
    phoneNo = phoneNum;
    this.email = email;
    notifications = new ArrayList<>();
  }

  /**
   * Sets the current ID counter to a specific value. This is useful for resetting or setting the ID
   * generation starting point.
   *
   * @param num The new starting value for the ID counter
   */
  public static void setIDCount(int num) {
    currentIdNumber = num;
  }

  /**
   * Prints the details of the person.
   */
  public void printInfo() {
    System.out.println("-------------------------------");
    System.out.println("\nThe details of the person are: \n");
    System.out.println("ID: " + id);
    System.out.println("Name: " + name);
    System.out.println("Address: " + address);
    System.out.println("Phone No: " + phoneNo);
    System.out.println("Email: " + email);
  }

  /**
   * Gets the ID of the person.
   *
   * @return The ID of the person
   */
  public int getID() {
    return id;
  }

  /**
   * Sets the ID of the person.
   *
   * @param id ID to be set
   */
  public void setID(int id) {
    this.id = id;
  }

  /**
   * Gets the name of the person.
   *
   * @return The name of the person
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the person.
   *
   * @param name Name to be set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the address of the person.
   *
   * @return The address of the person
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of the person.
   *
   * @param address Address to be set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets the email of the person.
   *
   * @return The email of the person
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the person.
   *
   * @param email Email to be set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the phone number of the person.
   *
   * @return The phone number of the person
   */
  public int getPhoneNo() {
    return phoneNo;
  }

  /**
   * Sets the phone number of the person.
   *
   * @param phoneNo Phone number to be set
   */
  public void setPhoneNo(int phoneNo) {
    this.phoneNo = phoneNo;
  }

  /**
   * This method returns the value of the 'password' field. Note: It's not recommended to use this
   * method to directly expose passwords as it may lead to sensitive information leakage. Consider
   * implementing security measures such as password encryption or limiting its use to internal
   * systems.
   *
   * @return password (the password of the object)
   */
  public String getPassword() {
    return password;
  }

  /**
   * Prints all the notifications for the person. Iterates through the list of notifications and
   * prints each one to the console.
   */
  public void printNotifications() {
    if (notifications.isEmpty()) {
      System.out.println("\nThere is no notification.");
    } else {
      System.out.println("\nNotifications: ");
      for (String s : notifications) {
        System.out.println(s);
      }
    }
  }

  /**
   * @return notifications
   */
  public ArrayList<String> getNotifications() {
    return notifications;
  }

  /**
   * @param notifications set notifications for person
   */
  public void setNotifications(ArrayList<String> notifications) {
    this.notifications = notifications;
  }

  /**
   * Adds a notification for the person.
   *
   * @param notification The notification to be added
   */
  public void addNotification(String notification) {
    notifications.add(notification);
  }

  /**
   * Clears all notifications for the person.
   */
  public void clearNotifications() {
    notifications.clear();
  }

  /**
   * Compares this object to the specified object for equality.
   *
   * @param o the object to be compared for equality with this object
   * @return {@code true} if the specified object is equal to this object; {@code false} otherwise
   */
  public abstract boolean equals(Object o);

  /**
   * Returns a hash code value for the object. This method is supported for the benefit of hash
   * tables such as those provided by {@link java.util.HashMap}.
   *
   * @return a hash code value for this object.
   */
  public abstract int hashCode();
}
