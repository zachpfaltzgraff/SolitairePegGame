module com.example.solitairepeggame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.solitairepeggame to javafx.fxml;
    exports com.example.solitairepeggame;
}