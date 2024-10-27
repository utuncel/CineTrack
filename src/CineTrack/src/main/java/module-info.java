open module CineTrack {

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
  requires javafx.base;

  exports org.com to javafx.graphics;
  exports org.com.controller.dashboard;
  exports org.com.controller.cinematics;
  exports org.com.models.helper;
  exports org.com.models;
  exports org.com.models.statistics;
  exports org.com.models.records;
  exports org.com.models.enums;
  exports org.com.controller;
  exports org.com.controller.cinematics.helper;
  exports org.com.controller.dataimport to javafx.fxml;
  exports org.com.controller.addcinematic to javafx.fxml;
  exports org.com.controller.logger to javafx.fxml;
  exports org.com.models.logger to javafx.fxml;
}