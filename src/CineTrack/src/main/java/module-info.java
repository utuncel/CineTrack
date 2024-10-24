module CineTrack {

  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires javafx.graphics;
  requires com.fasterxml.jackson.databind;
  requires java.net.http;

  opens org.com to javafx.fxml;
  opens org.com.models.helper to com.fasterxml.jackson.databind;
  exports org.com to javafx.graphics;
  exports org.com.controller.dashboard;
  exports org.com.controller.cinematics;
  exports org.com.models.helper;
  exports org.com.models;
  opens org.com.models to com.fasterxml.jackson.databind;
  exports org.com.models.statistics;
  opens org.com.models.statistics to com.fasterxml.jackson.databind;
  exports org.com.models.records;
  opens org.com.models.records to com.fasterxml.jackson.databind;
  opens org.com.controller.dashboard to javafx.fxml;
  exports org.com.models.enums;
  exports org.com.controller;
  opens org.com.controller to javafx.fxml;
  opens org.com.controller.cinematics to javafx.fxml;
  exports org.com.controller.cinematics.helper;
  opens org.com.controller.cinematics.helper to javafx.fxml;
  exports org.com.controller.dataImport to javafx.fxml;
  opens org.com.controller.dataImport to javafx.fxml;
}