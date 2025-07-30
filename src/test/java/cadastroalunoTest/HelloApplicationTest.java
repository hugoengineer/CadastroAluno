package cadastroalunoTest;

import com.example.cadastroaluno.HelloApplication;
import javafx.scene.control.Label;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class HelloApplicationTest {

    private Connection connection;
    private HelloApplication loginDAO;
    private TestLabel testErroLabel;

    private static class TestLabel extends Label {
        private String capturedText;

        public String getCapturedText() {
            return capturedText;
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_db_login;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE login (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) UNIQUE, senha VARCHAR(255), tipo VARCHAR(25));");
            stmt.execute("CREATE TABLE cadastro (id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(255), matricula INT UNIQUE, telefone VARCHAR(20), data_de_nascimento DATE, curso VARCHAR(255), cpf VARCHAR(14) UNIQUE, turno VARCHAR(50), turma VARCHAR(50));");

            // Inserir dados de teste
            stmt.execute("INSERT INTO login (username, senha, tipo) VALUES ('adminUser', 'adminPass', 'adm');");
            stmt.execute("INSERT INTO login (username, senha, tipo) VALUES ('regularUser', 'regularPass', 'user');");

            stmt.execute("INSERT INTO cadastro (nome, matricula, telefone, data_de_nascimento, curso, cpf, turno, turma) VALUES ('Aluno Teste', 12345, '111', '2000-01-01', 'Eng', '123', 'M', 'A');");
            stmt.execute("INSERT INTO cadastro (nome, matricula, telefone, data_de_nascimento, curso, cpf, turno, turma) VALUES ('Outro Aluno', 67890, '222', '2000-02-02', 'Dir', '456', 'V', 'B');");
        }

        testErroLabel = new TestLabel();
        loginDAO = new HelloApplication();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testValidarAdmSuccess() {
        assertTrue(loginDAO.validarAdm("adminUser", "adminPass"), "Deve validar um administrador com credenciais corretas.");
        assertEquals("", testErroLabel.getCapturedText(), "A mensagem de erro deve ser limpa em caso de sucesso.");
    }

    @Test
    void testValidarAdmWrongPassword() {
        assertFalse(loginDAO.validarAdm("adminUser", "wrongPass"), "Não deve validar administrador com senha incorreta.");
        assertEquals("", testErroLabel.getCapturedText(), "A mensagem de erro deve ser limpa mesmo em falha de credencial."); // Não há erro SQL, então limpa.
    }

    @Test
    void testValidarAdmNonExistentUser() {
        assertFalse(loginDAO.validarAdm("nonExistentUser", "anyPass"), "Não deve validar usuário inexistente.");
        assertEquals("", testErroLabel.getCapturedText(), "A mensagem de erro deve ser limpa mesmo em falha de credencial.");
    }

    @Test
    void testValidarAdmSQLException() throws SQLException {
        connection.close();

        assertFalse(loginDAO.validarAdm("adminUser", "adminPass"), "Deve retornar false em caso de SQLException.");
        assertTrue(testErroLabel.getCapturedText().startsWith("Erro ao validar Administrador:"), "Deve definir a mensagem de erro apropriada.");
    }


    @Test
    void testValidarAlunoSuccess() {
        assertTrue(loginDAO.validarAluno("Aluno Teste", "12345"), "Deve validar um aluno com nome e matrícula corretos.");
        assertEquals("", testErroLabel.getCapturedText(), "A mensagem de erro deve ser limpa em caso de sucesso.");
    }

    @Test
    void testValidarAlunoWrongMatricula() {
        assertFalse(loginDAO.validarAluno("Aluno Teste", "99999"), "Não deve validar aluno com matrícula incorreta.");
        assertEquals("", testErroLabel.getCapturedText(), "A mensagem de erro deve ser limpa mesmo em falha de credencial.");
    }

    @Test
    void testValidarAlunoNonExistentNome() {
        assertFalse(loginDAO.validarAluno("Aluno Inexistente", "12345"), "Não deve validar aluno com nome inexistente.");
        assertEquals("", testErroLabel.getCapturedText(), "A mensagem de erro deve ser limpa mesmo em falha de credencial.");
    }

    @Test
    void testValidarAlunoInvalidMatriculaFormat() {
        assertFalse(loginDAO.validarAluno("Aluno Teste", "abcde"), "Deve retornar false se a matrícula não for um número.");
        assertEquals("Erro: Matrícula inválida. Deve ser um número.", testErroLabel.getCapturedText(), "Deve definir a mensagem de erro para formato inválido.");
    }

    @Test
    void testValidarAlunoSQLException() throws SQLException {
        connection.close();

        assertFalse(loginDAO.validarAluno("Aluno Teste", "12345"), "Deve retornar false em caso de SQLException.");
        assertTrue(testErroLabel.getCapturedText().startsWith("Erro ao validar Aluno:"), "Deve definir a mensagem de erro apropriada.");
    }
}