package com.example.cadastroaluno.src;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TurmaDAO {
    public static ObservableList<String> listaCurso = FXCollections.observableArrayList();
    public static ObservableList<String> listaTurma = FXCollections.observableArrayList();
    public static ObservableList<String> listaTurno = FXCollections.observableArrayList();

    static String preencherTabela = "INSERT INTO turma (curso, turma, turno) VALUES ('Historia', '215F', 'Noturno')," +
            "('Letras', '201F', 'Matutino')," +
            "('direito', '203C', 'Vespertino')," +
            "('Engenharia de Software', '216F', 'Noturno')," +
            "('Matematica', '221C', 'Matutino')";

    public static void preencherTabelaTurma(Connection conexao){
        String sqlPreencher = "SELECT COUNT(*) FROM turma";

        try (PreparedStatement stmtPreencher = conexao.prepareStatement(sqlPreencher)) {

            ResultSet rs = stmtPreencher.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0) {
                try (PreparedStatement stmt = conexao.prepareStatement(preencherTabela)) {
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("\nErro ao preencher tabela.\n");
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        carregarListas(conexao);
    }
    public static void carregarListas(Connection conexao){
        listaCurso.clear();
        listaTurma.clear();
        listaTurno.clear();
        String sqlCarregar = "SELECT DISTINCT curso, turma, turno FROM turma";
        try(PreparedStatement stmt = conexao.prepareStatement(sqlCarregar)){
            ResultSet rs =stmt.executeQuery();
            while(rs.next()){
                String curso = rs.getString("curso");
                String turma = rs.getString("turma");
                String turno = rs.getString("turno");

                if (!listaCurso.contains(curso)) listaCurso.add(curso);
                if (!listaTurma.contains(turma)) listaTurma.add(turma);
                if (!listaTurno.contains(turno)) listaTurno.add(turno);
            }
        } catch (SQLException e){
            System.out.println("\nErro ao carregar listas de curso, turma e turno.\n");
            e.printStackTrace();
        }

    }

    public static void inserirAlunoTurma(int matricula, String turma, Connection conexao){
        List<Integer> lista = buscarIDMatriculaTurma(matricula, turma, conexao);

        if (lista != null && lista.size() >= 2) {
            String sqlInserir = "INSERT INTO alunoturma (idaluno, idturma) VALUES (?, ?)";
            try(PreparedStatement stmt = conexao.prepareStatement(sqlInserir)){
                stmt.setInt(1, lista.get(0));
                stmt.setInt(2, lista.get(1));
                stmt.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        } else {
            System.err.println("Erro: IDs de matrícula ou turma não encontrados para vincular o aluno.");
        }
    }

    public static List<Integer> buscarIDMatriculaTurma(int matricula, String turma, Connection conexao){
        String sqlSelectIdMatricula = "SELECT id FROM cadastro WHERE matricula = ?";
        String sqlSelectIdTurma =  "SELECT id FROM turma WHERE turma = ?";
        int idMatricula = -1;
        int idTurma = -1;

        try (PreparedStatement stmtMatricula = conexao.prepareStatement(sqlSelectIdMatricula)) {
            stmtMatricula.setInt(1, matricula);
            try (ResultSet rsMatricula = stmtMatricula.executeQuery()) {
                if (rsMatricula.next()) idMatricula = rsMatricula.getInt("id");
            }

        }catch (SQLException e) {
            System.err.println("Erro SQL ao buscar IDs ou vincular aluno/turma: " + e.getMessage());
            e.printStackTrace();
        }

        if (turma != null) {
            try (PreparedStatement stmtTurma = conexao.prepareStatement(sqlSelectIdTurma)) {
                stmtTurma.setString(1, turma);
                try (ResultSet rsTurma = stmtTurma.executeQuery()) {
                    if (rsTurma.next()) idTurma = rsTurma.getInt("id");
                }
            }catch (SQLException e) {
                System.err.println("Erro SQL ao buscar IDs ou vincular aluno/turma: " + e.getMessage());
                e.printStackTrace();
            }
        }


        List<Integer> lista = new ArrayList<>();
        lista.add(idMatricula);
        lista.add(idTurma);
        return lista;
    }

    public static void inserirTurmas(Connection conexao, String turma, String curso, String turno){
        String sqlInserirTurmas = "INSERT INTO turma (curso, turma, turno) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sqlInserirTurmas)){
            stmt.setString(1, curso);
            stmt.setString(2, turma);
            stmt.setString(3, turno);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static ObservableList<Aluno> lerTurmas(String filtro, Connection conexao){
        ObservableList<Aluno> listaAluno  = FXCollections.observableArrayList();
        if("Nenhum".equals(filtro) || filtro == null || filtro.isEmpty()) {
            listaAluno = lerSemFiltro(conexao, filtro);

        }
        else {
            listaAluno = lerComFiltro(conexao, filtro);
        }
        return listaAluno;
    }

    public static ObservableList lerSemFiltro(Connection conexao, String filtro) {
        ObservableList <Aluno> listaAluno = FXCollections.observableArrayList();
        String sqlNenhum = "SELECT id, nome, cpf, matricula, curso, telefone, data_de_nascimento, turno, turma FROM cadastro";
        try(PreparedStatement stmt = conexao.prepareStatement(sqlNenhum)){

            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    int matricula = rs.getInt("matricula");
                    String curso = rs.getString("curso");
                    String telefone = rs.getString("telefone");
                    java.sql.Date dataSql = rs.getDate("data_de_nascimento");
                    String turno = rs.getString("turno");
                    String turma = rs.getString("turma");

                    Aluno aluno = new Aluno(nome, cpf, curso, matricula, telefone, dataSql, turno, turma);
                    listaAluno.add(aluno);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ler alunos do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
        return listaAluno;
    }

    public static ObservableList lerComFiltro(Connection conexao, String filtro) {
        ObservableList <Aluno> listaAluno = FXCollections.observableArrayList();
        String sqlFiltro = "SELECT id, nome, cpf, matricula, curso, telefone, data_de_nascimento, turno, turma" +
                " FROM cadastro WHERE turma LIKE ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlFiltro)) {
            stmt.setString(1,"%" + filtro + "%");
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    int matricula = rs.getInt("matricula");
                    String curso = rs.getString("curso");
                    String telefone = rs.getString("telefone");
                    java.sql.Date dataSql = rs.getDate("data_de_nascimento");
                    String turno = rs.getString("turno");
                    String turma = rs.getString("turma");

                    Aluno aluno = new Aluno(nome, cpf, curso, matricula, telefone, dataSql, turno, turma);
                    listaAluno.add(aluno);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  listaAluno;
    }

    public static void desvincularAluno(Connection conexao, int matricula) {
        System.out.println(matricula);
        List<Integer> listaId = buscarIDMatriculaTurma(matricula, null, conexao);
        if (!listaId.isEmpty() && listaId.get(0) != -1) {
            int id = listaId.get(0);
            System.out.println(id);
            String sql = "delete from alunoturma where idaluno = ?";

            try (PreparedStatement stmt = conexao.prepareStatement(sql)){
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            System.err.println("Erro: ID do aluno para desvincular não encontrado para a matrícula: " + matricula);
        }
    }
}