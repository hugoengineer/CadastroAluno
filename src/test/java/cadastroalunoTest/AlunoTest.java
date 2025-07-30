package cadastroalunoTest;

import com.example.cadastroaluno.src.Aluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlunoTest {

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        Date dataNascimentoExemplo = Date.valueOf(LocalDate.of(2000, 5, 15));

        aluno = new Aluno(
                "Maria Silva",
                "123.456.789-00",
                "Engenharia de Software",
                12345,
                "(XX) 99999-8888",
                dataNascimentoExemplo,
                "Noturno",
                "TCC A"
        );
    }

    @Test
    void testGetNome() {
        assertEquals("Maria Silva", aluno.getNome(), "O nome retornado deve ser 'Maria Silva'");
    }

    @Test
    void testGetCpf() {
        assertEquals("123.456.789-00", aluno.getCpf(), "O CPF retornado deve ser '123.456.789-00'");
    }

    @Test
    void testGetCurso() {
        assertEquals("Engenharia de Software", aluno.getCurso(), "O curso retornado deve ser 'Engenharia de Software'");
    }

    @Test
    void testGetMatricula() {
        assertEquals(12345, aluno.getMatricula(), "A matrícula retornada deve ser 12345");
    }

    @Test
    void testGetTurno() {
        assertEquals("Noturno", aluno.getTurno(), "O turno retornado deve ser 'Noturno'");
    }

    @Test
    void testGetTurma() {
        assertEquals("TCC A", aluno.getTurma(), "A turma retornada deve ser 'TCC A'");
    }

    @Test
    void testGetTelefone() {
        assertEquals("(XX) 99999-8888", aluno.getTelefone(), "O telefone retornado deve ser '(XX) 99999-8888'");
    }

    @Test
    void testGetDatanascimento() {
        Date dataNascimentoEsperada = Date.valueOf(LocalDate.of(2000, 5, 15));
        assertEquals(dataNascimentoEsperada, aluno.getDatanascimento(), "A data de nascimento deve ser 2000-05-15");
        assertNotNull(aluno.getDatanascimento(), "A data de nascimento não deve ser nula");
    }

    @Test
    void testSetNome() {
        String novoNome = "João Pereira";
        aluno.setNome(novoNome);
        assertEquals(novoNome, aluno.getNome(), "O nome deve ser atualizado para 'João Pereira'");
    }

    @Test
    void testSetTelefone() {
        String novoTelefone = "(YY) 77777-6666";
        aluno.setTelefone(novoTelefone);
        assertEquals(novoTelefone, aluno.getTelefone(), "O telefone deve ser atualizado para '(YY) 77777-6666'");
    }

    @Test
    void testSetCurso() {
        String novoCurso = "Ciência da Computação";
        aluno.setCurso(novoCurso);
        assertEquals(novoCurso, aluno.getCurso(), "O curso deve ser atualizado para 'Ciência da Computação'");
    }

    @Test
    void testSetTurno() {
        String novoTurno = "Diurno";
        aluno.setTurno(novoTurno);
        assertEquals(novoTurno, aluno.getTurno(), "O turno deve ser atualizado para 'Diurno'");
    }

    @Test
    void testSetTurma() {
        String novaTurma = "TCC B";
        aluno.setTurma(novaTurma);
        assertEquals(novaTurma, aluno.getTurma(), "A turma deve ser atualizada para 'TCC B'");
    }

    @Test
    void testCpfIsImmutable() {
        String cpfOriginal = aluno.getCpf();
        assertNotNull(cpfOriginal, "CPF não deve ser nulo");
        assertEquals("123.456.789-00", cpfOriginal, "CPF deve ser imutável e igual ao valor inicial");
    }

    @Test
    void testMatriculaIsImmutable() {
        int matriculaOriginal = aluno.getMatricula();
        assertEquals(12345, matriculaOriginal, "Matrícula deve ser imutável e igual ao valor inicial");
    }
}