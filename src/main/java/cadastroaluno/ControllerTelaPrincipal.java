package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.CriarTabela;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;



public class ControllerTelaPrincipal {
    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;
    @FXML
    private Button btnCadastrar, btnMostrar, btnBuscar, btnAtualizar, btnDeletar, btnSair, btnTurma, btnMostrarTurma;
    @FXML
    public void initialize() {

        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);

        btnCadastrar.setOnMouseClicked(event -> controle.trocarTela("HelloController", "telaOpcao1-view.fxml", event));
        btnMostrar.setOnMouseClicked(event -> controle.trocarTela("TelaOpcao2", "telaOpcao2-view.fxml", event));
        btnBuscar.setOnMouseClicked(event -> controle.trocarTela("TelaOpcao3", "telaOpcao3-view.fxml", event));
        btnAtualizar.setOnMouseClicked(event -> controle.trocarTela("TelaOpcao4", "telaOpcao4-view.fxml", event));
        btnTurma.setOnMouseClicked(event -> controle.trocarTela("TelaOpcao5", "telaOpcao6-view.fxml", event));
        btnMostrarTurma.setOnMouseClicked(event -> controle.trocarTela("TelaOpcao6", "telaOpcao7-view.fxml", event));
        btnDeletar.setOnMouseClicked(event -> controle.trocarTela("TelaOpcao7", "telaOpcao5-view.fxml", event));
        btnSair.setOnMouseClicked(event -> Platform.exit());
    }

}
