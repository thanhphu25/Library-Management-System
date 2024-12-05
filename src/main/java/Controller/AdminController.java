package Controller;

import LMS.*;
import com.google.zxing.WriterException;
import javafx.application.Platform;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static LMS.HandleAlertOperations.showAlert;
import static LMS.HandleAlertOperations.showConfirmation;

public class AdminController implements Initializable {

  private static final Library library = Library.getInstance();
  private final ArrayList<Borrower> users = library.getBorrowers();
  private final ArrayList<Librarian> librarians = library.getLibrarians();
  private final API apiTest = new API();
  private final ObservableList<Book> apiBooksList = FXCollections.observableArrayList();
  private final AtomicReference<String> searchQuery = new AtomicReference<>("");
  private final long debounceDelay = 500;
  private final ScheduledExecutorService debounceScheduler = Executors.newSingleThreadScheduledExecutor();
  private final ExecutorService apiExecutor = Executors.newCachedThreadPool();
  private final ArrayList<Book> books = library.getBooks();
  public VBox barChart;
  public BarChart<String, Number> borrowedBookChart;
  private ScheduledFuture<?> scheduledFuture;
  private volatile long lastApiCallTime = 0;
  private ObservableList<Book> bookList;
  private ObservableList<Loan> loanList;


  @FXML
  private TableView<Book> tableAddBooks;
  @FXML
  private AnchorPane paneAddBook;
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
  private TableColumn<Book, Void> bookOptionsColumn;
  @FXML
  private AnchorPane paneBooks;
  @FXML
  private AnchorPane paneUsers;
  @FXML
  private AnchorPane paneDashboard;
  @FXML
  private AnchorPane paneHistory;
  @FXML
  private TableView<Book> tableBooks;
  @FXML
  private TableView<Person> tableUsers;
  @FXML
  private Label textSubTiltle;
  @FXML
  private TableColumn<Borrower, String> userAddressColumn;
  @FXML
  private TableColumn<Borrower, String> userEmailColumn;
  @FXML
  private TableColumn<Borrower, Integer> userIdColumn;
  @FXML
  private TableColumn<Borrower, String> userNameColumn;
  @FXML
  private TableColumn<Borrower, Integer> userPhoneColumn;
  @FXML
  private TextField userSearchTextField;
  @FXML
  private TextField bookApiSearchTextField;
  @FXML
  private Label labelTotalBooks;
  @FXML
  private Label labelTotalUsers;
  @FXML
  private ImageView qrImage;
  @FXML
  private TableColumn<Book, String> addBookAuthorColumn;
  @FXML
  private TableColumn<Book, Void> addBookBtnColumn;
  @FXML
  private TableColumn<Book, String> addBookTitleColumn;
  @FXML
  private StackPane box;
  @FXML
  private BorderPane paneHome;
  @FXML
  private AnchorPane paneInformation;
  @FXML
  private TextField infoName;
  @FXML
  private TextField infoEmail;
  @FXML
  private TextField infoAddress;
  @FXML
  private TextField infoPhone;
  @FXML
  private TableView<Loan> historyTableView;
  @FXML
  private TableColumn<Loan, Integer> noColumn;
  @FXML
  private TableColumn<Loan, Integer> bookIDColumn;
  @FXML
  private TableColumn<Loan, String> titleColumn;
  @FXML
  private TableColumn<Loan, Integer> borrowerIDColumn;
  @FXML
  private TableColumn<Loan, String> borrowerColumn;
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
  @FXML
  private Label labelWelcome;
  @FXML
  private AnchorPane paneNotifications;
  @FXML
  private TableView<String> tableNotifications;
  @FXML
  private TableColumn<String, Integer> notificationsNoColumn;
  @FXML
  private TableColumn<String, String> notificationsMessageColumn;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    labelTotalBooks.setText(String.valueOf(books.size()));
    labelTotalUsers.setText(String.valueOf(users.size()));

    initializeTableBooks();
    initializeTableUsers();
    initializeTableHistory();
    initializeInformation();
    initializeTableNotifications();
    initializeBarChart();
  }

  private void showBookDetails(Book selectedBook) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/LMS/Card.fxml"));
      HBox cardBox = fxmlLoader.load();
      CardController cardController = fxmlLoader.getController();
      cardController.setData(selectedBook);

      // Cập nhật thông tin sách
      qrImage.setImage(selectedBook.generateQRCodeImage(selectedBook.getPreviewLink(), 150, 150));
      textSubTiltle.setText(selectedBook.getDescription());

      // Thay toàn bộ nội dung của StackPane
      box.getChildren().setAll(cardBox);
    } catch (IOException | WriterException e) {
      e.printStackTrace();
    }
  }

  private void initializeTableBooks() {
    // Thiết lập các cột với thuộc tính của lớp Book
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
    bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));
    bookIsIssuedColumn.setCellValueFactory(new PropertyValueFactory<>("IssuedStatus"));

    // Đánh dấu bảng có thể chỉnh sửa
    tableBooks.setEditable(true);
    bookTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    bookAuthorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    bookIsIssuedColumn.setCellFactory(
        TextFieldTableCell.forTableColumn(new BooleanStringConverter()));

    bookTitleColumn.setOnEditCommit(event -> {
      Book book = event.getRowValue();
      book.setTitle(event.getNewValue());
      //library.setBooksInLibrary(book.getID(), book);
    });

    bookAuthorColumn.setOnEditCommit(event -> {
      Book book = event.getRowValue();
      book.setAuthor(event.getNewValue());
    });

    bookOptionsColumn.setCellFactory(column -> new TableCell<>() {
      private final MenuButton menuButton = new MenuButton("");

      {
        // Create MenuItems for each action
        MenuItem holdItem = new MenuItem("Place book on Hold");
        holdItem.setOnAction(event -> {
          Book book = getTableView().getItems().get(getIndex());
          handleHoldBookAction(book); // Implement your method for adding a book
        });

        MenuItem checkOutItem = new MenuItem("Check out");
        checkOutItem.setOnAction(event -> {
          Book book = getTableView().getItems().get(getIndex());
          handleCheckOutBookAction(book); // Implement your method for editing a book
        });

        MenuItem checkInItem = new MenuItem("Check in");
        checkInItem.setOnAction(event -> {
          Book book = getTableView().getItems().get(getIndex());
          handleCheckInBookAction(book); // Implement your method for deleting a book
        });

        MenuItem deleteItem = new MenuItem("Remove");
        deleteItem.setOnAction(event -> {
          Book selectedBook = getTableView().getItems().get(getIndex());
          handleDeleteBookAction(selectedBook);
        });

        MenuItem showHoldRequestQueue = new MenuItem("Show hold request queue");
        showHoldRequestQueue.setOnAction(event -> {
          Book book = getTableView().getItems().get(getIndex());
          handleShowHoldRequestQueue(book);
        });

        // Add all menu items to the MenuButton
        menuButton.getItems()
            .addAll(holdItem, checkOutItem, checkInItem, showHoldRequestQueue, deleteItem);
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(menuButton); // Show the MenuButton in the cell
        }
      }
    });

    // Tạo ObservableList từ danh sách sách gốc
    bookList = FXCollections.observableArrayList(books);

    // Tạo FilteredList để hỗ trợ tìm kiếm
    FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);

    // Gán dữ liệu cho bảng
    tableBooks.setItems(filteredData);

    // Lắng nghe sự thay đổi của thanh tìm kiếm
    bookSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(book -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        return book.getTitle().toLowerCase().contains(lowerCaseFilter) || book.getAuthor()
            .toLowerCase().contains(lowerCaseFilter) || String.valueOf(book.getID())
            .contains(lowerCaseFilter);
      });
    });

    // Lắng nghe khi chọn dòng mới và hiển thị chi tiết sách
    tableBooks.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            showBookDetails(newValue);
          }
        });

    // Chọn dòng đầu tiên mặc định khi mở ứng dụng
    if (!tableBooks.getItems().isEmpty()) {
      tableBooks.getSelectionModel().selectFirst();
    }

  }

  private void initializeTableUsers() {
    // Thiết lập các cột với thuộc tính của lớp Person (là lớp cha của Borrower và Librarian)
    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
    userNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNo"));
    userAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

    // Tạo thêm cột is_Librarian
    TableColumn<Person, String> isLibrarianColumn = new TableColumn<>("Is Librarian");
    isLibrarianColumn.setCellValueFactory(person -> {
      if (person.getValue() instanceof Librarian) {
        return new SimpleStringProperty("True");
      } else {
        return new SimpleStringProperty("False");
      }
    });

    // Thêm cột isLibrarian vào bảng
    tableUsers.getColumns().add(isLibrarianColumn);

    // Thiết lập bảng để có thể chỉnh sửa
    tableUsers.setEditable(true);

    // Cho phép chỉnh sửa từng cột
    userIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    userEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    userNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    userPhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    userAddressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    // Xử lý sự kiện khi chỉnh sửa từng cột
    userIdColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setID(event.getNewValue());
    });

    userEmailColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setEmail(event.getNewValue());
    });

    userNameColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setName(event.getNewValue());
    });

    userPhoneColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setPhoneNo(event.getNewValue());
    });

    userAddressColumn.setOnEditCommit(event -> {
      Person person = event.getRowValue();
      person.setAddress(event.getNewValue());
    });

    ArrayList<Person> combinedPersons = new ArrayList<>();
    combinedPersons.addAll(users);
    combinedPersons.addAll(librarians);

    combinedPersons.sort(Comparator.comparingInt(Person::getID));

    ObservableList<Person> personList = FXCollections.observableArrayList(combinedPersons);

    FilteredList<Person> filteredData = new FilteredList<>(personList, p -> true);

    tableUsers.setItems(filteredData);

    userSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(person -> {
        // Nếu thanh tìm kiếm trống, hiển thị tất cả người dùng
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // So khớp từ khóa (không phân biệt chữ hoa/thường)
        String lowerCaseFilter = newValue.toLowerCase();

        if (String.valueOf(person.getID()).contains(lowerCaseFilter)) {
          return true; // Khớp với ID
        } else if (person.getEmail().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với email
        } else if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với tên
        } else if (String.valueOf(person.getPhoneNo()).contains(lowerCaseFilter)) {
          return true; // Khớp với số điện thoại
        } else if (person.getAddress().toLowerCase().contains(lowerCaseFilter)) {
          return true; // Khớp với địa chỉ
        } else if (person instanceof Librarian && "true".contains(lowerCaseFilter)) {
          return true; // Khớp với is_Librarian là True
        } else {
          return !(person instanceof Librarian) && "false".contains(
              lowerCaseFilter); // Khớp với is_Librarian là False
        }
// Không khớp
      });
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
    borrowerIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(
        cellData.getValue().getBorrower().getID()).asObject());
    borrowerColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getBorrower().getName()));
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

  private void initializeInformation() {
    // Lấy thông tin người dùng
    Librarian librarian = (Librarian) library.getUser();

    // Hiển thị thông tin hiện tại
    infoName.setText(librarian.getName());
    infoEmail.setText(librarian.getEmail());
    infoAddress.setText(librarian.getAddress());
    infoPhone.setText(String.valueOf(librarian.getPhoneNo()));
  }

  private void addChangeListener(TextField textField, Librarian librarian, String field) {
    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue) {
        try {
          switch (field) {
            case "name":
              librarian.setName(textField.getText());
              break;
            case "email":
              librarian.setEmail(textField.getText());
              break;
            case "address":
              librarian.setAddress(textField.getText());
              break;
            case "phoneNo":
              librarian.setPhoneNo(Integer.parseInt(textField.getText()));
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
  }

  private void initializeBarChart() {
    CompletableFuture.runAsync(() -> {
      List<Loan> loans = library.getLoans();
      Map<String, Integer> bookCounts = new HashMap<>();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

      // Tạo mảng để lưu số lượng sách cho mượn mỗi ngày trong tuần gần nhất
      Calendar calendar = Calendar.getInstance();

      // Lấy ngày hiện tại và lùi lại 7 ngày
      Date today = new Date();
      calendar.setTime(today);
      calendar.add(Calendar.DAY_OF_YEAR, -7);
      Date weekAgo = calendar.getTime();

      // Đếm số lượng sách cho mượn mỗi ngày
      for (Loan loan : loans) {
        Date issuedDate = loan.getIssuedDate();
        if (issuedDate.after(weekAgo) && issuedDate.before(today)) {
          String dateLabel = dateFormat.format(issuedDate);
          bookCounts.put(dateLabel, bookCounts.getOrDefault(dateLabel, 0) + 1);
        }
      }

      // Update the UI on the JavaFX Application Thread
      Platform.runLater(() -> {
        borrowedBookChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of books");

        calendar.setTime(weekAgo);
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
          String dateLabel = dateFormat.format(calendar.getTime());
          series.getData()
              .add(new XYChart.Data<>(dateLabel, bookCounts.getOrDefault(dateLabel, 0)));
          dates.add(dateLabel);
          calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Update CategoryAxis and NumberAxis
        CategoryAxis xAxis = (CategoryAxis) borrowedBookChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList(dates));
        NumberAxis yAxis = (NumberAxis) borrowedBookChart.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(Collections.max(bookCounts.values()) + 1);
        yAxis.setTickUnit(1);
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
          @Override
          public String toString(Number object) {
            return String.format("%d", object.intValue());
          }
        });

        // Update the BarChart with the series
        borrowedBookChart.getData().setAll(series);
      });
    });
  }

  @FXML
  private void handleHoldBookAction(Book book) {
    // Similar to handleHoldRequest in OnTerminal.java
    Borrower borrower = handleFindBorrower();
    if (borrower != null) {
      showAlert("Place Book on Hold operation", book.makeHoldRequest(borrower));
    }
  }

  @FXML
  private void handleCheckOutBookAction(Book book) {
    if (book.getHoldRequests() == null || book.getHoldRequests().isEmpty()) {
      showAlert("Check Out Operation", "This book has not been requested by any borrower yet.");
      return;
    }
    if (book.getHoldRequests() != null && !book.getHoldRequests().isEmpty()) {
      HoldRequest firstRequest = book.getHoldRequests().get(0);
      if (showConfirmation("Check Out Book Operation",
          "This book has been firstly requested by " + firstRequest.getBorrower().getName()
              + " (ID: " + firstRequest.getBorrower().getID() + ")"
              + ".\nCheck out this book?")) {
        showAlert("Check Out Book Operation",
            book.issueBook(firstRequest.getBorrower(), (Librarian) library.getUser()));
      }
      Loan newLoan = book.getLoan();
      if (newLoan != null) {
        loanList.add(newLoan);
      }
      tableBooks.refresh();
      initializeTableNotifications();
      return;
    }

    // Phần xử lý khi danh sách holdRequests rỗng hoặc null
    Borrower borrower = handleFindBorrower();
    if (borrower != null) {
      if (showConfirmation("Check Out Book Operation",
          "Issue book to this borrower: " + borrower.getName() + ", ID: " + borrower.getID()
              + "?")) {
        showAlert("Place Book on Hold operation",
            book.issueBook(borrower, (Librarian) library.getUser()));
      }

      Loan newLoan = book.getLoan();
      if (newLoan != null) {
        loanList.add(newLoan);
      }
    }
    initializeTableNotifications();
    tableBooks.refresh();
  }

  @FXML
  private void handleCheckInBookAction(Book book) {
    if (!book.getIssuedStatus()) {
      showAlert("Check In Operation", "This book has not been issued yet!");
    } else {
      Loan loan = book.getLoan();
      if (showConfirmation("Check in Confirmation",
          "This book is now borrowed by " + loan.getBorrower().getName()
              + ".\nCheck in this Book?")) {
        // Trả sách và cập nhật thông tin
        String message = book.returnBook(loan.getBorrower(), loan, (Librarian) library.getUser());
        showAlert("Check In Operation", message);

        initializeTableHistory();
        initializeTableNotifications();

        historyTableView.refresh();
        tableBooks.refresh();
      }
    }
  }

  @FXML
  private void handleDeleteBookAction(Book book) {
    if (showConfirmation("Confirm Delete", "Are you sure you want to delete this book?")) {
      String result = library.removeBookfromLibrary(book);

      showAlert("Remove Book from Library", result);

      if (result.endsWith("The book is successfully removed.\n")) {
        bookList.remove(book);
      }
    }
  }

  @FXML
  private void handleShowHoldRequestQueue(Book book) {
    if (book == null) {
      showAlert("Error", "No book selected to show hold requests.");
      return;
    }

    List<HoldRequest> holdRequests = book.getHoldRequests(); // Assume this method exists
    Stage stage = new Stage();
    stage.setTitle("Hold Request Queue for " + book.getTitle());

    if (holdRequests.isEmpty()) {
      showAlert("Information", "No hold requests available for this book.");
      return;
    }

    TableView<HoldRequest> table = new TableView<>();
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    TableColumn<HoldRequest, String> noColumn = new TableColumn<>("No.");
    noColumn.setCellValueFactory(data -> new SimpleStringProperty(
        String.valueOf(holdRequests.indexOf(data.getValue()) + 1)));
    noColumn.setPrefWidth(40);

    TableColumn<HoldRequest, String> titleColumn = new TableColumn<>("Book's Title");
    titleColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
    titleColumn.setCellFactory(column -> {
      return new TableCell<HoldRequest, String>() {
        private final Text text;

        {
          text = new Text();
          text.wrappingWidthProperty().bind(column.widthProperty());
          setGraphic(text);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setText(null);
          } else {
            text.setText(item);
          }
        }
      };
    });
    titleColumn.setPrefWidth(500);

    TableColumn<HoldRequest, Integer> borrowerIDColumn = new TableColumn<>("BorrowerID");
    borrowerIDColumn.setCellValueFactory(
        data -> new SimpleIntegerProperty(data.getValue().getBorrower().getID()).asObject());
    borrowerIDColumn.setPrefWidth(90);

    TableColumn<HoldRequest, String> borrowerColumn = new TableColumn<>("Borrower's Name");
    borrowerColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getBorrower().getName()));
    borrowerColumn.setPrefWidth(150);

    TableColumn<HoldRequest, String> dateColumn = new TableColumn<>("Request Date");
    dateColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getRequestDate().toString()));

    table.getColumns().addAll(noColumn, titleColumn, borrowerIDColumn, borrowerColumn, dateColumn);

    ObservableList<HoldRequest> holdRequestList = FXCollections.observableArrayList(holdRequests);
    table.setItems(holdRequestList);

    VBox vbox = new VBox(10);
    vbox.setPadding(new Insets(10));
    vbox.getChildren().addAll(new Label("Hold Request Queue for: " + book.getTitle()), table);

    Scene scene = new Scene(vbox, 1000, 400);
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
  }

  @FXML
  private Borrower handleFindBorrower() {
    // Create the custom dialog
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Search Borrower");
    dialog.setHeaderText("Search for a Borrower using specific criteria");

    // Set the dialog's buttons
    ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

    // Create a dropdown (ChoiceBox) and a TextField
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    choiceBox.getItems().addAll("ID", "Name");
    choiceBox.setValue("ID"); // Default selection

    TextField textField = new TextField();
    textField.setPromptText("Enter search value");

    // Layout: Arrange the ChoiceBox and TextField
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    grid.add(new Label("Search By:"), 0, 0);
    grid.add(choiceBox, 1, 0);
    grid.add(new Label("Search Value:"), 0, 1);
    grid.add(textField, 1, 1);

    dialog.getDialogPane().setContent(grid);

    // Convert the result when the "Search" button is clicked
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == searchButtonType) {
        return new Pair<>(choiceBox.getValue(), textField.getText());
      }
      return null;
    });

    // Show the dialog and process the result
    Optional<Pair<String, String>> result = dialog.showAndWait();
    if (result.isPresent()) {
      String searchType = result.get().getKey();
      String searchValue = result.get().getValue();

      // Perform the search based on the selected type
      Borrower borrower = null;
      try {
        switch (searchType) {
          case "ID":
            borrower = library.findBorrowerById(Integer.parseInt(searchValue));
            break;
          case "Name":
            borrower = library.findBorrowerByName(searchValue);
            break;
          default:
            break;
        }

        // Display the result
        if (borrower != null) {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Borrower Found");
          alert.setHeaderText("Borrower's Name");
          alert.setContentText(borrower.getName());
          alert.showAndWait();
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Borrower Not Found");
          alert.setHeaderText("No Borrower Found");
          alert.setContentText("No borrower matches the provided information.");
          alert.showAndWait();
        }
      } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
      }
      return borrower;
    }

    return null;
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
  private void handleShowInfo(ActionEvent event) {
    paneHome.setVisible(false);
    paneInformation.setVisible(true);
  }

  private void showPane(AnchorPane paneToShow) {
    paneBooks.setVisible(false);
    paneUsers.setVisible(false);
    paneDashboard.setVisible(false);
    paneHistory.setVisible(false);
    paneAddBook.setVisible(false);
    paneNotifications.setVisible(false);
    paneToShow.setVisible(true);
  }

  @FXML
  void handleAddBook() {
    showPane(paneAddBook);

    // Thiết lập cột với thuộc tính của lớp Book
    addBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    addBookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

    // Thiết lập cột hành động với nút "Add"
    addBookBtnColumn.setCellFactory(column -> new TableCell<>() {
      private final Button addButton = new Button("Add");

      {
        addButton.setOnAction(event -> {
          Book book = getTableView().getItems().get(getIndex());

          // Kiểm tra xem sách đã tồn tại trong danh sách hay chưa
          boolean bookExists = bookList.stream().anyMatch(
              existingBook -> existingBook.getTitle().equalsIgnoreCase(book.getTitle())
                  && existingBook.getAuthor().equalsIgnoreCase(book.getAuthor()));

          if (bookExists) {
            // Hiển thị thông báo nếu sách đã tồn tại
            System.out.println("Book already exists: " + book.getTitle());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Duplicate Book");
            alert.setHeaderText("This book already exists in the library.");
            alert.setContentText("You cannot add the same book again.");
            alert.showAndWait();
          } else {
            // Thêm sách mới nếu chưa tồn tại
            System.out.println("Adding book: " + book.getTitle());
            Book newBook = new Book(book.getCurrentIdNumber() + 1, book.getTitle(),
                book.getDescription(), book.getAuthor(), book.getIssuedStatus(),
                book.getImageLink(),
                book.getPreviewLink());
            bookList.add(newBook);
            library.addBookinLibrary(newBook);
          }
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(addButton);
        }
      }
    });

    tableAddBooks.setItems(apiBooksList);

    // Thực hiện debounce và throttle tìm kiếm
    bookApiSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || newValue.trim().isEmpty()) {
        apiBooksList.clear(); // Xóa bảng nếu từ khóa tìm kiếm trống
        return;
      }

      // Chỉ tìm kiếm nếu từ khóa có tối thiểu 3 ký tự
      if (newValue.trim().length() < 3) {
        return;
      }

      // Cập nhật giá trị tìm kiếm hiện tại
      searchQuery.set(newValue.trim());

      // Hủy bỏ nhiệm vụ đã lên lịch trước đó nếu có
      if (scheduledFuture != null && !scheduledFuture.isDone()) {
        scheduledFuture.cancel(false);
      }

      // Lên lịch gọi API sau 500ms (debounce)
      scheduledFuture = debounceScheduler.schedule(this::performSearch, debounceDelay,
          TimeUnit.MILLISECONDS);
    });
  }

  private void performSearch() {
    String query = searchQuery.get();
    CompletableFuture.supplyAsync(() -> {
      try {
        // Chỉ gọi API mỗi giây một lần để tránh quá tải (throttle)
        if (System.currentTimeMillis() - lastApiCallTime < 500) {
          return null;
        }

        lastApiCallTime = System.currentTimeMillis();
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=10";
        String jsonResponse = apiTest.getHttpResponse(apiUrl);

        if (jsonResponse != null) {
          return apiTest.getBooksFromJson(jsonResponse);
        }
      } catch (Exception e) {
        System.out.println("Lỗi khi gọi API: " + e.getMessage());
      }
      return null;
    }).thenAccept(booksFromAPI -> {
      if (booksFromAPI == null) {
        return;
      }

      // Chỉ cập nhật nếu kết quả khác biệt
      Platform.runLater(() -> {
        if (!apiBooksList.equals(booksFromAPI)) {
          apiBooksList.clear();
          apiBooksList.addAll(booksFromAPI);
        }
      });
    });
  }

  @FXML
  void handleBack(ActionEvent event) {
    handleChangeInfo();
    paneInformation.setVisible(false);
    paneHome.setVisible(true);
  }

  @FXML
  void handleBooks(ActionEvent event) {
    showPane(paneBooks);
  }

  @FXML
  void handleDashBoard(ActionEvent event) {
    showPane(paneDashboard);
  }

  @FXML
  void handleHistory(ActionEvent event) {
    showPane(paneHistory);
  }

  @FXML
  void handleNotifications(ActionEvent event) {
    showPane(paneNotifications);
  }

  @FXML
  void handleUsers(ActionEvent event) {
    showPane(paneUsers);
  }

  @FXML
  void handleBackFromPaneAPI(ActionEvent event) {
    showPane(paneBooks);
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
    Librarian librarian = (Librarian) library.getUser();
    String name = librarian.getName();
    String email = librarian.getEmail();
    String address = librarian.getAddress();
    String phone = String.valueOf(librarian.getPhoneNo());

    String newName = infoName.getText();
    String newEmail = infoEmail.getText();
    String newAddress = infoAddress.getText();
    String newPhone = infoPhone.getText();

    if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty() || phone.isEmpty()) {
      showAlert("Error", "These info can not be Empty.");
    } else {
      if (!newName.equals(name) || !newEmail.equals(email) || !newAddress.equals(address) || !newPhone.equals(phone)) {
        if (showConfirmation("Change Information", "Are you sure you want to change your information?")) {
          librarian.setName(newName);
          librarian.setEmail(newEmail);
          librarian.setAddress(newAddress);
          librarian.setPhoneNo(Integer.parseInt(newPhone));
          showAlert("Change Information", "Your information has been changed successfully.");
        } else {
          initializeInformation();
        }
      }
    }
  }
}
