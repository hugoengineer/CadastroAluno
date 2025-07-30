package cadastroalunoTest;

import com.example.cadastroaluno.src.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    void testUsuarioConstructorAndGetters() {
        String expectedNome = "testUser";
        String expectedSenha = "testPassword";
        String expectedTipo = "admin";

        Usuario usuario = new Usuario(expectedNome, expectedSenha, expectedTipo);

        assertNotNull(usuario, "O objeto Usuario não deve ser nulo.");

        assertEquals(expectedNome, usuario.getNome(), "O nome do usuário deve corresponder ao valor fornecido.");
        assertEquals(expectedSenha, usuario.getSenha(), "A senha do usuário deve corresponder ao valor fornecido.");

    }

    @Test
    void testUsuarioWithEmptyValues() {
        String expectedNome = "";
        String expectedSenha = "";
        String expectedTipo = "";

        Usuario usuario = new Usuario(expectedNome, expectedSenha, expectedTipo);

        assertNotNull(usuario);
        assertEquals(expectedNome, usuario.getNome());
        assertEquals(expectedSenha, usuario.getSenha());
    }

    @Test
    void testUsuarioWithNullValues() {
        String expectedNome = null;
        String expectedSenha = null;
        String expectedTipo = null;

        Usuario usuario = new Usuario(expectedNome, expectedSenha, expectedTipo);

        assertNotNull(usuario);
        assertNull(usuario.getNome());
        assertNull(usuario.getSenha());
    }
}