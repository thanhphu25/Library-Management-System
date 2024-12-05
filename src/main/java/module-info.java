module org.example.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.librarymanagementsystem to javafx.fxml;
    exports org.example.librarymanagementsystem;
}