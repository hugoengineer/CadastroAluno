package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.TurmaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;

public class Controller1_1 {
    @FXML
    private Button btnVoltar;
    @FXML
    private TextArea textoSaida;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;
    Aluno aluno = alunoDAOimpl.alunoCopia;


    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        btnVoltar.setOnMouseClicked(event ->controle.trocarTela("HelloController","hello-view.fxml", event));

        carregar();
    }

    private void carregar() {
        textoSaida.appendText("nome: " + aluno.getNome() + "\nCPF: " + aluno.getCpf() + "\nMatricula: " + aluno.getMatricula() +
                "\nCurso: " + aluno.getCurso() + "\nTurno: " + aluno.getTurno() + "\nTurma:" + aluno.getTurma() +
                "\nTelefone: " + aluno.getTelefone() + "\nData Nasc: " + AlunoDAOimpl.converterData(aluno.getDatanascimento()));
    }
}
