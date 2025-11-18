module org.imc {
    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires lombok;

    opens org.imc.model to org.hibernate.orm.core;   // <<< ESSENCIAL
    opens org.imc.controller to javafx.fxml;

    exports org.imc.principal;
    exports org.imc.controller;
}
