package com.example.cadastroaluno.src;

import java.sql.Date;
import java.time.Year;
import java.util.Random;

public class Aluno {
    private String nome;
    private final String cpf;
    private String curso;
    private String telefone;
    private final int matricula;
    private final Date datanascimento;
    private String turma;
    private String turno;

    public Aluno(String nome, String cpf, String curso, int matricula, String telefone, Date datanascimento, String turno, String turma) {
        this.nome = nome;
        this.cpf = cpf;
        this.curso = curso;
        this.matricula = matricula;
        this.telefone = telefone;
        this.datanascimento = datanascimento;
        this.turno = turno;
        this.turma  = turma;
    }


    public String getNome() {
        return this.nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getCurso() {
        return this.curso;
    }

    public int getMatricula() {return this.matricula;}

    public String getTurno() {return this.turno;}

    public String getTurma() {return this.turma;}

    public String getTelefone() {
        return this.telefone;
    }

    public Date getDatanascimento() {
        return this.datanascimento;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
    public void setCurso(String curso){
        this.curso = curso;
    }
    public void setTurno(String turno){
        this.turno = turno;
    }
    public void setTurma(String turma){
        this.turma = turma;
    }
}