package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.Connection;

public class Controller5_1 {
    @FXML
    private Button btnVoltar;
    @FXML
    private TextArea textoSaida;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;

    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        btnVoltar.setOnMouseClicked(event ->controle.trocarTela("HelloController","hello-view.fxml", event));

        carregar();
    }

    private void carregar() {
        Aluno aluno = alunoDAOimpl.alunoCopia;
        textoSaida.appendText("nome: " + aluno.getNome() + "\nCPF: " + aluno.getCpf() + "\nMatricula: " + aluno.getMatricula() +
                "\nCurso: " + aluno.getCurso() + "\nTelefone: " + aluno.getTelefone() + "\nData Nasc: " + AlunoDAOimpl.converterData(aluno.getDatanascimento()));
    }
}
