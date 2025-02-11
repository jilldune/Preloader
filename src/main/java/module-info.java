module com.edubiz.preloader {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.edubiz.preloader to javafx.fxml;
    exports com.edubiz.preloader;
    exports com.edubiz.preloader.classes.interfaces;
}