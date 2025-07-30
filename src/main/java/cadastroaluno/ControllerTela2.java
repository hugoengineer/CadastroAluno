package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.TurmaDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerTela2 {
    @FXML
    private Button btnVoltar;
    @FXML
    private TextArea textoSaida;
    @FXML
    private TableView<Aluno> tableTabela;
    @FXML
    private TableColumn<Aluno, String> colNome;
    @FXML
    private TableColumn<Aluno, Integer> colMatricula;
    @FXML
    private TableColumn<Aluno, String> colCpf;
    @FXML
    private TableColumn<Aluno, String> colCurso;
    @FXML
    private TableColumn<Aluno, String> colTurno;
    @FXML
    private TableColumn<Aluno, String> colTurma;
    @FXML
    private TableColumn<Aluno, String> colTelefone;
    @FXML
    private TableColumn<Aluno, Date> colDataNascimento;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;


    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        configurarTabela();
        carregarAlunosNaTabela();
        btnVoltar.setOnMouseClicked(event -> controle.trocarTela("HelloController", "hello-view.fxml", event));

        carregarAlunosNaTabela();
    }
    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colTurma.setCellValueFactory(new PropertyValueFactory<>("turma"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colDataNascimento.setCellValueFactory(new PropertyValueFactory<>("datanascimento"));
    }
    private void carregarAlunosNaTabela() {
        ObservableList<Aluno> alunos = TurmaDAO.lerTurmas("Nenhum", conexao);
        tableTabela.setItems(alunos);
        tableTabela.refresh();
    }
}

