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
    opens org.com.Models.Helper to  com.fasterxml.jackson.databind;
    exports org.com to javafx.graphics;
    exports org.com.Controller.Dashboard;
    exports org.com.Models.Helper;
    exports org.com.Models;
    opens org.com.Models to com.fasterxml.jackson.databind;
    exports org.com.Models.Statistic;
    opens org.com.Models.Statistic to com.fasterxml.jackson.databind;
    exports org.com.Models.Records;
    opens org.com.Models.Records to com.fasterxml.jackson.databind;
    opens org.com.Controller.Dashboard to javafx.fxml;
    exports org.com.Models.Enums;
}