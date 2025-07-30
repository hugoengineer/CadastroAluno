package cadastroalunoTest; // Mantenha no seu pacote de testes

import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.TurmaDAO;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurmaDAOTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test_db_turma;DB_CLOSE_DELAY=-1");

        TurmaDAO.listaCurso.clear();
        TurmaDAO.listaTurma.clear();
        TurmaDAO.listaTurno.clear();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE turma (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "curso VARCHAR(100) NOT NULL," +
                    "turma VARCHAR(50) NOT NULL," +
                    "turno VARCHAR(50) NOT NULL" +
                    ");");
            stmt.execute("CREATE TABLE cadastro (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "matricula INT UNIQUE NOT NULL," +
                    "nome VARCHAR(255) NOT NULL," +
                    "telefone VARCHAR(20)," +
                    "data_de_nascimento DATE," +
                    "curso VARCHAR(255)," +
                    "cpf VARCHAR(14) UNIQUE," +
                    "turno VARCHAR(50)," +
                    "turma VARCHAR(50)" +
                    ");");
            stmt.execute("CREATE TABLE alunoturma (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "idaluno INT NOT NULL," +
                    "idturma INT NOT NULL," +
                    "FOREIGN KEY (idaluno) REFERENCES cadastro(id)," +
                    "FOREIGN KEY (idturma) REFERENCES turma(id)" +
                    ");");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testPreencherTabelaTurmaInsertsDataAndLoadsLists() throws SQLException {
        TurmaDAO.preencherTabelaTurma(connection);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM turma")) {
            assertTrue(rs.next());
            assertEquals(5, rs.getInt(1), "Deve inserir 5 registros na tabela turma.");
        }

        assertFalse(TurmaDAO.listaCurso.isEmpty(), "listaCurso não deve estar vazia.");
        assertFalse(TurmaDAO.listaTurma.isEmpty(), "listaTurma não deve estar vazia.");
        assertFalse(TurmaDAO.listaTurno.isEmpty(), "listaTurno não deve estar vazia.");

        assertTrue(TurmaDAO.listaCurso.contains("Historia"));
        assertTrue(TurmaDAO.listaTurma.contains("215F"));
        assertTrue(TurmaDAO.listaTurno.contains("Noturno"));
    }

    @Test
    void testPreencherTabelaTurmaDoesNotDuplicateOnSecondCall() throws SQLException {
        TurmaDAO.preencherTabelaTurma(connection);
        TurmaDAO.preencherTabelaTurma(connection);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM turma")) {
            assertTrue(rs.next());
            assertEquals(5, rs.getInt(1), "Não deve duplicar registros na tabela turma.");
        }

        assertEquals(5, TurmaDAO.listaCurso.size());
        assertEquals(5, TurmaDAO.listaTurma.size());
        assertEquals(3, TurmaDAO.listaTurno.size());
    }

    @Test
    void testCarregarListas() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO turma (curso, turma, turno) VALUES " +
                    "('Quimica', 'Q1A', 'Manhã'), " +
                    "('Fisica', 'F2B', 'Tarde'), " +
                    "('Quimica', 'Q1B', 'Manhã');");
        }

        TurmaDAO.carregarListas(connection);

        assertFalse(TurmaDAO.listaCurso.isEmpty());
        assertFalse(TurmaDAO.listaTurma.isEmpty());
        assertFalse(TurmaDAO.listaTurno.isEmpty());

        assertEquals(2, TurmaDAO.listaCurso.size(), "Deve ter 2 cursos únicos: Quimica, Fisica");
        assertTrue(TurmaDAO.listaCurso.contains("Quimica"));
        assertTrue(TurmaDAO.listaCurso.contains("Fisica"));

        assertEquals(3, TurmaDAO.listaTurma.size(), "Deve ter 3 turmas únicas: Q1A, F2B, Q1B");
        assertTrue(TurmaDAO.listaTurma.contains("Q1A"));
        assertTrue(TurmaDAO.listaTurma.contains("F2B"));
        assertTrue(TurmaDAO.listaTurma.contains("Q1B"));

        assertEquals(2, TurmaDAO.listaTurno.size(), "Deve ter 2 turnos únicos: Manhã, Tarde");
        assertTrue(TurmaDAO.listaTurno.contains("Manhã"));
        assertTrue(TurmaDAO.listaTurno.contains("Tarde"));
    }

    @Test
    void testBuscarIDMatriculaTurma() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO cadastro (id, matricula, nome, telefone, data_de_nascimento, curso, cpf, turno, turma) " +
                    "VALUES (100, 12345, 'Teste Aluno', '111111111', '2000-01-01', 'Curso Teste', '123.456.789-00', 'Diurno', 'A')");
            stmt.execute("INSERT INTO turma (id, curso, turma, turno) VALUES (200, 'Curso Teste', 'A', 'Diurno')");
        }

        List<Integer> ids = TurmaDAO.buscarIDMatriculaTurma(12345, "A", connection);

        assertNotNull(ids);
        assertEquals(2, ids.size());
        assertEquals(100, ids.get(0), "ID do aluno deve ser 100.");
        assertEquals(200, ids.get(1), "ID da turma deve ser 200.");

        List<Integer> idsInexistentes = TurmaDAO.buscarIDMatriculaTurma(99999, "Z", connection);
        assertEquals(-1, idsInexistentes.get(0), "ID de matrícula inexistente deve ser -1.");
        assertEquals(-1, idsInexistentes.get(1), "ID de turma inexistente deve ser -1.");
    }

    @Test
    void testInserirAlunoTurma() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO cadastro (id, matricula, nome, telefone, data_de_nascimento, curso, cpf, turno, turma) " +
                    "VALUES (1, 54321, 'Aluno Vinculo', '222222222', '2001-02-02', 'Curso Teste', '987.654.321-00', 'Noturno', 'B')");
            stmt.execute("INSERT INTO turma (id, curso, turma, turno) VALUES (2, 'Curso Teste', 'B', 'Noturno')");
        }

        TurmaDAO.inserirAlunoTurma(54321, "B", connection);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM alunoturma WHERE idaluno = 1 AND idturma = 2")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1), "Deve inserir 1 registro na tabela alunoturma.");
        }
    }

    @Test
    void testInserirTurmas() throws SQLException {
        TurmaDAO.inserirTurmas(connection, "Nova Turma", "Novo Curso", "Manhã");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM turma WHERE turma = 'Nova Turma' AND curso = 'Novo Curso'")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1), "Deve inserir a nova turma.");
        }
    }

    @Test
    void testLerTurmasSemFiltro() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO cadastro (id, matricula, nome, telefone, data_de_nascimento, curso, cpf, turno, turma) VALUES " +
                    "(1, 101, 'Aluno Um', '1', '2000-01-01', 'C1', '111', 'T1', 'A')," +
                    "(2, 102, 'Aluno Dois', '2', '2000-01-02', 'C2', '222', 'T2', 'B')");
        }

        ObservableList<Aluno> alunos = TurmaDAO.lerTurmas("Nenhum", connection);

        assertNotNull(alunos);
        assertEquals(2, alunos.size());
        assertEquals("Aluno Um", alunos.get(0).getNome());
        assertEquals("Aluno Dois", alunos.get(1).getNome());
    }

    @Test
    void testLerTurmasComFiltro() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO cadastro (id, matricula, nome, telefone, data_de_nascimento, curso, cpf, turno, turma) VALUES " +
                    "(1, 101, 'Aluno X', '1', '2000-01-01', 'C1', '111', 'T1', 'Turma A')," +
                    "(2, 102, 'Aluno Y', '2', '2000-01-02', 'C2', '222', 'T2', 'Turma B')," +
                    "(3, 103, 'Aluno Z', '3', '2000-01-03', 'C3', '333', 'T3', 'Turma A')");
        }

        ObservableList<Aluno> alunosFiltrados = TurmaDAO.lerTurmas("Turma A", connection);

        assertNotNull(alunosFiltrados);
        assertEquals(2, alunosFiltrados.size());
        assertTrue(alunosFiltrados.stream().anyMatch(a -> a.getNome().equals("Aluno X")));
        assertTrue(alunosFiltrados.stream().anyMatch(a -> a.getNome().equals("Aluno Z")));
        assertFalse(alunosFiltrados.stream().anyMatch(a -> a.getNome().equals("Aluno Y")));
    }

    @Test
    void testDesvincularAluno() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO cadastro (id, matricula, nome, telefone, data_de_nascimento, curso, cpf, turno, turma) " +
                    "VALUES (50, 98765, 'Aluno Desvincular', '333333333', '1999-03-03', 'Teste', '444.555.666-77', 'Integral', 'C')");
            stmt.execute("INSERT INTO turma (id, curso, turma, turno) VALUES (51, 'Teste', 'C', 'Integral')");
        }
        TurmaDAO.inserirAlunoTurma(98765, "C", connection);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM alunoturma WHERE idaluno = 50")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1), "Aluno deve estar inicialmente vinculado.");
        }

        TurmaDAO.desvincularAluno(connection, 98765);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM alunoturma WHERE idaluno = 50")) {
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1), "Aluno deve ter sido desvinculado.");
        }
    }
}