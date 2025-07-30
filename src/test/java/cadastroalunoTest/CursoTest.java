package cadastroalunoTest;

import com.example.cadastroaluno.src.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CursoTest {

    @BeforeEach
    void setUp() {
        Curso.getListaCurso().clear();
    }

    @Test
    void testCursoConstructorAndGetters() {
        String expectedCurso = "Matematica";
        String expectedProfessor = "Augusto Barros";
        String expectedPeriodo = "8 periodos";

        Curso curso = new Curso(expectedCurso, expectedProfessor, expectedPeriodo);

        assertNotNull(curso, "O objeto Curso não deve ser nulo.");
        assertEquals(expectedCurso, curso.getcurso(), "O nome do curso deve corresponder ao valor fornecido.");
        assertEquals(expectedProfessor, curso.getProfessor(), "O nome do professor deve corresponder ao valor fornecido.");
        assertEquals(expectedPeriodo, curso.getPeriodo(), "O período deve corresponder ao valor fornecido.");
    }

    @Test
    void testCriarCursoPopulatesList() {
        assertTrue(Curso.getListaCurso().isEmpty(), "A lista de cursos deve estar vazia antes de criarCurso.");

        Curso.criarCurso();

        assertFalse(Curso.getListaCurso().isEmpty(), "A lista de cursos não deve estar vazia após criarCurso.");
        assertEquals(5, Curso.getListaCurso().size(), "A lista de cursos deve conter 5 cursos após criarCurso.");

        List<Curso> cursos = Curso.getListaCurso();
        assertTrue(cursos.stream().anyMatch(c -> c.getcurso().equals("Matematica")), "Deve conter 'Matematica'");
        assertTrue(cursos.stream().anyMatch(c -> c.getcurso().equals("Letras")), "Deve conter 'Letras'");
        assertTrue(cursos.stream().anyMatch(c -> c.getcurso().equals("Historia")), "Deve conter 'Historia'");
        assertTrue(cursos.stream().anyMatch(c -> c.getcurso().equals("Engenharia de Software")), "Deve conter 'Engenharia de Software'");
        assertTrue(cursos.stream().anyMatch(c -> c.getcurso().equals("Direito")), "Deve conter 'Direito'");
    }

    @Test
    void testCriarCursoDoesNotDuplicateOnSecondCall() {
        Curso.criarCurso();
        assertEquals(5, Curso.getListaCurso().size(), "A lista deve ter 5 cursos após a primeira chamada.");

        Curso.criarCurso();
        assertEquals(10, Curso.getListaCurso().size(), "A lista deve ter 10 cursos após a segunda chamada, pois o método adiciona novamente.");

    }

    @Test
    void testGetListaCursoReturnsCorrectListReference() {
        List<Curso> firstList = Curso.getListaCurso();
        Curso.criarCurso();
        List<Curso> secondList = Curso.getListaCurso();

        assertNotNull(firstList, "A lista retornada não deve ser nula.");
        assertSame(firstList, secondList, "getListaCurso deve retornar a mesma instância de lista.");
        assertEquals(5, firstList.size(), "A lista deve conter os cursos após criarCurso.");
    }
}