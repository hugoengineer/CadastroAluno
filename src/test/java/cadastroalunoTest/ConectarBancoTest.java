package cadastroalunoTest;

import com.example.cadastroaluno.src.ConectarBanco;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ConectarBancoTest {

    @Test
    void testGetConnectionSuccess() {
        Connection connection = null;
        try {
            connection = ConectarBanco.getConnection();
            assertNotNull(connection, "A conexão não deve ser nula.");
            assertFalse(connection.isClosed(), "A conexão não deve estar fechada.");
        } catch (RuntimeException e) {
            fail("Erro inesperado ao tentar conectar ao banco de dados: " + e.getMessage());
        } catch (SQLException e) {
            fail("Erro de SQL inesperado ao verificar a conexão: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão no teste: " + e.getMessage());
                }
            }
        }
    }

    @Test
    void testGetConnectionFailure() {
        String originalUrl = ConectarBanco.URL;
        String originalUser = ConectarBanco.USER;
        String originalPassword = ConectarBanco.PASSWORD;

        try {
            Assertions.assertThrows(RuntimeException.class, () -> {
                ConectarBanco.getConnection();
            }, "Deve lançar RuntimeException quando a conexão falha.");


        } finally {
        }
    }
}