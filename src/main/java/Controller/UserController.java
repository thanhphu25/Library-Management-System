package Controller;

import LMS.*;
import com.google.zxing.WriterException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import static LMS.HandleAlertOperations.showAlert;
import static LMS.HandleAlertOperations.showConfirmation;

public class UserController implements Initializable {

  private static final Library library = Library.getInstance();

  private final ArrayList<Book> books = library.getBooks();
  public Button btnNotifications;

  private ObservableList<Loan> loanBookList;
  private ObservableList<Book> bookList;
  private ObservableList<Loan> loanList;

  @FXML
  private TableView<Loan> tableBookShelf;

  @FXML
  private BorderPane paneHome;
  @FXML
  private AnchorPane paneInformation;
  @FXML
  private TableView<Book> tableBooks;
  @FXML
  private TableColumn<Book, String> bookAuthorColumn;
  @FXML
  private TableColumn<Book, Integer> bookIdColumn;
  @FXML
  private TableColumn<Book, Boolean> bookIsIssuedColumn;
  @FXML
  private TextField bookSearchTextField;
  @FXML
  private TableColumn<Book, String> bookTitleColumn;
  @FXML
  private AnchorPane paneBooks;
  @FXML
  private AnchorPane paneHistory;
  @FXML
  private AnchorPane paneYourShelf;
  @FXML
  private ImageView qrImage;
  @FXML
  private Label textSubTiltle;
  @FXML
  private Label textLoanDescription;
  @FXML
  private StackPane box;
  @FXML
  private StackPane boxLoan;
  @FXML
  private TextField infoName;
  @FXML
  private TextField infoEmail;
  @FXML
  private TextField infoAddress;
  @FXML
  private TextField infoPhone;
  @FXML
  private Label labelWelcome;
  @FXML
  private TableColumn<Loan, String> bookShelfAuthorColumn;

  @FXML
  private TableColumn<Loan, Integer> bookShelfIdColumn;

  @FXML
  private TextField bookShelfSearchTextField;

  @FXML
  private TableColumn<Loan, String> bookShelfTitleColumn;
  @FXML
  private AnchorPane paneNotifications;
  @FXML
  private TableView<String> tableNotifications;
  @FXML
  private TableColumn<String, Integer> notificationsNoColumn;
  @FXML
  private TableColumn<String, String> notificationsMessageColumn;
  @FXML
  private Button btnClearNotifications;
  @FXML
  private TableView<Loan> historyTableView;
  @FXML
  private TableColumn<Loan, Integer> noColumn;
  @FXML
  private TableColumn<Loan, Integer> bookIDColumn;
  @FXML
  private TableColumn<Loan, String> titleColumn;
  @FXML
  private TableColumn<Loan, String> issuerIDColumn;
  @FXML
  private TableColumn<Loan, String> issuerColumn;
  @FXML
  private TableColumn<Loan, String> issuedDateColumn;
  @FXML
  private TableColumn<Loan, String> receiverIDColumn;
  @FXML
  private TableColumn<Loan, String> receiverColumn;
  @FXML
  private TableColumn<Loan, String> returnedDateColumn;
  @FXML
  private TextField historySearchTextField;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeTableBooks();
    initializeTableLoanBooks();
    initializeInformation();
    initializeTableNotifications();
    initializeTableHistory();
  }

  private void showBookDetails(Book selectedBook) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
      HBox cardBox = fxmlLoader.load();
      CardController cardController = fxmlLoader.getController();
      cardController.setData(selectedBook);

      // Cập nhật thông tin sách
      textSubTiltle.setText(selectedBook.getDescription());

      // Thay toàn bộ nội dung của StackPane
      box.getChildren().setAll(cardBox);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showFullBookDetails(Book selectedBook) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
      HBox cardBox = fxmlLoader.load();
      CardController cardController = fxmlLoader.getController();
      cardController.setData(selectedBook);

      // Cập nhật thông tin sách
      qrImage.setImage(selectedBook.generateQRCodeImage(selectedBook.getPreviewLink(), 150, 150));
      textLoanDescription.setText(selectedBook.getDescription());

      // Thay toàn bộ nội dung của StackPane
      boxLoan.getChildren().setAll(cardBox);
    } catch (IOException | WriterException e) {
      e.printStackTrace();
    }
  }

  private void initializeTableBooks() {
    // Thiết lập các cột với thuộc tính của lớp Book
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    bookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));

    // Chuyển đổi ArrayList<Book> sang ObservableList<Book>
    bookList = FXCollections.observableArrayList(books);

    // Tạo FilteredList để hỗ trợ tìm kiếm
    FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);

    // Thiết lập dữ liệu cho bảng
    tableBooks.setItems(filteredData);

    // Lắng nghe sự thay đổi của thanh tìm kiếm
    bookSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(book -> {
        // Nếu thanh tìm kiếm trống, hiển thị tất cả sách
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // So khớp từ khóa (không phân biệt chữ hoa/thường)
        String lowerCaseFilter = newValue.toLowerCase();

        if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với tiêu đề sách
        } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với tác giả
        } else {
          return String.valueOf(book.getID()).contains(lowerCaseFilter); // Khớp với ID
        }
// Không khớp
      });
    });

    tableBooks.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            showBookDetails(newValue);
          }
        });
  }

  private void initializeTableLoanBooks() {
    Borrower borrower = (Borrower) library.getUser();

    // Thiết lập các cột với thuộc tính của lớp Loan
    bookShelfIdColumn.setCellValueFactory(
        cellData -> new SimpleIntegerProperty(cellData.getValue().getBook().getID()).asObject());
    bookShelfTitleColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
    bookShelfAuthorColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));

    loanBookList = FXCollections.observableArrayList(borrower.getBorrowedBooks());

    // Tạo FilteredList để hỗ trợ tìm kiếm
    FilteredList<Loan> filteredData = new FilteredList<>(loanBookList, b -> true);

    // Lắng nghe sự thay đổi từ trường tìm kiếm (searchTextField)
    bookShelfSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(loan -> {
        // Nếu thanh tìm kiếm trống, hiển thị toàn bộ danh sách
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // Chuyển từ khóa sang chữ thường để so khớp không phân biệt hoa/thường
        String lowerCaseFilter = newValue.toLowerCase();

        // So khớp từ khóa với các thuộc tính của Loan
        return loan.getBook().getTitle().toLowerCase().contains(lowerCaseFilter)
            || loan.getBook().getAuthor().toLowerCase().contains(lowerCaseFilter)
            || String.valueOf(loan.getBook().getID()).contains(lowerCaseFilter);
      });
    });

    // Thiết lập dữ liệu cho bảng
    tableBookShelf.setItems(filteredData);

    // Lắng nghe sự thay đổi khi chọn một mục trong bảng
    tableBookShelf.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            showFullBookDetails(newValue.getBook());
          }
        });

    // Chọn dòng đầu tiên mặc định khi mở ứng dụng
    if (!tableBookShelf.getItems().isEmpty()) {
      tableBookShelf.getSelectionModel().selectFirst();
    }

  }

  private void initializeTableNotifications() {
    ArrayList<String> notifications = library.getUser().getNotifications();
    ObservableList<String> notificationList = FXCollections.observableArrayList(notifications);
    tableNotifications.setItems(notificationList);

    // Set up the columns
    notificationsNoColumn.setCellValueFactory(cellData -> {
      int index = tableNotifications.getItems().indexOf(cellData.getValue()) + 1;
      return Bindings.createIntegerBinding(() -> index).asObject();
    });
    notificationsNoColumn.setPrefWidth(30);

    notificationsMessageColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue()));

    // Use a custom cell factory to ensure the row numbers are updated correctly
    notificationsNoColumn.setCellFactory(column -> new TableCell<>() {
      @Override
      protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setText(null);
        } else {
          setText(String.valueOf(getIndex() + 1));
        }
      }
    });
  }

  private void initializeTableHistory() {
    // Khởi tạo danh sách động từ danh sách các khoản mượn
    loanList = FXCollections.observableArrayList(library.getLoans());

    FilteredList<Loan> filteredData = new FilteredList<>(loanList, b -> true);

    // Gắn danh sách này vào bảng
    historyTableView.setItems(filteredData);

    // Cấu hình các cột
    noColumn.setCellValueFactory(cellData -> {
      int index = historyTableView.getItems().indexOf(cellData.getValue()) + 1;
      return Bindings.createIntegerBinding(() -> index).asObject();
    });

    bookIDColumn.setCellValueFactory(
        cellData -> new SimpleIntegerProperty(cellData.getValue().getBook().getID()).asObject());

    titleColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));

    issuerIDColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(
            String.valueOf(cellData.getValue().getIssuer().getID())));
    issuerColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getIssuer().getName()));
    issuedDateColumn.setCellValueFactory(cellData -> {
      LocalDate issuedDate = cellData.getValue().getIssuedDate().toInstant()
          .atZone(ZoneId.systemDefault()).toLocalDate();

      // Format the LocalDate to display only the day
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      return new SimpleStringProperty(issuedDate.format(formatter));
    });

    receiverIDColumn.setCellValueFactory(cellData -> {
      // Get the receiver object, and check if it's null
      Librarian receiver = cellData.getValue().getReceiver();
      if (receiver != null) {
        // Return the receiver's ID as a string
        return new SimpleStringProperty(String.valueOf(receiver.getID()));
      } else {
        // Return an empty string if receiver is null
        return new SimpleStringProperty("");
      }
    });

    receiverColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getReceiver() != null ? cellData.getValue().getReceiver().getName()
            : ""));
    returnedDateColumn.setCellValueFactory(cellData -> {
      // Get the return date, and convert it to LocalDate if it's not null
      Date returnedDate = cellData.getValue().getReturnDate();
      if (returnedDate != null) {
        // Convert java.util.Date to java.time.LocalDate
        LocalDate localDate = returnedDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        // Format the LocalDate to display only the day
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new SimpleStringProperty(localDate.format(formatter));
      } else {
        // If the return date is null, return an empty string
        return new SimpleStringProperty("");
      }
    });

    // Add a listener to the search text field to filter the table
    historySearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(loan -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String issuedDate = loan.getIssuedDate().toInstant().atZone(ZoneId.systemDefault())
            .toLocalDate().format(formatter);
        String returnDate = loan.getReturnDate() != null ? loan.getReturnDate().toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate().format(formatter) : "";

        if (String.valueOf(loan.getBook().getID()).contains(lowerCaseFilter)) {
          return true;
        } else if (loan.getBook().getTitle().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (String.valueOf(loan.getBorrower().getID()).contains(lowerCaseFilter)) {
          return true;
        } else if (loan.getBorrower().getName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (String.valueOf(loan.getIssuer().getID()).contains(lowerCaseFilter)) {
          return true;
        } else if (loan.getIssuer().getName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (issuedDate.contains(lowerCaseFilter)) {
          return true;
        } else if (loan.getReceiver() != null && String.valueOf(loan.getReceiver().getID())
            .contains(lowerCaseFilter)) {
          return true;
        } else if (loan.getReceiver() != null && loan.getReceiver().getName().toLowerCase()
            .contains(lowerCaseFilter)) {
          return true;
        } else if (returnDate.contains(lowerCaseFilter)) {
          return true;
        }
        return false;
      });
    });

    historyTableView.setEditable(true);
  }

  private void initializeInformation() {
    // Lấy thông tin người dùng
    Borrower borrower = (Borrower) library.getUser();

        // Hiển thị thông tin hiện tại
        infoName.setText(borrower.getName());
        infoEmail.setText(borrower.getEmail());
        infoAddress.setText(borrower.getAddress());
        infoPhone.setText(String.valueOf(borrower.getPhoneNo()));
    }

  private void addChangeListener(TextField textField, Borrower borrower, String field) {
    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) { // Khi mất focus (blur)
        try {
          switch (field) {
            case "name":
              borrower.setName(textField.getText());
              break;
            case "email":
              borrower.setEmail(textField.getText());
              break;
            case "address":
              borrower.setAddress(textField.getText());
              break;
            case "phoneNo":
              borrower.setPhoneNo(Integer.parseInt(textField.getText()));
              break;
          }
        } catch (NumberFormatException e) {
          labelWelcome.setText("Invalid phone number format!");
        } catch (Exception e) {
          e.printStackTrace();
          labelWelcome.setText("Error updating information!");
        }
      }
    });
    // Chọn dòng đầu tiên mặc định khi mở ứng dụng
    if (!tableBooks.getItems().isEmpty()) {
      tableBooks.getSelectionModel().selectFirst();
    }

  }

  @FXML
  public void handleHoldBookAction(ActionEvent event) {
    Borrower borrower = (Borrower) library.getUser();
    Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
    if (selectedBook.getHoldRequests().isEmpty()) {
      if (showConfirmation("Borrow Book", "Do you want to want to borrow this Book?"
          + "\nYou will have to wait for acceptance from our Librarians.")) {
        String response = selectedBook.makeHoldRequest(borrower);
        // Notify librarians about borrow action
        selectedBook.notifyObservers(
            "Please service the hold request for " + selectedBook.getTitle()
                + " (ID: " + selectedBook.getID() + ") by " + borrower.getName() + " (ID: "
                + borrower.getID() + ").");
      }
    } else {
      if (showConfirmation("Place Book on Hold", "There are earlier requests." +
          "\nDo you want to place it on hold?")) {
        showAlert("Place Book on Hold operation", selectedBook.makeHoldRequest(borrower));
      }
    }
  }

  private void showPane(AnchorPane paneToShow) {
    paneBooks.setVisible(false);
    paneYourShelf.setVisible(false);
    paneHistory.setVisible(false);
    paneNotifications.setVisible(false);
    paneToShow.setVisible(true);
  }

  @FXML
  void handleBooks(ActionEvent event) {
    showPane(paneBooks);
  }

  @FXML
  void handleShowInformation(ActionEvent event) {
    paneHome.setVisible(false);
    paneInformation.setVisible(true);
  }

    @FXML
    void handleBack(ActionEvent event) {
        handleChangeInfo();
        paneInformation.setVisible(false);
        paneHome.setVisible(true);
    }

  @FXML
  void handleHistory(ActionEvent event) {
    showPane(paneHistory);
  }

  @FXML
  void handleYourShelf(ActionEvent event) {
    showPane(paneYourShelf);
  }

  @FXML
  void handleNotifications(ActionEvent event) {
    showPane(paneNotifications);
  }

  @FXML
  private void handleClearNotifications(ActionEvent event) {
    if (showConfirmation("Clear Notifications",
        "Are you sure you want to clear all notifications?")) {
      library.getUser().clearNotifications();
      tableNotifications.getItems().clear();
    }
  }

  @FXML
  void handleLogOut(ActionEvent event) throws IOException {
    if (showConfirmation("Log Out", "Are you sure you want to log out?")) {
      Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      // Tải file FXML của dashboard
      FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LMS/Login.fxml"));

      Scene loginScene = new Scene(loginLoader.load(), 1096, 640);

      primaryStage.setTitle("Login");

            // Chuyển sang Scene của dashboard
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
        }
    }

    @FXML
    private void handleChangeInfo() {
        Borrower borrower = (Borrower) library.getUser();
        String name = borrower.getName();
        String email = borrower.getEmail();
        String address = borrower.getAddress();
        String phone = String.valueOf(borrower.getPhoneNo());

        String newName = infoName.getText();
        String newEmail = infoEmail.getText();
        String newAddress = infoAddress.getText();
        String newPhone = infoPhone.getText();

        if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "These info can not be Empty.");
        } else {
            if (!newName.equals(name) || !newEmail.equals(email) || !newAddress.equals(address) || !newPhone.equals(phone)) {
                if (showConfirmation("Change Information", "Are you sure you want to change your information?")) {
                    borrower.setName(newName);
                    borrower.setEmail(newEmail);
                    borrower.setAddress(newAddress);
                    borrower.setPhoneNo(Integer.parseInt(newPhone));
                    showAlert("Change Information", "Your information has been changed successfully.");
                } else {
                    initializeInformation();
                }
            }
        }
    }
}
