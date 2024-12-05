module group {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
  requires com.h2database;
  requires java.smartcardio;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.sql;
  requires javafx.swing;
  requires com.google.zxing;
  requires com.google.zxing.javase;

  opens LMS to javafx.fxml;
  exports LMS;
  opens Controller to javafx.fxml;
  exports Controller;
  exports Interface;
  opens Interface to javafx.fxml;
}