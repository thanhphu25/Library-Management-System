package Controller;

import LMS.Borrower;
import LMS.Librarian;
import LMS.Library;
import LMS.Person;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static LMS.HandleAlertOperations.showAlert;

/**
 * The FXMLDocument class serves as the controller for the Library Management System's JavaFX
 * application. It handles various user interactions within the application, such as logging in,
 * creating accounts. The class is responsible for managing the visibility of different forms and
 * validating user inputs.
 *
 * <p>
 * The class contains several methods annotated with {@code @FXML} to handle specific action events
 * triggered by user interactions with the UI components defined in the FXML file. These methods
 * include:
 * </p>
 *
 * <ul>
 *   <li>{@link #handleBack(Event)}: Handles the back button action event to switch between forms.</li>
 *   <li>{@link #handleCreateAccount(Event)}: Handles the action event for creating a new account.</li>
 *   <li>{@link #handleForgotPassword(Event)}: Handles the action event for the "Forgot Password" option.</li>
 *   <li>{@link #handleLogin(Event)}: Handles the login action when the login button is pressed.</li>
 *   <li>{@link #handleSignUp(Event)}: Handles the sign-up action triggered by the user.</li>
 *   <li>{@link #togglePasswordVisibility()}: Toggles the visibility of the password field in the login form.</li>
 *   <li>{@link #initialize()}: Initializes the controller class after the FXML file has been loaded.</li>
 * </ul>
 *
 * <p>
 * The class also contains private helper methods for validating login credentials, recovering passwords,
 * registering new accounts, and displaying alert messages.
 * </p>
 *
 * <p>
 * The following FXML components are injected into the class:
 * </p>
 *
 * <ul>
 *   <li>{@code ResourceBundle resources}</li>
 *   <li>{@code URL location}</li>
 *   <li>{@code Button changePass_backBtn}</li>
 *   <li>{@code PasswordField changePass_cPassword}</li>
 *   <li>{@code AnchorPane changePass_form}</li>
 *   <li>{@code PasswordField changePass_password}</li>
 *   <li>{@code Button changePass_proceedBtn}</li>
 *   <li>{@code Button login_btn}</li>
 *   <li>{@code Button login_createAccount}</li>
 *   <li>{@code Hyperlink login_forgotPassword}</li>
 *   <li>{@code AnchorPane login_form}</li>
 *   <li>{@code PasswordField login_password}</li>
 *   <li>{@code CheckBox login_selectShowPassword}</li>
 *   <li>{@code TextField login_showPassword}</li>
 *   <li>{@code TextField login_username}</li>
 *   <li>{@code AnchorPane main_form}</li>
 *   <li>{@code TextField signup_answer}</li>
 *   <li>{@code Button signup_btn}</li>
 *   <li>{@code TextField signup_email}</li>
 *   <li>{@code AnchorPane signup_form}</li>
 *   <li>{@code Button signup_loginAccount}</li>
 *   <li>{@code PasswordField signup_password}</li>
 *   <li>{@code ComboBox<String> signup_selectQuestion}</li>
 *   <li>{@code TextField signup_username}</li>
 * </ul>
 *
 * <p>
 * The {@link #initialize()} method ensures that all the necessary FXML components are properly injected
 * and initializes the selection options for the security questions in the forgot password and signup forms.
 * </p>
 */
public class LoginController {

  Library library = Library.getInstance();
  @FXML
  private ResourceBundle resources;
  @FXML
  private URL location;
  @FXML
  private Button login_btn;
  @FXML
  private Button login_createAccount;
  @FXML
  private AnchorPane login_form;
  @FXML
  private PasswordField login_password;
  @FXML
  private CheckBox login_selectShowPassword;
  @FXML
  private TextField login_showPassword;
  @FXML
  private TextField login_username;
  @FXML
  private AnchorPane main_form;
  @FXML
  private Button signup_btn;
  @FXML
  private TextField signup_email;
  @FXML
  private AnchorPane signup_form;
  @FXML
  private Button signup_loginAccount;
  @FXML
  private PasswordField signup_password;
  @FXML
  private TextField signup_username;
  @FXML
  private TextField signup_address;
  @FXML
  private TextField signup_phone;
  @FXML
  private TextField signup_systemPassword;
  @FXML
  private TextField signup_salary;
  @FXML
  private CheckBox signup_librarian;
  @FXML
  private Hyperlink hyperlink_loginHere;
  @FXML
  private Hyperlink hyperlink_createAccount;

  /**
   * Handles the back button action event. This method is triggered when the back button is pressed.
   * It checks which form is currently visible (signup, forgot password, or change password) and
   * switches the visibility to the login form.
   *
   * @param event the action event triggered by the back button
   */
  @FXML
  void handleBack(Event event) {
    if (signup_form.isVisible()) {
      signup_form.setVisible(false);
      login_form.setVisible(true);
    }
  }

  /**
   * Handles the action event for creating a new account. This method is triggered when the user
   * initiates the account creation process. It hides the login form and displays the signup form.
   *
   * @param event the action event triggered by the user interaction
   */
  @FXML
  void handleSignUp(Event event) {
    login_form.setVisible(false);
    signup_form.setVisible(true);
    signup_systemPassword.setVisible(false);
    signup_salary.setVisible(false);
  }

  /**
   * Handles the action event triggered when the "Forgot Password" option is selected. This method
   * hides the login form and displays the forgot password form.
   *
   * @param event the action event triggered by the user interaction
   */
  @FXML
  void handleForgotPassword(Event event) {
    showAlert("Unavailable function!",
        "Please contact administrator for further help\nEmail: 23020086@vnu.edu.vn");
  }

  /**
   * Handles the login action when the login button is pressed.
   *
   * @param event The Event triggered by the login button.
   *              <p>
   *              This method retrieves the username and password from the login form, validates the
   *              credentials, and displays an appropriate alert message. If the login is
   *              successful, it makes the main form visible and hides the login form.
   */
  @FXML
  void handleLogin(Event event) throws IOException {
    String username = login_username.getText();
    String password = login_password.getText();

    Person user = library.logicalLogin(username, password);
    if (user != null) {
      showAlert("Success", "Login successful for user: " + username);
      login_form.setVisible(false);
      main_form.setVisible(true);
      if (user instanceof Borrower) {
        library.setUser(user);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tải file FXML của dashboard
        FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/LMS/User.fxml"));

        Scene userScene = new Scene(adminLoader.load(), 1096, 640);

        primaryStage.setTitle("Library Administration");

        // Chuyển sang Scene của dashboard
        primaryStage.setScene(userScene);
        primaryStage.setResizable(false);

      } else {
        library.setUser(user);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Tải file FXML của dashboard
        FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/LMS/Admin.fxml"));

        Scene adminScene = new Scene(adminLoader.load(), 1096, 640);

        primaryStage.setTitle("Library");

        // Chuyển sang Scene của dashboard
        primaryStage.setScene(adminScene);
        primaryStage.setResizable(false);
      }
    } else {
      showAlert("Error", "Invalid username or password.");
    }
  }

  /**
   * Handles the sign-up action triggered by the user. This method retrieves the input values from
   * the sign-up form fields, including email, username, password, security question, and answer. It
   * then attempts to register a new account with these details. If the registration is successful,
   * a success alert is shown, the sign-up form is hidden, and the login form is made visible. If
   * the registration fails, an error alert is displayed.
   *
   * @param event The Event triggered by the sign-up button.
   */
  @FXML
  void handleCreateAccount(Event event) {
    String username = signup_username.getText();
    String password = signup_password.getText();
    String email = signup_email.getText();
    String address = signup_address.getText();
    String phoneText = signup_phone.getText();

    // Validate that essential fields are not null or empty
    if (username == null || username.trim().isEmpty() ||
        password == null || password.trim().isEmpty() ||
        email == null || email.trim().isEmpty() ||
        phoneText == null || phoneText.trim().isEmpty()) {
      showAlert("Error",
          "Please fill in all required fields: username, password, email, and phone.");
      return;
    }

    // Validate phone number is an integer
    int phone;
    try {
      phone = Integer.parseInt(phoneText);
    } catch (NumberFormatException e) {
      showAlert("Error", "Invalid phone number. Please enter a numeric value.");
      return;
    }

    boolean isLibrarian = signup_librarian.isSelected();
    if (isLibrarian) {
      String systemPassword = signup_systemPassword.getText();
      String salaryText = signup_salary.getText();
      double salary;

      // Validate salary is a double
      try {
        salary = Double.parseDouble(salaryText);
      } catch (NumberFormatException e) {
        showAlert("Error", "Invalid salary. Please enter a numeric value.");
        return;
      }

      Librarian librarian = new Librarian(-1, username, password, address, phone, email, salary);
      if (systemPassword.equals(library.LMS_PASSWORD)) {

        if (library.addLibrarian(librarian)) {
          showAlert("Success", "Librarian account created successfully.");
          signup_form.setVisible(false);
          login_form.setVisible(true);
        } else {
          showAlert("Error", "Failed to create librarian account. Record already exists.");
        }
      } else {
        showAlert("Error", "System password is incorrect.");
      }
    } else {
      Borrower borrower = new Borrower(-1, username, password, address, phone, email);
      if (library.addBorrower(borrower)) {
        showAlert("Success", "Borrower account created successfully.");
        signup_form.setVisible(false);
        login_form.setVisible(true);
      } else {
        showAlert("Error", "Failed to create borrower account. Record already exists.");
      }
    }
  }

  /**
   * Toggles the visibility of the password field in the login form. When the "Show Password"
   * checkbox is selected, the plain text password is displayed. When the checkbox is not selected,
   * the password is hidden and displayed as masked text.
   * <p>
   * This method switches the visibility between two text fields:
   * - `login_password`: The password field that masks the input.
   * - `login_showPassword`: The text field that shows the plain text password.
   * <p>
   * The method checks the state of the `login_selectShowPassword` checkbox to determine which field
   * should be visible and updates the text accordingly.
   */
  @FXML
  private void togglePasswordVisibility() {
    if (login_selectShowPassword.isSelected()) {
      login_showPassword.setText(login_password.getText());
      login_showPassword.setVisible(true);
      login_password.setVisible(false);
    } else {
      login_password.setText(login_showPassword.getText());
      login_password.setVisible(true);
      login_showPassword.setVisible(false);
    }
  }


  /**
   * Initializes the controller class. This method is automatically called after the FXML file has
   * been loaded. It ensures that all the necessary FXML components are properly injected and
   * initializes the selection
   * <p>
   * Assertions:
   * - Ensures that all the FXML components are not null and have been properly injected.
   * <p>
   * Initialization:
   */
  @FXML
  void initialize() {
    assert login_btn
        != null : "fx:id=\"login_btn\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_createAccount
        != null : "fx:id=\"login_createAccount\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_form
        != null : "fx:id=\"login_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_password
        != null : "fx:id=\"login_password\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_selectShowPassword
        != null : "fx:id=\"login_selectShowPassword\" was not injected: check your FXML file 'Login.fxml'.";
    assert login_username
        != null : "fx:id=\"login_username\" was not injected: check your FXML file 'Login.fxml'.";
    assert main_form
        != null : "fx:id=\"main_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_btn
        != null : "fx:id=\"signup_btn\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_email
        != null : "fx:id=\"signup_email\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_form
        != null : "fx:id=\"signup_form\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_loginAccount
        != null : "fx:id=\"signup_loginAccount\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_password
        != null : "fx:id=\"signup_password\" was not injected: check your FXML file 'Login.fxml'.";
    assert signup_username
        != null : "fx:id=\"signup_username\" was not injected: check your FXML file 'Login.fxml'.";
  }

  @FXML
  public void handleLibrarianSignup(ActionEvent actionEvent) {
    if (signup_librarian.isSelected()) {
      signup_systemPassword.setVisible(true);
      signup_salary.setVisible(true);
    } else {
      signup_systemPassword.setVisible(false);
      signup_salary.setVisible(false);
    }
  }


}
