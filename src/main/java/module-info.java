module org.akash.messcup {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.akash.messcup to javafx.fxml;
    exports org.akash.messcup;
    opens org.akash.messcup.controllers to javafx.fxml;
    exports org.akash.messcup.controllers;
}