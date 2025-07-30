package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.List;


public class ControllerTela5 {
    @FXML
    private Button btnVoltar, btnConfirmar, btnAtualizar;
    @FXML
    private TextArea textoSaida;
    @FXML
    private TextField caixaNome;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;
    boolean flag = false;
    Aluno alunoCopia;

    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        btnVoltar.setOnMouseClicked(event -> controle.trocarTela("HelloController", "hello-view.fxml", event));
        btnConfirmar.setOnMouseClicked(event -> {String texto = caixaNome.getText();
            int matricula = Integer.parseInt(texto);
            carregarAlunos(matricula);});
        btnAtualizar.setDisable(true);
        btnAtualizar.setOnMouseClicked(event ->{
            alunoDAOimpl.deletarAluno(alunoCopia);
            controle.trocarTela("HelloController", "telaOpcao5_1-view.fxml", event);});
    }
    private void carregarAlunos(int matricula) {
        List<Aluno> alunos = alunoDAOimpl.buscarPorMatricula(matricula);

        if (alunos != null && !alunos.isEmpty()) {
            textoSaida.clear();
            for (Aluno aluno : alunos) {
                textoSaida.appendText("Nome: " + aluno.getNome() + "\n" +
                        "CPF: " + aluno.getCpf() + "\n" +
                        "Matrícula: " + aluno.getMatricula() + "\n" +
                        "Curso: " + aluno.getCurso() + "\n" +
                        "Turno: " + aluno.getTurno() + "\n" +
                        "Turma: " + aluno.getTurma() + "\n" +
                        "Telefone: " + aluno.getTelefone() + "\n" +
                        "Data de Nascimento: " + AlunoDAOimpl.converterData(aluno.getDatanascimento()) + "\n\n");
                alunoCopia = aluno;
                alunoDAOimpl.copiarAluno(alunoCopia);
            }
            flag = true;
            btnAtualizar.setDisable(false);

        } else {
            textoSaida.setText("Não há alunos cadastrados.");
        }
    }
}