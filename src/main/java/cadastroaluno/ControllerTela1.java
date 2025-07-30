package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.Year;
import java.util.Random;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;

public class ControllerTela1 {
    @FXML
    private TextField caixaNome, caixaTelefone, caixaCpf;
    @FXML
    private ChoiceBox caixaCurso;
    @FXML
    private ChoiceBox caixaTurno;
    @FXML
    private ChoiceBox caixaTurma;
    @FXML
    private DatePicker data;
    @FXML
    private Button btnVoltar, btnConfirmar;
    @FXML
    private Text textoSaida;

    private Stage stage;
    private final ObservableList<String> listaCursos = TurmaDAO.listaCurso;
    private final ObservableList<String> listaTurma = TurmaDAO.listaTurma;
    private final ObservableList<String> listaTurno = TurmaDAO.listaTurno;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;


    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        caixaCurso.setItems(listaCursos);
        caixaTurno.setItems(listaTurno);
        caixaTurma.setItems(listaTurma);
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        Aluno alunoCopia = null;

        btnVoltar.setOnMouseClicked(event -> controle.trocarTela("HelloController", "hello-view.fxml", event));
        btnConfirmar.setOnMouseClicked(event -> {
            String nome = caixaNome.getText().strip();
            String telefone = caixaTelefone.getText().strip();
            String cpf = caixaCpf.getText().strip();
            LocalDate dataNasc = data.getValue();
            String curso = (String) caixaCurso.getValue();
            String turno = (String) caixaTurno.getValue();
            String turma = (String) caixaTurma.getValue();
            try {
                validarDados(telefone, cpf);
                int matricula = gerarMatricula();
                java.sql.Date dataSql = java.sql.Date.valueOf(dataNasc);
                Aluno aluno = new Aluno(nome, cpf, curso, matricula,telefone, dataSql, turno, turma);
                cadastrar(aluno);
                controle.trocarTela("HelloController", "telaOpcao1_1-view.fxml", event);
            } catch (IllegalArgumentException e) {
                textoSaida.setText(e.getMessage());
            }
        });
        configurarValidacaoCampos();
    }

    private void validarDados(String telefone, String cpf) {
        if (!telefone.matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone deve conter 10 ou 11 dígitos.");
        }
        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos.");
        }
    }

    private void cadastrar(Aluno aluno) {
        alunoDAOimpl.inserirAluno(aluno);
        alunoDAOimpl.copiarAluno(aluno);
    }

    private void configurarValidacaoCampos() {
        UnaryOperator<TextFormatter.Change> filtroCpf = change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.matches("\\d{0,11}")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatterCpf = new TextFormatter<>(filtroCpf);
        caixaCpf.setTextFormatter(textFormatterCpf);

        UnaryOperator<TextFormatter.Change> filtroTelefone = change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.matches("\\d{0,11}")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatterTelefone = new TextFormatter<>(filtroTelefone);
        caixaTelefone.setTextFormatter(textFormatterTelefone);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private int gerarMatricula(){
        Random random = new Random();
        int numeroAleatorio1 = random.nextInt(99);
        int numeroAleatorio2= random.nextInt(99);
        int numeroAleatorio3 = random.nextInt(99);
        String matriculaGerada = Year.now().getValue() + String.valueOf(numeroAleatorio1) + numeroAleatorio3
                + numeroAleatorio2;
        return Integer.parseInt(matriculaGerada);
    }
}
