package com.example.cadastroaluno.src;

import java.util.List;

public interface AlunoDAO {
    void inserirAluno(Aluno aluno);
    List<Aluno> listarAlunos();
    List<Aluno> buscarPorMatricula(int matricula);
    void atualizarAluno(Aluno aluno);
    void deletarAluno(Aluno aluno);
    List <Aluno> buscarPorNome(String nome);
}