package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.TurmaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public class ControllerTela7 {
    @FXML
    private Button btnVoltar, btnConfirmar;
    @FXML
    private TableView<Aluno> tableTabela;
    @FXML
    private ChoiceBox caixaFiltro;
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
    private  ObservableList<String> listaTurma;
    private  ObservableList<Aluno> listaAluno;


    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        listaTurma = FXCollections.observableArrayList(TurmaDAO.listaTurma);
        listaTurma.add("Nenhum");
        caixaFiltro.setItems(listaTurma);
        configurarTabela();
        carregarAlunosNaTabela("Nenhum");

        btnVoltar.setOnMouseClicked(event -> controle.trocarTela("HelloController", "hello-view.fxml", event));
        btnConfirmar.setOnMouseClicked(event -> {
            String filtro = (String) caixaFiltro.getValue();
            carregarAlunosNaTabela(filtro);
        });
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

    private void carregarAlunosNaTabela(String filtro) {
        ObservableList<Aluno> alunos = TurmaDAO.lerTurmas(filtro, conexao);
        tableTabela.setItems(alunos);
        tableTabela.refresh();
    }
}