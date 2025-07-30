module com.example.cadastroaluno {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    requires java.sql;




    opens com.example.cadastroaluno to javafx.fxml;
    exports com.example.cadastroaluno;


    opens cadastroaluno to javafx.fxml;

    opens com.example.cadastroaluno.src to javafx.base;
    exports com.example.cadastroaluno.src;
}