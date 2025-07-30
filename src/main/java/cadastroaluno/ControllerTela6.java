package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.TurmaDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.List;


public class ControllerTela6 {
    @FXML
    private Button btnVoltar, btnConfirmar;
    @FXML
    private TextField caixaTurma;
    @FXML
    private ChoiceBox caixaCurso, caixaTurno;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;
    private final ObservableList<String> listaCursos = TurmaDAO.listaCurso;
    private final ObservableList<String> listaTurno = TurmaDAO.listaTurno;

    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        caixaCurso.setItems(listaCursos);
        caixaTurno.setItems(listaTurno);

        btnVoltar.setOnMouseClicked(event -> controle.trocarTela("HelloController", "hello-view.fxml", event));
        btnConfirmar.setOnMouseClicked(event -> {
            String turma = caixaTurma.getText();
            String curso = (String) caixaCurso.getValue();
            String turno = (String) caixaTurno.getValue();
            TurmaDAO.inserirTurmas(conexao, turma, curso, turno);
            controle.trocarTela("nada", "hello-view.fxml", event);
        });
    }
}