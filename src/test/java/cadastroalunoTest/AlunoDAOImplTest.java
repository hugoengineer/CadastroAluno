package cadastroalunoTest;

import com.example.cadastroaluno.src.Aluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlunoDAOImplTest {

    private Connection connection;
    private AlunoDAOimpl alunoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
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
            stmt.execute("CREATE TABLE turma_aluno (" +
                    "matricula_aluno INT PRIMARY KEY," +
                    "nome_turma VARCHAR(50)" +
                    ");");
        }

        alunoDAO = new AlunoDAOimpl(connection);
        AlunoDAOimpl.alunos.clear();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testInserirAluno() {
        Aluno aluno = new Aluno(
                "Joao Silva", "111.111.111-11", "Computacao", 10001,
                "(11)9999-1111", Date.valueOf(LocalDate.of(1995, 1, 1)), "Diurno", "A");

        alunoDAO.inserirAluno(aluno);

        List<Aluno> alunosInseridos = alunoDAO.buscarPorMatricula(10001);
        assertFalse(alunosInseridos.isEmpty(), "Aluno deve ter sido inserido");
        assertEquals("Joao Silva", alunosInseridos.get(0).getNome());
    }

    @Test
    void testListarAlunos() {
        Aluno aluno1 = new Aluno("Ana", "222.222.222-22", "Engenharia", 20001, "(22)2222-2222", Date.valueOf(LocalDate.of(1996, 2, 2)), "Noturno", "B");
        Aluno aluno2 = new Aluno("Bruno", "333.333.333-33", "Arquitetura", 30001, "(33)3333-3333", Date.valueOf(LocalDate.of(1997, 3, 3)), "Diurno", "C");

        alunoDAO.inserirAluno(aluno1);
        alunoDAO.inserirAluno(aluno2);

        List<Aluno> todosAlunos = alunoDAO.listarAlunos();

        assertNotNull(todosAlunos, "A lista de alunos não deve ser nula");
        assertEquals(2, todosAlunos.size(), "Deve listar 2 alunos");
        assertTrue(todosAlunos.stream().anyMatch(a -> a.getNome().equals("Ana")), "Deve conter Ana");
        assertTrue(todosAlunos.stream().anyMatch(a -> a.getNome().equals("Bruno")), "Deve conter Bruno");
    }

    @Test
    void testBuscarPorMatricula() {
        Aluno aluno = new Aluno("Carla", "444.444.444-44", "Medicina", 40001, "(44)4444-4444", Date.valueOf(LocalDate.of(1998, 4, 4)), "Diurno", "D");
        alunoDAO.inserirAluno(aluno);

        List<Aluno> encontrado = alunoDAO.buscarPorMatricula(40001);
        assertFalse(encontrado.isEmpty(), "Aluno deve ser encontrado pela matrícula");
        assertEquals("Carla", encontrado.get(0).getNome());

        List<Aluno> naoEncontrado = alunoDAO.buscarPorMatricula(99999);
        assertTrue(naoEncontrado.isEmpty(), "Não deve encontrar aluno com matrícula inexistente");
    }

    @Test
    void testBuscarPorNome() {
        Aluno aluno = new Aluno("Daniel", "555.555.555-55", "Direito", 50001, "(55)5555-5555", Date.valueOf(LocalDate.of(1999, 5, 5)), "Noturno", "E");
        alunoDAO.inserirAluno(aluno);

        List<Aluno> encontrado = alunoDAO.buscarPorNome("Daniel");
        assertFalse(encontrado.isEmpty(), "Aluno deve ser encontrado pelo nome");
        assertEquals(50001, encontrado.get(0).getMatricula());

        List<Aluno> naoEncontrado = alunoDAO.buscarPorNome("Nome Inexistente");
        assertTrue(naoEncontrado.isEmpty(), "Não deve encontrar aluno com nome inexistente");
    }

    @Test
    void testAtualizarAluno() {
        Aluno alunoOriginal = new Aluno("Eva", "666.666.666-66", "Historia", 60001, "(66)6666-6666", Date.valueOf(LocalDate.of(2000, 6, 6)), "Diurno", "F");
        alunoDAO.inserirAluno(alunoOriginal);

        Aluno alunoAtualizado = new Aluno(
                "Eva Maria", alunoOriginal.getCpf(), "Sociologia", alunoOriginal.getMatricula(),
                "(66)6666-6677", Date.valueOf(LocalDate.of(2001, 7, 7)), "Noturno", "G"
        );
        alunoDAO.atualizarAluno(alunoAtualizado);

        List<Aluno> alunosAtualizados = alunoDAO.buscarPorMatricula(alunoOriginal.getMatricula());
        assertFalse(alunosAtualizados.isEmpty(), "Aluno atualizado deve ser encontrado");
        assertEquals("Eva Maria", alunosAtualizados.get(0).getNome(), "Nome deve ser atualizado");
        assertEquals("Sociologia", alunosAtualizados.get(0).getCurso(), "Curso deve ser atualizado");
        assertEquals("(66)6666-6677", alunosAtualizados.get(0).getTelefone(), "Telefone deve ser atualizado");
        assertEquals(Date.valueOf(LocalDate.of(2001, 7, 7)), alunosAtualizados.get(0).getDatanascimento(), "Data de nascimento deve ser atualizada");
    }

    @Test
    void testDeletarAluno() {
        Aluno aluno = new Aluno("Fabio", "777.777.777-77", "Fisica", 70001, "(77)7777-7777", Date.valueOf(LocalDate.of(2001, 7, 7)), "Noturno", "H");
        alunoDAO.inserirAluno(aluno);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO turma_aluno (matricula_aluno, nome_turma) VALUES (70001, 'H');");
        } catch (SQLException e) {
            fail("Falha ao inserir em turma_aluno para teste de exclusão: " + e.getMessage());
        }

        alunoDAO.deletarAluno(aluno);

        List<Aluno> alunosRestantes = alunoDAO.buscarPorMatricula(70001);
        assertTrue(alunosRestantes.isEmpty(), "Aluno deve ter sido deletado");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM turma_aluno WHERE matricula_aluno = 70001")) {
            rs.next();
            assertEquals(0, rs.getInt(1), "Aluno deve ter sido desvinculado da turma.");
        } catch (SQLException e) {
            fail("Falha ao verificar desvinculação da turma: " + e.getMessage());
        }
    }

    @Test
    void testConverterData() {
        Date sqlDate = Date.valueOf(LocalDate.of(2023, 10, 26));
        String expected = "26/10/2023";
        assertEquals(expected, AlunoDAOimpl.converterData(sqlDate), "A data deve ser formatada corretamente.");
    }

    @Test
    void testCopiarAluno() {
        Aluno original = new Aluno("Pedro", "888.888.888-88", "Matematica", 80001, "(88)8888-8888", Date.valueOf(LocalDate.of(1994, 8, 8)), "Diurno", "I");
        alunoDAO.copiarAluno(original);

        assertNotNull(AlunoDAOimpl.alunoCopia, "Aluno copiado não deve ser nulo");
        assertEquals("Pedro", AlunoDAOimpl.alunoCopia.getNome(), "Nome do aluno copiado deve ser 'Pedro'");
        assertSame(original, AlunoDAOimpl.alunoCopia, "A cópia deve ser a mesma instância do original");
    }
}