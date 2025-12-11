module org.akash.messcup {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires java.logging;


    opens org.akash.messcup to javafx.fxml;
    exports org.akash.messcup;
    opens org.akash.messcup.controllers to javafx.fxml;
    exports org.akash.messcup.controllers;
    exports org.akash.messcup.menuDto;
}