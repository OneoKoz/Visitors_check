module visitors {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.kozyrev.Main;
    exports com.kozyrev.Main;

    opens com.kozyrev.Controller;
    exports com.kozyrev.Controller to javafx.fxml;

    opens com.kozyrev.Fields;
    exports com.kozyrev.Fields to javafx.base;
}