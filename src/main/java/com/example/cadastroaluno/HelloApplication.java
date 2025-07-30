package com.example.cadastroaluno;

import cadastroaluno.ControllerTelaAluno;
import com.example.cadastroaluno.src.AlunoDAOimpl;
import com.example.cadastroaluno.src.ConectarBanco;
import com.example.cadastroaluno.src.CriarTabela;
import com.example.cadastroaluno.src.Usuario;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @FXML
    private Button btnConfirmar;
    @FXML
    private TextField caixaNome;
    @FXML
    PasswordField caixaSenha;
    @FXML
    private Text textoErro;
    @FXML
    private CheckBox mostrarSenhaCheckBox;
    @FXML
    private TextField textoSenha;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private AlunoDAOimpl alunoDAOimpl;
    private Connection conexao;
    private Usuario usuarioLogado;
    private String senhaOriginal = "";

    @FXML
    public void initialize() {
        conexao = ConectarBanco.getConnection();
        alunoDAOimpl = new AlunoDAOimpl(conexao);


        textoSenha.setVisible(false);

        btnConfirmar.setOnMouseClicked(event -> {
            String nome = caixaNome.getText().strip();
            String senha = mostrarSenhaCheckBox.isSelected() ? textoSenha.getText().trim() : caixaSenha.getText().trim();
            if (validarAdm(nome, senha)) {
                trocarTela("HelloController", "hello-view.fxml", event);
            } else if (validarAluno(nome, senha)) {
                usuarioLogado = new Usuario(nome, senha, "aluno");
                ControllerTelaAluno controller = new ControllerTelaAluno();
                System.out.println("Aluno criado");
                controller.setUsuarioLogado(usuarioLogado);
                trocarTela("HelloController", "telaOpcaoAluno-view.fxml", event);
            } else {
                textoErro.setText("Login inválido");
            }
        });

        mostrarSenhaCheckBox.setOnAction(event -> {
            if (mostrarSenhaCheckBox.isSelected()) {
                senhaOriginal = caixaSenha.getText();
                textoSenha.setText(senhaOriginal);
                textoSenha.setVisible(true);
                caixaSenha.setVisible(false);

            } else {
                caixaSenha.setText(senhaOriginal);
                textoSenha.setVisible(false);
                caixaSenha.setVisible(true);
                caixaSenha.setPromptText("Senha");
            }
        });
    }

    public boolean validarAdm(String nome, String senha) {
        String sql = "SELECT CASE " +
                "WHEN COUNT(id) > 0 THEN TRUE " +
                "ELSE FALSE " +
                "END AS resultado " +
                "FROM login " +
                "WHERE username = ? " +
                "AND senha = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("resultado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            textoErro.setText("Erro ao validar Administrador: " + e.getMessage());
        }
        return false;
    }

    public boolean validarAluno(String nome, String senha) {
        String sql = "SELECT CASE " +
                "WHEN COUNT(id) > 0 THEN TRUE " +
                "ELSE FALSE " +
                "END AS resultado " +
                "FROM cadastro " +
                "WHERE nome = ? " +
                "AND matricula = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);

            int senhaInt = Integer.parseInt(senha);
            stmt.setInt(2, senhaInt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("resultado");
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            textoErro.setText("Erro ao validar Aluno: " + e.getMessage());
        }
        return false;
    }

    public void trocarTela(String nomeTela, String enderecoFXML, javafx.scene.input.MouseEvent mouseEvent) {
        try {
            URL urlFXML = getClass().getResource(enderecoFXML);
            if (urlFXML == null) {
                System.err.println("FXML não encontrado: " + enderecoFXML);
                return;
            }
            FXMLLoader loader = new FXMLLoader(urlFXML);
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();

            Scene novaCena = new Scene(root);
            stage.setScene(novaCena);
            stage.show();

        } catch (IOException erro) {
            erro.printStackTrace();
            System.err.println("Erro ao carregar FXML: " + erro.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection conexao = ConectarBanco.getConnection();
        String javafxVersion = System.getProperty("javafx.runtime.version");
        System.out.println("JavaFX version: " + javafxVersion);
        CriarTabela.GerarTabela(conexao);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        String fxmlPath = "telaLogin-view.fxml";
        URL urlFXML = getClass().getResource(fxmlPath);
        if (urlFXML == null) {
            System.err.println("FXML não encontrado: " + fxmlPath);
            return;
        }
        Parent root = FXMLLoader.load(urlFXML);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Cadastro Aluno");
        stage.show();

        URL iconURL = getClass().getResource("/images/83098131-uma-coruja-marrom-bonito-com-ilustração-de-livro.jpg");
        if (iconURL != null) {
            Image icon = new Image(iconURL.toExternalForm());
            stage.getIcons().add(icon);
        } else {
            System.err.println("Ícone não encontrado: seu_icone.png");
        }
    }


}