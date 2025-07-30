package com.example.cadastroaluno.src;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlunoDAOimpl implements AlunoDAO{
    private Connection conexao;

    public AlunoDAOimpl(Connection conexao){
        this.conexao = conexao;
    }
    public static Aluno alunoCopia;
    public static List<Aluno> alunos = new ArrayList<>();

    @Override
    public void inserirAluno(Aluno aluno) {
        String sql = "insert into cadastro(matricula, nome, telefone, data_de_nascimento, curso, cpf, turno, turma)\n    values(?, ?, ?, ?, ?, ?, ?, ?)\n";
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setInt(1, aluno.getMatricula());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getTelefone());
            stmt.setDate(4, aluno.getDatanascimento());
            stmt.setString(5, aluno.getCurso());
            stmt.setString(6, aluno.getCpf());
            stmt.setString(7, aluno.getTurno());
            stmt.setString(8, aluno.getTurma());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            }
        salvarAluno(aluno);
    }
    public void salvarAluno(Aluno aluno){
        TurmaDAO.inserirAlunoTurma(aluno.getMatricula(), aluno.getTurma(), conexao);

    }

    public void copiarAluno(Aluno aluno){
        alunoCopia = aluno;
    }

    @Override
    public List<Aluno> listarAlunos() {
        String sql = "SELECT  id, nome, cpf, curso, turno, turma, matricula, telefone, data_de_nascimento FROM cadastro ORDER BY id";
        try(PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
           while(rs.next()){
               Aluno aluno  = new Aluno(
                       rs.getString("nome"),
                       rs.getString("cpf"),
                       rs.getString("curso"),
                       rs.getInt("matricula"),
                       rs.getString("telefone"),
                       rs.getDate("data_de_nascimento"),
                       rs.getString("turno"),
                       rs.getString("turma"));

               alunos.add(aluno);
           }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return alunos;
    }

    @Override
    public List<Aluno> buscarPorMatricula(int matricula) {
        List<Aluno> alunosEncontrados = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, curso, turno, turma, matricula, telefone, data_de_nascimento FROM cadastro WHERE matricula = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("curso"),
                        rs.getInt("matricula"),
                        rs.getString("telefone"),
                        rs.getDate("data_de_nascimento"),
                        rs.getString("turno"),
                        rs.getString("turma"));
                alunosEncontrados.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunosEncontrados;
    }

    @Override
    public List<Aluno> buscarPorNome(String nome) {
        List<Aluno> alunosEncontrados = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, curso, turno, turma, matricula, telefone, data_de_nascimento FROM cadastro WHERE nome = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("curso"),
                        rs.getInt("matricula"),
                        rs.getString("telefone"),
                        rs.getDate("data_de_nascimento"),
                        rs.getString("turno"),
                        rs.getString("turma"));
                alunosEncontrados.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunosEncontrados;
    }


    @Override
    public void atualizarAluno(Aluno aluno) {
        String sql = "UPDATE cadastro SET nome = ?, cpf = ?, curso = ?, turma = ?, turno = ?, matricula = ?, telefone = ?, data_de_nascimento = ? WHERE matricula = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getCurso());
            stmt.setString(4, aluno.getTurma());
            stmt.setString(5, aluno.getTurno());
            stmt.setInt(6, aluno.getMatricula());
            stmt.setString(7, aluno.getTelefone());
            stmt.setDate(8, aluno.getDatanascimento());
            stmt.setInt(9, aluno.getMatricula());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletarAluno(Aluno aluno) {
        int matricula = aluno.getMatricula();
        String sql = "DELETE FROM cadastro WHERE matricula = ?";
        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            TurmaDAO.desvincularAluno( conexao, matricula);
            stmt.setInt(1, matricula);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static String converterData(java.sql.Date dataSql){
        java.util.Date dataUtil = new java.util.Date(dataSql.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

        return sdf.format(dataUtil);
    }
}
