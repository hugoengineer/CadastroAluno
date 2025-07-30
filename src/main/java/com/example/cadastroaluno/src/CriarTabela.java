package com.example.cadastroaluno.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CriarTabela {
    private Connection conexao;

    public CriarTabela(Connection conexao) {
        this.conexao = conexao;
    }

    public void CriarTabela(String sql) {
        try(PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("\nTabela criada com sucesso!\n");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void PreencherTabelaLogin() throws SQLException {
        String sqlPreencherLogin = "SELECT COUNT(*) FROM login";
        String sqlPreencherUsuario = "SELECT COUNT(*) FROM cadastro";
        Curso.criarCurso();

        try (PreparedStatement stmtPreencher = conexao.prepareStatement(sqlPreencherLogin)) {
            ResultSet rs = stmtPreencher.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0){
                String sql = "INSERT INTO login(username, senha, tipo) VALUES ('admin', 'admin', 'adm')";

                try(PreparedStatement stmt = conexao.prepareStatement(sql)){
                    stmt.executeUpdate();
                }catch (SQLException e){
                    System.out.println("\nErro ao preencher tabela.\n");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao preencher a tabela login" + e.getMessage());
            e.printStackTrace();
        }

        try (PreparedStatement stmtPreencher = conexao.prepareStatement(sqlPreencherUsuario)) {
            ResultSet rs = stmtPreencher.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0){
                String sql = "INSERT INTO cadastro(matricula, nome, telefone, curso, cpf, data_de_nascimento)" +
                        " VALUES ('2025345678', 'Jacinto Roberto', '62997985463', 'História', '75438792035', '1979-04-24')";

                try(PreparedStatement stmt = conexao.prepareStatement(sql)){
                    stmt.executeUpdate();
                }catch (SQLException e){
                    System.out.println("\nErro ao preencher tabela.\n");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao preencher a tabela cadastro" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void PreencherTabelaCurso() throws SQLException {
        String sqlPreencher = "SELECT COUNT(*) FROM curso";
        Curso.criarCurso();

        try (PreparedStatement stmtPreencher = conexao.prepareStatement(sqlPreencher)) {

            ResultSet rs = stmtPreencher.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0) {
                for (Curso curso : Curso.getListaCurso()) {
                    String sql2 = "INSERT INTO curso(materia, professor, periodo) VALUES (?, ?, ?)";

                    try (PreparedStatement stmt = conexao.prepareStatement(sql2)) {
                        stmt.setString(1, curso.getcurso());
                        stmt.setString(2, curso.getProfessor());
                        stmt.setString(3, curso.getPeriodo());
                        stmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("\nErro ao preencher tabela.\n");
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao preencher a tabela curso " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void GerarTabela(Connection conexao) throws SQLException {
        CriarTabela tabelas = new CriarTabela(conexao);
        String sql1 = "create table if not exists cadastro(" +
                "id serial primary key," +
                "matricula  int unique not null," +
                "nome varchar(255) not null," +
                "telefone varchar(25)," +
                "data_de_nascimento date not null," +
                "curso varchar(100) not null," +
                "turma varchar(50) not null," +
                "turno varchar(50) not null," +
                "cpf varchar(100) not null)";
        String sql2 = "Create table if not exists login(" +
                "id serial primary key," +
                "username varchar(50) unique not null," +
                "senha varchar(255) not null," +
                "tipo varchar(25) not null)";
        String sql3 = "CREATE TABLE IF NOT EXISTS curso (" +
                "id SERIAL PRIMARY KEY," +
                "materia VARCHAR(100) NOT NULL," +
                "professor VARCHAR(100)," +
                "periodo VARCHAR(25) NOT NULL" +
                ")";
        String sql4 = "CREATE TABLE IF NOT EXISTS turma (" +
                "id SERIAL PRIMARY KEY," +
                "curso VARCHAR(50) NOT NULL," +
                "turma VARCHAR(50) NOT NULL," +
                "turno VARCHAR(50) NOT NULL)";
        String sql5 = "CREATE TABLE IF NOT EXISTS alunoturma (" +
                "id SERIAL PRIMARY KEY," +
                "idaluno INT NOT NULL," +
                "idturma INT NOT NULL)";
        tabelas.CriarTabela(sql1);
        tabelas.CriarTabela(sql2);
        tabelas.PreencherTabelaLogin();
        tabelas.CriarTabela(sql3);
        tabelas.PreencherTabelaCurso();
        tabelas.CriarTabela(sql4);
        TurmaDAO.preencherTabelaTurma(conexao);
        tabelas.CriarTabela(sql5);
    }
}