package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.Connection;

public class ControllerTela3 {
    @FXML
    private Button btnVoltar, btnFiltrarTodos, btnFiltrarNome, btnFiltrarMatricula;

    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        btnVoltar.setOnMouseClicked(event ->controle.trocarTela("HelloController","hello-view.fxml", event));
        btnFiltrarTodos.setOnMouseClicked(event -> {controle.trocarTela("HelloController", "telaOpcao2-view.fxml", event);});
        btnFiltrarNome.setOnMouseClicked(event -> {controle.trocarTela("HelloController", "telaOpcao3_1-view.fxml", event);});
        btnFiltrarMatricula.setOnMouseClicked(event -> {controle.trocarTela("HelloController", "telaOpcao3_2-view.fxml", event);});
    }
}