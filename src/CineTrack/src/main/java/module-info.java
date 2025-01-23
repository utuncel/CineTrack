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
  requires java.desktop;
  requires java.naming;
  requires org.hibernate.orm.core;
  requires java.sql;
  requires mysql.connector.j;

  exports org.com to javafx.graphics;
  exports org.com.controller.dashboard;
  exports org.com.controller.cinematics;
  exports org.com.model.domain;
  exports org.com.model.domain.statistics;
  exports org.com.model.records;
  exports org.com.model.enums;
  exports org.com.controller;
  exports org.com.controller.cinematics.helper;
  exports org.com.controller.dataimport to javafx.fxml;
  exports org.com.controller.addcinematic to javafx.fxml;
  exports org.com.controller.log to javafx.fxml;
  exports org.com.controller.authentication;
  exports org.com.model.models;
}