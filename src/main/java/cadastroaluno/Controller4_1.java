package cadastroaluno;

import com.example.cadastroaluno.HelloApplication;
import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.TurmaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class Controller4_1 {
    @FXML
    private Button btnVoltar, btnConfirmar;
    @FXML
    private ChoiceBox caixaCurso, caixaTurno, caixaTurma;
    @FXML
    private TextField caixaNome, caixaTelefone;

    private final ObservableList<String> listaCursos = TurmaDAO.listaCurso;
    private final ObservableList<String> listaTurma = TurmaDAO.listaTurma;
    private final ObservableList<String> listaTurno = TurmaDAO.listaTurno;

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;

    @FXML
    private void initialize() {
        HelloApplication controle = new HelloApplication();
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);
        caixaCurso.setItems(listaCursos);
        caixaTurno.setItems(listaTurno);
        caixaTurma.setItems(listaTurma);
        btnVoltar.setOnMouseClicked(event ->controle.trocarTela("HelloController","telaOpcao4-view.fxml", event));
        btnConfirmar.setOnMouseClicked(event ->{
            String nome = caixaNome.getText();
            String telefone = caixaTelefone.getText();
            String curso = (String) caixaCurso.getValue();
            String turma = (String) caixaTurma.getValue();
            String turno = (String) caixaTurno.getValue();
            atualizar(nome, telefone, curso, turma, turno);
            controle.trocarTela("HelloController","telaOpcao4_2.fxml", event);
        });
    }
    private void atualizar(String nome, String telefone, String curso, String turno, String turma) {
        Aluno alunoCopia = alunoDAOimpl.alunoCopia;
        if(!nome.isEmpty()){
            alunoCopia.setNome(nome);
        }if(!telefone.isEmpty()){
            alunoCopia.setTelefone(telefone);
        }if(!curso.isEmpty()){
            alunoCopia.setCurso(curso);
        }if(!turno.isEmpty()) {
            alunoCopia.setTurno(turno);
        }if(!curso.isEmpty()) {
            alunoCopia.setTurma(turma);
        }
        Aluno alunoAtualizado = new Aluno(alunoCopia.getNome(), alunoCopia.getCpf(), alunoCopia.getCurso(),
                alunoCopia.getMatricula(), alunoCopia.getTelefone(), alunoCopia.getDatanascimento(), alunoCopia.getTurno(),
                alunoCopia.getTurma());
        alunoDAOimpl.atualizarAluno(alunoAtualizado);
    }
}