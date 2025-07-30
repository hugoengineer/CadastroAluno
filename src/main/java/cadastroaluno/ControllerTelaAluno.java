package cadastroaluno;

import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.util.List;

public class ControllerTelaAluno {
    @FXML
    private Button btnVoltar;
    @FXML
    private TextArea textoSaida;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;
    private static Usuario usuarioLogado;

    public void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    @FXML
    private void initialize() {
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);

        if (usuarioLogado != null) {
            int matricula = Integer.parseInt(usuarioLogado.getSenha());
            List<Aluno> alunoEncontrado =  alunoDAOimpl.buscarPorMatricula(matricula);

            if (!alunoEncontrado.isEmpty()) {
                Aluno aluno = alunoEncontrado.get(0);
                textoSaida.appendText("Nome: " + aluno.getNome() + "\n" +
                        "CPF: " + aluno.getCpf() + "\n" +
                        "Matrícula: " + aluno.getMatricula() + "\n" +
                        "Curso: " + aluno.getCurso()+ "\n" +
                        "Turno: " + aluno.getTurno() + "\n" +
                        "Turma: " + aluno.getTurma() + "\n" +
                        "Telefone: " + aluno.getTelefone() + "\n" +
                        "Data de Nascimento: " + AlunoDAOimpl.converterData(aluno.getDatanascimento()) + "\n\n");
            } else {
                textoSaida.appendText("Aluno com a matrícula " + matricula + " não encontrado.\n");
            }

        } else {
            textoSaida.appendText("Erro: Usuário não logado.\n");
        }

        btnVoltar.setOnMouseClicked(event -> Platform.exit());
    }
}